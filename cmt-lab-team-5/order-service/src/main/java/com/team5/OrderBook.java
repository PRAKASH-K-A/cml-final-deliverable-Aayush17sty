package com.team5;

import java.util.*;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * OrderBook - In-memory representation of buy/sell orders for a given symbol.
 * 
 * Uses ConcurrentSkipListMap to maintain price-sorted order levels:
 * - Bids: Sorted highest price first (for matching with Sell orders)
 * - Asks: Sorted lowest price first (for matching with Buy orders)
 * 
 * Thread-safe for concurrent order insertions from FIX engine.
 * Implements Price-Time Priority matching:
 *   1. Price Priority: Best prices matched first
 *   2. Time Priority: FIFO at each price level
 */
public class OrderBook {
    
    private String symbol;
    
    // Bid side: Price -> List of Orders (sorted descending by price = highest bid first)
    private ConcurrentSkipListMap<Double, List<Order>> bids;
    
    // Ask side: Price -> List of Orders (sorted ascending by price = lowest ask first)
    private ConcurrentSkipListMap<Double, List<Order>> asks;
    
    public OrderBook(String symbol) {
        this.symbol = symbol;
        // Bids: Reverse order (highest price = key 0, descending to lowest)
        this.bids = new ConcurrentSkipListMap<>(Collections.reverseOrder());
        // Asks: Natural order (lowest price = key 0, ascending to highest)
        this.asks = new ConcurrentSkipListMap<>();
    }
    
    public String getSymbol() {
        return symbol;
    }
    
    public ConcurrentSkipListMap<Double, List<Order>> getBids() {
        return bids;
    }
    
    public ConcurrentSkipListMap<Double, List<Order>> getAsks() {
        return asks;
    }
    
    /**
     * Get best bid (highest price) for this symbol
     */
    public Double getBestBidPrice() {
        return bids.isEmpty() ? null : bids.firstKey();
    }
    
    /**
     * Get best ask (lowest price) for this symbol
     */
    public Double getBestAskPrice() {
        return asks.isEmpty() ? null : asks.firstKey();
    }
    
    /**
     * Get number of bid levels (price points with pending Buy orders)
     */
    public int getBidLevelCount() {
        return bids.size();
    }
    
    /**
     * Get number of ask levels (price points with pending Sell orders)
     */
    public int getAskLevelCount() {
        return asks.size();
    }
    
    /**
     * Get total bid quantity (all Buy orders combined)
     * Sums the leaves quantity (unfilled remaining) for all pending Buy orders
     */
    public double getTotalBidQty() {
        return bids.values().stream()
                .flatMap(List::stream)
                .mapToDouble(Order::getLeavesQty)
                .sum();
    }
    
    /**
     * Get total ask quantity (all Sell orders combined)
     * Sums the leaves quantity (unfilled remaining) for all pending Sell orders
     */
    public double getTotalAskQty() {
        return asks.values().stream()
                .flatMap(List::stream)
                .mapToDouble(Order::getLeavesQty)
                .sum();
    }
    
    /**
     * Get spread (difference between best ask and best bid)
     */
    public Double getSpread() {
        Double bestBid = getBestBidPrice();
        Double bestAsk = getBestAskPrice();
        
        if (bestBid == null || bestAsk == null) {
            return null;
        }
        
        return bestAsk - bestBid;
    }
    
    /**
     * Get mid price (average of best bid and ask)
     */
    public Double getMidPrice() {
        Double bestBid = getBestBidPrice();
        Double bestAsk = getBestAskPrice();
        
        if (bestBid == null || bestAsk == null) {
            return null;
        }
        
        return (bestBid + bestAsk) / 2.0;
    }
    
    /**
     * Check if there's a potential cross (buy and sell prices overlap)
     */
    public boolean hasCross() {
        Double bestBid = getBestBidPrice();
        Double bestAsk = getBestAskPrice();
        
        if (bestBid == null || bestAsk == null) {
            return false;
        }
        
        return bestBid >= bestAsk;  // Bid >= Ask means there's a cross
    }
    
    
    /**
     * CORE MATCHING ENGINE
     * 
     * Matches incoming order against existing resting orders on opposite side.
     * Uses Price-Time Priority:
     *   1. Best price first (Buy: lowest ask; Sell: highest bid)
     *   2. Within same price: FIFO (first resting order added = first to match)
     * 
     * For each match:
     *   - Match qty = min(incoming.leavesQty, resting.leavesQty)
     *   - Both orders have leavesQty reduced by match qty
     *   - Both orders have cumQty increased by match qty
     *   - Resting orders removed when leavesQty reaches 0
     *   - Incoming order added to book if leavesQty > 0 after all matches
     */
    public synchronized List<Execution> match(Order incoming) {
        List<Execution> executions = new ArrayList<>();
        
        System.out.printf("[MATCHING] Incoming %s Order: %d shares @ %.2f (OrderQty=%g, CumQty=%g, LeavesQty=%g)%n",
                incoming.getSide() == '1' ? "BUY" : "SELL",
                (int)incoming.getOrderQty(), incoming.getPrice(),
                incoming.getOrderQty(), incoming.getCumQty(), incoming.getLeavesQty());
        
        // Determine opposite side: Buy orders match against Asks, Sell orders match against Bids
        NavigableMap<Double, List<Order>> oppositeSide = (incoming.getSide() == '1') ? asks : bids;
        
        // Price-based matching loop
        matchAgainstRestingOrders(incoming, oppositeSide, executions);
        
        // If incoming order still has unfilled quantity, add it to the book
        if (incoming.getLeavesQty() > 0) {
            addToBook(incoming);
        } else {
            System.out.printf("[MATCHING] Incoming order FULLY FILLED%n");
        }
        
        return executions;
    }
    
    /**
     * Match incoming order against resting orders on opposite side
     * Continues until incoming order is fully filled OR no more resting orders
     */
    private void matchAgainstRestingOrders(Order incoming, NavigableMap<Double, List<Order>> oppositeSide,
                                          List<Execution> executions) {
        boolean isBuy = (incoming.getSide() == '1');
        int tradeCount = 0;
        
        while (incoming.getLeavesQty() > 0 && !oppositeSide.isEmpty()) {
            Double bestPrice = oppositeSide.firstKey();
            
            // Price crossing check:
            // - BUY order: will accept asks at bestPrice if incoming.price >= bestPrice
            // - SELL order: will accept bids at bestPrice if incoming.price <= bestPrice
            if (!canCross(incoming, isBuy, bestPrice)) {
                break;  // No more acceptable prices on opposite side
            }
            
            // Get the first resting order at best price (FIFO within price level)
            List<Order> ordersAtLevel = oppositeSide.get(bestPrice);
            Order restingOrder = ordersAtLevel.get(0);
            
            // Calculate trade quantity: match the smaller of the two orders
            double tradeQty = Math.min(incoming.getLeavesQty(), restingOrder.getLeavesQty());
            
            // Create execution record
            tradeCount++;
            Execution execution = createExecution(incoming, restingOrder, tradeQty, bestPrice);
            executions.add(execution);
            
            // CRITICAL: Update both orders' quantity tracking
            incoming.reduceLeavesQty(tradeQty);     // incoming: cumQty += tradeQty, leavesQty -= tradeQty
            restingOrder.reduceLeavesQty(tradeQty); // resting:  cumQty += tradeQty, leavesQty -= tradeQty
            
            System.out.printf("▪ Trade %d: %.0f shares @ %.2f (Incoming: cumQty now %.0f, leavesQty now %.0f)%n",
                    tradeCount, tradeQty, bestPrice, incoming.getCumQty(), incoming.getLeavesQty());
            
            // Remove fully-filled resting order from book
            if (restingOrder.getLeavesQty() == 0) {
                ordersAtLevel.remove(0);
                // Clean up price level if empty
                if (ordersAtLevel.isEmpty()) {
                    oppositeSide.remove(bestPrice);
                }
                System.out.printf("  → Resting order FULLY FILLED and removed from book%n");
            } else {
                System.out.printf("  → Resting order PARTIALLY FILLED (leavesQty: %.0f)%n", restingOrder.getLeavesQty());
            }
        }
    }
    
    /**
     * Check if incoming order price crosses with resting order price
     * 
     * @param incoming The incoming order
     * @param isBuy True if incoming is a BUY order
     * @param restingPrice The price level of resting orders
     * @return true if the orders can trade at this price
     */
    private boolean canCross(Order incoming, boolean isBuy, double restingPrice) {
        if (isBuy) {
            // BUY order crosses if it's willing to pay AT or ABOVE the ask price
            return incoming.getPrice() >= restingPrice;
        } else {
            // SELL order crosses if it's willing to accept AT or BELOW the bid price
            return incoming.getPrice() <= restingPrice;
        }
    }
    
    /**
     * Add unmatched remaining quantity to the order book
     */
    private void addToBook(Order order) {
        NavigableMap<Double, List<Order>> side = (order.getSide() == '1') ? bids : asks;
        side.computeIfAbsent(order.getPrice(), k -> new LinkedList<>()).add(order);
        
        String sideLabel = order.getSide() == '1' ? "Buy" : "Sell";
        System.out.printf("▪ Remaining %s Order (%.0f shares) added to book @ %.2f [OrderQty: %.0f, CumQty: %.0f, LeavesQty: %.0f]%n",
                sideLabel, order.getLeavesQty(), order.getPrice(),
                order.getOrderQty(), order.getCumQty(), order.getLeavesQty());
    }
    /**
     * Create Execution record for a matched trade
     * Captures both incoming and resting order IDs for proper two-way trade reporting
     */
    private Execution createExecution(Order incoming, Order resting, double quantity, double price) {
        String execId = "EXEC_" + System.currentTimeMillis() + "_" + (int)(Math.random() * 10000);
        
        return new Execution(
                execId,
                incoming.getOrderId(),           // Incoming order that initiated the match
                resting.getOrderId(),            // Resting order that was matched against
                incoming.getSymbol(),
                incoming.getSide(),
                (int) quantity,
                price
        );
    }
    
    @Override
    public String toString() {
        return "OrderBook{" +
                "symbol='" + symbol + '\'' +
                ", bestBid=" + getBestBidPrice() +
                ", bestAsk=" + getBestAskPrice() +
                ", spread=" + getSpread() +
                ", bidLevels=" + getBidLevelCount() +
                ", askLevels=" + getAskLevelCount() +
                '}';
    }
}
