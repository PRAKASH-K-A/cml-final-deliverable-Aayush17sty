package com.team5;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Execution - Represents a realized trade (result of order matching).
 * Created when a Buy and Sell order match in the matching engine.
 */
public class Execution {
    
    private String execId;
    private String orderId;           // Incoming order ID
    private String restingOrderId;    // Resting order ID (LAB 7 fix)
    private String symbol;
    private char side;                // Side of incoming order
    private int execQty;
    private double execPrice;
    private LocalDateTime matchTime;
    
    public Execution(String execId, String orderId, String symbol, char side, int execQty, double execPrice) {
        this(execId, orderId, null, symbol, side, execQty, execPrice);
    }
    
    // LAB 7 Constructor: Track both incoming and resting orders in the match
    public Execution(String execId, String orderId, String restingOrderId, String symbol, char side, int execQty, double execPrice) {
        this.execId = execId;
        this.orderId = orderId;
        this.restingOrderId = restingOrderId;
        this.symbol = symbol;
        this.side = side;
        this.execQty = execQty;
        this.execPrice = execPrice;
        this.matchTime = LocalDateTime.now();
    }
    
    // Getters
    public String getExecId() {
        return execId;
    }
    
    public String getOrderId() {
        return orderId;
    }
    
    public String getRestingOrderId() {
        return restingOrderId;
    }
    
    public String getSymbol() {
        return symbol;
    }
    
    public char getSide() {
        return side;
    }
    
    public int getExecQty() {
        return execQty;
    }
    
    public double getExecPrice() {
        return execPrice;
    }
    
    public LocalDateTime getMatchTime() {
        return matchTime;
    }
    
    // Setters
    public void setExecId(String execId) {
        this.execId = execId;
    }
    
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
    
    public void setRestingOrderId(String restingOrderId) {
        this.restingOrderId = restingOrderId;
    }
    
    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }
    
    public void setSide(char side) {
        this.side = side;
    }
    
    public void setExecQty(int execQty) {
        this.execQty = execQty;
    }
    
    public void setExecPrice(double execPrice) {
        this.execPrice = execPrice;
    }
    
    public void setMatchTime(LocalDateTime matchTime) {
        this.matchTime = matchTime;
    }
    
    /**
     * Calculate total notional value of the execution
     */
    public double getNotionalValue() {
        return execQty * execPrice;
    }
    
    @Override
    public String toString() {
        return "Execution{" +
                "execId='" + execId + '\'' +
                ", orderId='" + orderId + '\'' +
                ", symbol='" + symbol + '\'' +
                ", side=" + (side == '1' ? "BUY" : "SELL") +
                ", execQty=" + execQty +
                ", execPrice=" + execPrice +
                ", notional=" + getNotionalValue() +
                ", matchTime=" + matchTime +
                '}';
    }
}
