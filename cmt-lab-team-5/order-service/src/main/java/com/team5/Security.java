package com.team5;

/**
 * Security - Represents a tradable instrument (Stock, Bond, etc.)
 * This is Reference Data - loaded into memory once at startup for fast lookup.
 */
public class Security {
    
    private String symbol;
    private String securityType;  // e.g., "CS" for Common Stock
    private String description;
    private String underlying;    // For derivatives (e.g., options)
    private int lotSize;          // Minimum tradable unit
    
    public Security(String symbol, String securityType, String description, int lotSize) {
        this.symbol = symbol;
        this.securityType = securityType;
        this.description = description;
        this.lotSize = lotSize;
    }
    
    // Getters
    public String getSymbol() {
        return symbol;
    }
    
    public String getSecurityType() {
        return securityType;
    }
    
    public String getDescription() {
        return description;
    }
    
    public String getUnderlying() {
        return underlying;
    }
    
    public int getLotSize() {
        return lotSize;
    }
    
    // Setters
    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }
    
    public void setSecurityType(String securityType) {
        this.securityType = securityType;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public void setUnderlying(String underlying) {
        this.underlying = underlying;
    }
    
    public void setLotSize(int lotSize) {
        this.lotSize = lotSize;
    }
    
    @Override
    public String toString() {
        return "Security{" +
                "symbol='" + symbol + '\'' +
                ", securityType='" + securityType + '\'' +
                ", description='" + description + '\'' +
                ", lotSize=" + lotSize +
                '}';
    }
}
