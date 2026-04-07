package com.team5;
import quickfix.Application;
import quickfix.Message;
import quickfix.SessionID;
import quickfix.DoNotSend;
import quickfix.FieldNotFound;
import quickfix.IncorrectDataFormat;
import quickfix.IncorrectTagValue;
import quickfix.RejectLogon;
import quickfix.UnsupportedMessageType;
import quickfix.Session;
import quickfix.field.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.BlockingQueue;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.List;

public class OrderApplication implements Application {
    private final OrderBroadcaster broadcaster;
    private final BlockingQueue<Object> persistenceQueue;  // Now handles both Order and Execution (Lab 7)
    private final Map<String, Security> securityMaster;
    private final Map<String, Customer> customerMaster;
    private final ConcurrentHashMap<String, OrderBook> orderBooks; // Per-symbol order books
    
    // LAB 7 FIX: Track all orders by orderId for trade reporting both sides of match
    private final ConcurrentHashMap<String, Order> allOrders = new ConcurrentHashMap<>();
    
    // Track net position per symbol (buys - sells)
    private final ConcurrentHashMap<String, Double> symbolNetPosition = new ConcurrentHashMap<>();
    // Order books: buy and sell orders per symbol
    private final ConcurrentHashMap<String, Queue<PendingOrder>> buyOrders = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Queue<PendingOrder>> sellOrders = new ConcurrentHashMap<>();
    
    // Inner class to track pending orders
    private static class PendingOrder {
        String clOrdID;
        double price;
        double remainingQty;
        
        PendingOrder(String clOrdID, double price, double qty) {
            this.clOrdID = clOrdID;
            this.price = price;
            this.remainingQty = qty;
        }
    }
    
    public OrderApplication() {
        this(null, null, null, null);
    }
    
    public OrderApplication(OrderBroadcaster broadcaster) {
        this(broadcaster, null, null, null);
    }
    
    public OrderApplication(OrderBroadcaster broadcaster, BlockingQueue<Object> persistenceQueue) {
        this(broadcaster, persistenceQueue, null, null);
    }
    
    public OrderApplication(OrderBroadcaster broadcaster, BlockingQueue<Object> persistenceQueue,
                           Map<String, Security> securityMaster, Map<String, Customer> customerMaster) {
        this.broadcaster = broadcaster;
        this.persistenceQueue = persistenceQueue;
        this.securityMaster = securityMaster != null ? securityMaster : new ConcurrentHashMap<>();
        this.customerMaster = customerMaster != null ? customerMaster : new ConcurrentHashMap<>();
        this.orderBooks = new ConcurrentHashMap<>();
    }
    
    @Override
    public void onCreate(SessionID sessionId) {
    System.out.println("Session Created: " + sessionId);
    }
    @Override
    public void onLogon(SessionID sessionId) {
    System.out.println("LOGON Success: " + sessionId);
    }
    @Override
    public void onLogout(SessionID sessionId) {
    System.out.println("LOGOUT: " + sessionId);
    }
    @Override
    public void toAdmin(Message message, SessionID sessionId) {
    // Used for administrative messages (Heartbeats, Logons)
    try {
        String msgType = message.getHeader().getString(MsgType.FIELD);
        if (!msgType.equals("0")) { // Don't spam heartbeats
            System.out.println("[toAdmin] Sending admin message: " + msgType);
        }
    } catch (Exception e) {
        // Ignore
    }
    }
    @Override
    public void fromAdmin(Message message, SessionID sessionId) throws FieldNotFound,
    IncorrectDataFormat, IncorrectTagValue, RejectLogon {
    // Received administrative messages
    try {
        String msgType = message.getHeader().getString(MsgType.FIELD);
        if (!msgType.equals("0")) { // Don't spam heartbeats
            System.out.println("[fromAdmin] Received admin message: " + msgType);
        }
    } catch (Exception e) {
        // Ignore
    }
    }
    @Override
    public void toApp(Message message, SessionID sessionId) throws DoNotSend {
    // Outgoing business messages
    }
    @Override
    public void fromApp(Message message, SessionID sessionId) throws FieldNotFound,
    IncorrectDataFormat, IncorrectTagValue, UnsupportedMessageType {
    // Incoming business messages (New Orders will arrive here)
    
    // LAB 9: Capture ingress timestamp (nanosecond precision)
    long ingressTime = System.nanoTime();
    
    try {
        // 1. Identify Message Type
        String msgType = message.getHeader().getString(MsgType.FIELD);
        System.out.println("[fromApp] Received message with type: " + msgType);
        
        if (msgType.equals("D") || msgType.equals(MsgType.ORDER_SINGLE)) {
            System.out.println("[fromApp] Processing as NewOrderSingle (D)");
            processNewOrder(message, sessionId, ingressTime);
        } else {
            System.out.println("[fromApp] Received unknown message type: " + msgType);
        }
    } catch (Exception e) {
        System.err.println("[fromApp] ERROR processing message: " + e.getMessage());
        e.printStackTrace();
        throw new UnsupportedMessageType();
    }
    }
    
    /**
     * LAB 7: Process incoming NewOrderSingle messages with Matching Engine integration
     * LAB 9: Added ingressTime parameter for latency tracking
     * 
     * Flow:
     * 1. Extract and validate FIX message fields
     * 2. Create Order object
     * 3. Get OrderBook for symbol (create if needed)
     * 4. Call OrderBook.match() to execute Price-Time Priority matching
     * 5. Generate Execution objects and process trades
     * 6. Add remainder to book or reject if needed
     */
    private void processNewOrder(Message message, SessionID sessionId, long ingressTime) {
        try {
            String clOrdId = message.getString(ClOrdID.FIELD);
            String symbol = message.getString(Symbol.FIELD);
            char side = message.getChar(Side.FIELD);
            double qty = message.getDouble(OrderQty.FIELD);
            double price = message.getDouble(Price.FIELD);
            
            System.out.printf("[LAB 7] ORDER RECEIVED: ID=%s Side=%s Sym=%s Px=%.2f Qty=%.0f%n",
                clOrdId, (side == '1' ? "BUY" : "SELL"), symbol, price, qty);
            
            // ===== VALIDATION PHASE =====
            if (qty <= 0 || price <= 0) {
                System.out.println("[LAB 7] Rejecting order - Invalid Price or Qty");
                sendReject(message, sessionId, "Invalid Price or Qty", ingressTime);
                return;
            }
            
            // Validation: Check if Symbol exists in Security Master
            if (!securityMaster.containsKey(symbol)) {
                System.out.println("[LAB 7] Rejecting order - Invalid Symbol: " + symbol);
                sendReject(message, sessionId, "Symbol not found in market: " + symbol, ingressTime);
                return;
            }
            
            Security security = securityMaster.get(symbol);
            System.out.println("[LAB 7] Symbol validated: " + security.getSymbol());
            
            // ===== SETUP PHASE =====
            String orderId = "ORD_" + System.currentTimeMillis() + "_" + (int)(Math.random() * 10000);
            
            // Send ACK to client (This is the NewOrderAck - ExecType=0)
            acceptOrder(message, sessionId, orderId, ingressTime);
            
            // Create Order object
            Order order = new Order(clOrdId, symbol, side, price, qty);
            order.setOrderId(orderId);
            order.setSessionId(sessionId.toString()); // LAB 7 FIX: Track which session placed this order
            
            // LAB 7 FIX: Store order in allOrders map for later trade reporting
            allOrders.put(orderId, order);
            System.out.printf("[LAB 7] Order stored in tracking map: %s (Session: %s)%n", orderId, sessionId);
            
            // ===== MATCHING ENGINE: LAB 7 CORE LOGIC =====
            
            // Get or create the OrderBook for this symbol
            OrderBook orderBook = orderBooks.computeIfAbsent(symbol, k -> {
                System.out.printf("[LAB 7] Creating new OrderBook for: %s%n", symbol);
                return new OrderBook(symbol);
            });
            
            // CRITICAL: Call the matching engine
            // This returns a list of Execution objects representing all trades
            java.util.List<Execution> executions = orderBook.match(order);
            
            System.out.printf("[LAB 7] Matching complete - %d trades generated%n", executions.size());
            
            // ===== EXECUTION PROCESSING =====
            
            // Queue the original order for persistence
            if (persistenceQueue != null) {
                persistenceQueue.offer(order);
                System.out.printf("[LAB 7] Order queued for DB persistence: %s%n", clOrdId);
            }
            
            // Broadcast the order to UI
            if (broadcaster != null) {
                broadcaster.broadcastOrder(order);
            }
            
            // Process each trade (execution)
            for (Execution execution : executions) {
                System.out.printf("[LAB 7] Processing Execution: %s - %d @ %.2f%n",
                        execution.getSymbol(), execution.getExecQty(), execution.getExecPrice());
                
                // Queue execution for DB persistence (async)
                if (persistenceQueue != null) {
                    persistenceQueue.offer(execution);
                    System.out.printf("[LAB 7] Execution queued for DB: %s%n", execution.getExecId());
                }
                
                // Broadcast trade to UI
                if (broadcaster != null) {
                    broadcaster.broadcastTrade(execution);
                }
                
                // LAB 7 FIX: Send trade reports for BOTH sides of the match
                // 1. Send report for incoming order
                sendTradeReport(execution, order, sessionId, ingressTime);
                
                // 2. LAB 7: Also send report for the resting order that just got filled
                if (execution.getRestingOrderId() != null) {
                    Order restingOrder = allOrders.get(execution.getRestingOrderId());
                    if (restingOrder != null) {
                        System.out.printf("[LAB 7] Sending trade report for RESTING order: %s (Session: %s)%n", 
                                execution.getRestingOrderId(), restingOrder.getSessionId());
                        // CRITICAL: Send to the resting order's session, not the incoming order's session!
                        SessionID restingOrderSessionId = new SessionID(restingOrder.getSessionId());
                        sendTradeReport(execution, restingOrder, restingOrderSessionId, ingressTime);
                    } else {
                        System.out.println("[LAB 7] WARNING: Resting order not found in allOrders map!");
                    }
                }
            }
            
            // Print current book state for debugging
            System.out.println("[LAB 7] OrderBook snapshot for " + symbol + ":");
            System.out.println("  Best Bid: " + orderBook.getBestBidPrice() + " (Qty: " + orderBook.getTotalBidQty() + ")");
            System.out.println("  Best Ask: " + orderBook.getBestAskPrice() + " (Qty: " + orderBook.getTotalAskQty() + ")");
            System.out.println("  Bid Levels: " + orderBook.getBidLevelCount() + ", Ask Levels: " + orderBook.getAskLevelCount());
            
        } catch (FieldNotFound e) {
            System.err.println("[LAB 7] FieldNotFound: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("[LAB 7] Unexpected error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Send acknowledgment for accepted orders
     * LAB 9: Added ingressTime for latency tracking (egress point for ACK)
     */
    private void acceptOrder(Message request, SessionID sessionId, String orderId, long ingressTime) {
        try {
            System.out.println("[acceptOrder] Creating ExecutionReport ACK...");
            
            // Create an ExecutionReport (MsgType = 8)
            quickfix.fix44.ExecutionReport ack = new quickfix.fix44.ExecutionReport();
            
            // Mandatory Fields mapping
            ack.set(new OrderID(orderId)); // Server generated ID
            ack.set(new ExecID("EXEC_" + System.currentTimeMillis()));
            ack.set(new ClOrdID(request.getString(ClOrdID.FIELD))); // Echo back the Client's ID
            ack.set(new Symbol(request.getString(Symbol.FIELD)));
            ack.set(new Side(request.getChar(Side.FIELD)));
            
            // Status fields: "New"
            ack.set(new ExecType(ExecType.NEW));
            ack.set(new OrdStatus(OrdStatus.NEW));
            
            // Order details - REQUIRED for proper acknowledgment
            ack.set(new OrderQty(request.getDouble(OrderQty.FIELD)));
            ack.set(new OrdType(request.getChar(OrdType.FIELD)));
            ack.set(new Price(request.getDouble(Price.FIELD)));
            
            // Quantity accounting
            ack.set(new LeavesQty(request.getDouble(OrderQty.FIELD)));  // All quantity initially leaves (unfilled)
            ack.set(new CumQty(0));  // No fills yet
            ack.set(new AvgPx(0));
            
            System.out.println("[acceptOrder] Sending ACK message back to client...");
            // Send back to the specific session
            Session.sendToTarget(ack, sessionId);
            
            // LAB 9: Capture egress timestamp and record latency
            long egressTime = System.nanoTime();
            long latency = egressTime - ingressTime;
            PerformanceMonitor.recordLatency(latency);
            
            System.out.println("[acceptOrder] ✅ Order ACCEPTED - ACK sent: " + request.getString(ClOrdID.FIELD));
            
        } catch (Exception e) {
            System.err.println("[acceptOrder] ERROR: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * LAB 7/8: Send trade execution report to client
     * LAB 9: Added ingressTime for latency tracking
     * 
     * Creates and sends an ExecutionReport with ExecType=TRADE
     * The trade happens at the RESTING order's price (already in execution object)
     */
    private void sendTradeReport(Execution execution, Order order, SessionID sessionId, long ingressTime) {
        try {
            System.out.printf("[sendTradeReport] Creating Trade ExecutionReport for Exec: %s%n", 
                    execution.getExecId());
            
            quickfix.fix44.ExecutionReport tradeReport = new quickfix.fix44.ExecutionReport();
            
            // Core identification fields
            tradeReport.set(new OrderID(execution.getOrderId()));
            tradeReport.set(new ExecID(execution.getExecId()));
            tradeReport.set(new ClOrdID("EXEC_" + execution.getExecId())); // Link to original order
            
            // Product details
            tradeReport.set(new Symbol(execution.getSymbol()));
            tradeReport.set(new Side(execution.getSide() == '1' ? '1' : '2'));
            
            // CRITICAL FOR LAB 7: ExecType=TRADE (not New)
            tradeReport.set(new ExecType(ExecType.TRADE)); // 'F' = Filled
            
            // Quantity Summary: Uses order's tracking fields for accurate accounting
            // - CumQty: Total cumulative quantity filled on this order so far
            // - LeavesQty: Remaining unfilled quantity = orderQty - cumQty (after this trade)
            // - LastQty: THIS execution's fill amount
            // - AvgPx: Average execution price (or last execution price if not time-weighted)
            double cumQtyAfterTrade = order.getCumQty();  // Total filled including this trade
            double leavesQtyAfterTrade = order.getLeavesQty(); // Remaining after this trade
            
            tradeReport.set(new OrdStatus(leavesQtyAfterTrade > 0 ? OrdStatus.PARTIALLY_FILLED : OrdStatus.FILLED));
            
            // Trade execution details - Use execution data
            tradeReport.set(new LastPx(execution.getExecPrice())); // Price at which trade occurred
            tradeReport.set(new LastQty(execution.getExecQty()));  // Quantity filled in THIS execution
            tradeReport.set(new CumQty((long) cumQtyAfterTrade));   // Total cumulative quantity
            tradeReport.set(new LeavesQty((long) leavesQtyAfterTrade)); // Remaining unfilled quantity
            tradeReport.set(new AvgPx(execution.getExecPrice()));  // Average price
            
            Session.sendToTarget(tradeReport, sessionId);
            
            // LAB 9: Capture egress timestamp and record latency
            long egressTime = System.nanoTime();
            long latency = egressTime - ingressTime;
            PerformanceMonitor.recordLatency(latency);
            
            System.out.printf("[sendTradeReport] Trade report sent: %d @ %.2f%n", 
                    execution.getExecQty(), execution.getExecPrice());
            
        } catch (Exception e) {
            System.err.println("[sendTradeReport] Error sending trade report: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Send reject message for invalid orders
     * LAB 9: Added ingressTime for latency tracking
     */
    private void sendReject(Message request, SessionID sessionId, String reason, long ingressTime) {
        try {
            System.out.println("[sendReject] Creating ExecutionReport REJECT...");
            
            quickfix.fix44.ExecutionReport reject = new quickfix.fix44.ExecutionReport();
            
            // Mandatory Fields
            reject.set(new OrderID("REJ_" + System.currentTimeMillis()));
            reject.set(new ExecID("EXEC_" + System.currentTimeMillis()));
            reject.set(new ClOrdID(request.getString(ClOrdID.FIELD)));
            reject.set(new Symbol(request.getString(Symbol.FIELD)));
            reject.set(new Side(request.getChar(Side.FIELD)));
            
            // Status: Rejected
            reject.set(new ExecType(ExecType.REJECTED));
            reject.set(new OrdStatus(OrdStatus.REJECTED));
            
            // Order details - REQUIRED for proper rejection report
            try {
                reject.set(new OrderQty(request.getDouble(OrderQty.FIELD)));
                reject.set(new OrdType(request.getChar(OrdType.FIELD)));
                reject.set(new Price(request.getDouble(Price.FIELD)));
            } catch (Exception e) {
                // If fields are missing, set defaults
                reject.set(new OrderQty(0));
                reject.set(new OrdType('2')); // Default to Limit
                reject.set(new Price(0));
            }
            
            // Quantity accounting - 0 for rejected orders
            reject.set(new LeavesQty(0));
            reject.set(new CumQty(0));
            reject.set(new AvgPx(0));
            
            // Rejection reason
            reject.set(new Text(reason));
            
            System.out.println("[sendReject] Sending REJECT message back to client...");
            Session.sendToTarget(reject, sessionId);
            
            // LAB 9: Capture egress timestamp and record latency
            long egressTime = System.nanoTime();
            long latency = egressTime - ingressTime;
            PerformanceMonitor.recordLatency(latency);
            
            System.out.println("[sendReject] ❌ Order REJECTED (" + reason + ")");
            
        } catch (Exception e) {
            System.err.println("[sendReject] ERROR: " + e.getMessage());
            e.printStackTrace();
        }
    }
}