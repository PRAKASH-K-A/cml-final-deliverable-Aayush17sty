package com.team5;

/**
 * Customer - Represents a trading client (Individual or Institution)
 * This is Reference Data - used to validate credit limits and customer type.
 */
public class Customer {
    
    private String customerCode;
    private String customerName;
    private String customerType;  // "RETAIL" or "INSTITUTIONAL"
    private double creditLimit;
    
    public Customer(String customerCode, String customerName, String customerType, double creditLimit) {
        this.customerCode = customerCode;
        this.customerName = customerName;
        this.customerType = customerType;
        this.creditLimit = creditLimit;
    }
    
    // Getters
    public String getCustomerCode() {
        return customerCode;
    }
    
    public String getCustomerName() {
        return customerName;
    }
    
    public String getCustomerType() {
        return customerType;
    }
    
    public double getCreditLimit() {
        return creditLimit;
    }
    
    // Setters
    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }
    
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
    
    public void setCustomerType(String customerType) {
        this.customerType = customerType;
    }
    
    public void setCreditLimit(double creditLimit) {
        this.creditLimit = creditLimit;
    }
    
    @Override
    public String toString() {
        return "Customer{" +
                "customerCode='" + customerCode + '\'' +
                ", customerName='" + customerName + '\'' +
                ", customerType='" + customerType + '\'' +
                ", creditLimit=" + creditLimit +
                '}';
    }
}
