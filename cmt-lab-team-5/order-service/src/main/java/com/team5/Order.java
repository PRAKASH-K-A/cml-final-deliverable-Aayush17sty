package com.team5;

public class Order {
    
    private String orderId;              // Server-generated ID for database
    private String clOrdID;
    private String symbol;
    private char side;
    private double price;
    private double orderQty;             // Original order quantity (immutable - original amount requested)
    private double cumQty;               // Cumulative/filled quantity (accumulates as trades execute)
    private double leavesQty;            // Leaves quantity (remaining unfilled = orderQty - cumQty)
    private String ordStatus;
    private String sessionId;            // FIX session that placed this order (LAB 7 fix)

    // Constructor
    public Order() {
    }

    public Order(String clOrdID, String symbol, char side, double price, double quantity) {
        this.clOrdID = clOrdID;
        this.symbol = symbol;
        this.side = side;
        this.price = price;
        this.orderQty = quantity;         // Original order quantity set once
        this.cumQty = 0;                  // No fills yet
        this.leavesQty = quantity;        // All quantity remains unfilled initially
        this.ordStatus = "NEW";
    }

    public Order(String clOrdID, String symbol, char side, double price, double quantity, double cumQty) {
        this.clOrdID = clOrdID;
        this.symbol = symbol;
        this.side = side;
        this.price = price;
        this.orderQty = quantity;         // Original order quantity set once
        this.cumQty = cumQty;             // Some fills may have occurred
        this.leavesQty = quantity - cumQty; // Calculate remaining
        this.ordStatus = "NEW";
    }

    public Order(String clOrdID, String symbol, char side, double price, double quantity, double cumQty, String ordStatus) {
        this.clOrdID = clOrdID;
        this.symbol = symbol;
        this.side = side;
        this.price = price;
        this.orderQty = quantity;         // Original order quantity set once
        this.cumQty = cumQty;             // Accumulated filled quantity
        this.leavesQty = quantity - cumQty; // Calculate remaining
        this.ordStatus = ordStatus;
    }

    // Getters and Setters
    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getClOrdID() {
        return clOrdID;
    }

    public void setClOrdID(String clOrdID) {
        this.clOrdID = clOrdID;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public char getSide() {
        return side;
    }

    public void setSide(char side) {
        this.side = side;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * Get original order quantity - the immutable total amount ordered
     */
    public double getOrderQty() {
        return orderQty;
    }

    public void setOrderQty(double orderQty) {
        this.orderQty = orderQty;
    }

    /**
     * Get cumulative/filled quantity - the total amount executed so far
     */
    public double getCumQty() {
        return cumQty;
    }

    public void setCumQty(double cumQty) {
        this.cumQty = cumQty;
        // Automatically recalculate leaves quantity to maintain consistency
        this.leavesQty = this.orderQty - cumQty;
    }

    /**
     * Get leaves quantity - the remaining unfilled amount (readonly - calculated field)
     * = orderQty - cumQty
     */
    public double getLeavesQty() {
        return leavesQty;
    }

    public void setLeavesQty(double leavesQty) {
        this.leavesQty = leavesQty;
    }

    /**
     * @deprecated Use getOrderQty() instead - quantity field is now split into orderQty/cumQty/leavesQty
     */
    @Deprecated
    public double getQuantity() {
        return leavesQty;  // Return leaves qty for backward compatibility
    }

    /**
     * @deprecated Use setOrderQty() instead
     */
    @Deprecated
    public void setQuantity(double quantity) {
        this.orderQty = quantity;
        this.leavesQty = quantity;
    }

    public String getOrdStatus() {
        return ordStatus;
    }

    public void setOrdStatus(String ordStatus) {
        this.ordStatus = ordStatus;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    /**
     * Fill/reduce order quantity during matching engine execution
     * 
     * Increments cumQty and decrements leavesQty when a trade (partial or full) is executed.
     * Updates ordStatus based on whether order is fully filled or partially filled.
     * 
     * Note: This is called AFTER an Execution is created with the fill amount.
     * 
     * @param fillQty The quantity amount to add to cumulative (what was just filled)
     */
    public void reduceLeavesQty(double fillQty) {
        this.cumQty += fillQty;          // Add to cumulative filled quantity
        this.leavesQty -= fillQty;       // Subtract from remaining leaves quantity
        
        // Ensure leaves qty doesn't go negative due to floating point math
        if (this.leavesQty < 0) {
            this.leavesQty = 0;
        }
        
        // Update order status based on fills
        if (this.leavesQty <= 0) {
            this.ordStatus = "FILLED";
        } else {
            this.ordStatus = "PARTIALLY_FILLED";
        }
    }

    /**
     * @deprecated Use reduceLeavesQty() instead - this method mutated 'quantity' field
     */
    @Deprecated
    public void reduceQty(double reduction) {
        reduceLeavesQty(reduction);
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderId='" + orderId + '\'' +
                ", clOrdID='" + clOrdID + '\'' +
                ", symbol='" + symbol + '\'' +
                ", side=" + side +
                ", price=" + price +
                ", orderQty=" + orderQty +            // Original total order quantity
                ", cumQty=" + cumQty +                // Total amount filled
                ", leavesQty=" + leavesQty +          // Remaining to fill
                ", ordStatus='" + ordStatus + '\'' +
                ", sessionId='" + sessionId + '\'' +
                '}';
    }
}
