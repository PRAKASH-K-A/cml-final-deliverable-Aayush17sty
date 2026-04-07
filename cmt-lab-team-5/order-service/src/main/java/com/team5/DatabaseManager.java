package com.team5;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * DatabaseManager - Singleton class for handling MySQL database operations.
 * Provides methods to:
 * - Load reference data (Security Master, Customer Master)
 * - Insert orders, executions into persistent audit trail
 * - Update order status
 */
public class DatabaseManager {

    private static final String URL = "jdbc:mysql://localhost:3306/trading_system";
    private static final String USER = "root";
    private static final String PASS = "root"; // MySQL root password

    static {
        try {
            // Load MySQL JDBC Driver
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Insert an order into the orders table.
     * This method is thread-safe and can be called from the persistence worker thread.
     * 
     * Stores the original order quantity (orderQty) - the full amount requested.
     * The status represents the current fill state (NEW, PARTIALLY_FILLED, FILLED).
     * Cumulative fills are tracked through the executions table separately.
     * 
     * @param order The Order object to persist
     */
    public static void insertOrder(Order order) {
        String sql = "INSERT INTO orders (order_id, cl_ord_id, symbol, side, price, quantity, status) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, order.getOrderId());
            pstmt.setString(2, order.getClOrdID());
            pstmt.setString(3, order.getSymbol());
            pstmt.setString(4, String.valueOf(order.getSide()));
            pstmt.setDouble(5, order.getPrice());
            pstmt.setDouble(6, order.getOrderQty());  // Store original order quantity (immutable)
            pstmt.setString(7, order.getOrdStatus());

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("[DB] Order persisted: " + order.getClOrdID());
            }

        } catch (SQLException e) {
            System.err.println("[DB ERROR] Failed to insert order: " + order.getClOrdID());
            e.printStackTrace();
        }
    }

    /**
     * Update order status in the database.
     * Used when an order is partially filled or fully filled.
     * 
     * @param orderId The server-generated order ID
     * @param status The new status
     */
    public static void updateOrderStatus(String orderId, String status) {
        String sql = "UPDATE orders SET status = ? WHERE order_id = ?";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, status);
            pstmt.setString(2, orderId);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("[DB] Order status updated: " + orderId + " -> " + status);
            }

        } catch (SQLException e) {
            System.err.println("[DB ERROR] Failed to update order status: " + orderId);
            e.printStackTrace();
        }
    }

    /**
     * Load all securities from the database into an in-memory Security Master map.
     * This is called once on startup to cache reference data.
     * 
     * @return HashMap<Symbol, Security>
     */
    /**
     * Load all securities from the database into an in-memory Security Master map.
     * If symbols don't exist, auto-populate the database with them.
     * This is called once on startup.
     * 
     * @return HashMap<Symbol, Security>
     */
    public static Map<String, Security> loadSecurityMaster() {
        Map<String, Security> securities = new HashMap<>();
        String sql = "SELECT symbol, security_type, description, lot_size FROM security_master";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                String symbol = rs.getString("symbol");
                String type = rs.getString("security_type");
                String desc = rs.getString("description");
                int lotSize = rs.getInt("lot_size");

                Security security = new Security(symbol, type, desc, lotSize);
                securities.put(symbol, security);
            }

            if (securities.isEmpty()) {
                System.out.println("[DB] Security Master is empty - auto-populating with symbols...");
                populateSecurityMaster(conn);
                // Reload after population
                try (PreparedStatement pstmt2 = conn.prepareStatement(sql);
                     ResultSet rs2 = pstmt2.executeQuery()) {
                    while (rs2.next()) {
                        String symbol = rs2.getString("symbol");
                        String type = rs2.getString("security_type");
                        String desc = rs2.getString("description");
                        int lotSize = rs2.getInt("lot_size");
                        Security security = new Security(symbol, type, desc, lotSize);
                        securities.put(symbol, security);
                    }
                }
            }

            System.out.println("[DB] Security Master loaded: " + securities.size() + " securities");

        } catch (SQLException e) {
            System.err.println("[DB ERROR] Failed to connect to database - using mock data");
            e.printStackTrace();
            securities.putAll(getMockSecurities());
        }

        return securities;
    }

    /**
     * Populate security_master table with all symbols needed for Lab 9 testing
     */
    private static void populateSecurityMaster(Connection conn) {
        String insertSql = "INSERT IGNORE INTO security_master (symbol, security_type, description, lot_size) VALUES (?, ?, ?, 1)";

        try (PreparedStatement pstmt = conn.prepareStatement(insertSql)) {
            
            // Get all symbols to insert
            Map<String, String> symbols = getAllSecuritySymbols();
            
            int count = 0;
            for (Map.Entry<String, String> entry : symbols.entrySet()) {
                String symbol = entry.getKey();
                String description = entry.getValue();
                String type = "EQUITY";
                
                pstmt.setString(1, symbol);
                pstmt.setString(2, type);
                pstmt.setString(3, description);
                pstmt.addBatch();
                count++;
            }
            
            int[] results = pstmt.executeBatch();
            int inserted = 0;
            for (int result : results) {
                if (result > 0) inserted++;
            }
            
            System.out.println("[DB] Inserted " + inserted + " symbols into security_master");
            
        } catch (SQLException e) {
            System.err.println("[DB ERROR] Failed to populate security_master");
            e.printStackTrace();
        }
    }

    /**
     * Get all security symbols - SYNCHRONIZED with simple_fix_sender.py SYMBOLS list
     */
    private static Map<String, String> getAllSecuritySymbols() {
        Map<String, String> symbols = new HashMap<>();
        
        // ===== INTERNATIONAL STOCKS (6) =====
        symbols.put("AAPL", "Apple Inc");
        symbols.put("GOOGL", "Alphabet Inc");
        symbols.put("MSFT", "Microsoft Corp");
        symbols.put("AMZN", "Amazon Inc");
        symbols.put("TSLA", "Tesla Inc");
        symbols.put("IBM", "IBM Corp");
        
        // ===== MAJOR INDIAN BANKS (5) =====
        symbols.put("HDFC", "HDFC Bank Ltd");
        symbols.put("ICICIBANK", "ICICI Bank Ltd");
        symbols.put("INDUSINDBK", "IndusInd Bank Ltd");
        symbols.put("AXISBANK", "Axis Bank Ltd");
        symbols.put("KOTAK", "Kotak Mahindra Bank Ltd");
        symbols.put("INDIGO", "Indigo Airlines Ltd");
        
        // ===== IT COMPANIES (8) =====
        symbols.put("TCS", "Tata Consultancy Services Ltd");
        symbols.put("INFY", "Infosys Ltd");
        symbols.put("WIPRO", "Wipro Ltd");
        symbols.put("TECHM", "Tech Mahindra Ltd");
        symbols.put("HCL", "HCL Technologies Ltd");
        symbols.put("LTTS", "LTT Technologies Ltd");
        symbols.put("MINDTREE", "MindTree Ltd");
        symbols.put("MPHASIS", "MphasiS Ltd");
        
        // ===== AUTO COMPANIES (6) =====
        symbols.put("MARUTI", "Maruti Suzuki India Ltd");
        symbols.put("TATAMOTORS", "Tata Motors Ltd");
        symbols.put("HEROMOTOCO", "Hero MotoCorp Ltd");
        symbols.put("ASHOKLEY", "Ashok Leyland Ltd");
        symbols.put("BAJAJ-AUTO", "Bajaj Auto Ltd");
        symbols.put("MandM", "Mahindra & Mahindra Ltd");
        
        // ===== FMCG (6) =====
        symbols.put("ITC", "ITC Ltd");
        symbols.put("BRITANNIA", "Britannia Industries Ltd");
        symbols.put("NESTLEIND", "Nestle India Ltd");
        symbols.put("HINDUNILVR", "Hindustan Unilever Ltd");
        symbols.put("COLPAL", "Colpal Ltd");
        
        // ===== ADDITIONAL SYMBOLS FROM simple_fix_sender.py =====
        // R-Series
        symbols.put("RIL", "Reliance Industries Ltd");
        
        // S-Series
        symbols.put("SBI", "State Bank of India Ltd");
        symbols.put("BHARATIIND", "Bharati Infrastructure Ltd");
        
        // U-Series
        symbols.put("UFLEX", "UFlex Ltd");
        symbols.put("ULTRACEMCO", "UltraTech Cement Ltd");
        symbols.put("UNIONBANK", "Union Bank of India");
        symbols.put("UNIPHOS", "Uniphos Ltd");
        symbols.put("UNITECH", "Unitech Ltd");
        symbols.put("UNITEDBN", "United Breweries Ltd");
        symbols.put("UMARKTIND", "UNIMECH Aerospace Ltd");
        symbols.put("USHARBI", "USL Pharma Ltd");
        symbols.put("UTIMASTER", "UTI Master Ltd");
        
        // V-Series
        symbols.put("VAIBHAVGBL", "Vaibhav Global Ltd");
        symbols.put("VEDL", "Vedanta Ltd");
        symbols.put("VOLTAS", "Voltas Ltd");
        symbols.put("VRLLOG", "VRL Logistics Ltd");
        symbols.put("VSTIND", "Varun Systems Ltd");
        symbols.put("VASWANI", "Vaswani Industries Ltd");
        symbols.put("VANGUARD", "Vanguard Infra Ltd");
        
        // ===== ADDITIONAL MISCELLANEOUS =====
        symbols.put("PAGE", "Page Industries Ltd");
        symbols.put("PAGEIND", "Page Industries Ltd");
        symbols.put("ALEMBIC", "Alembic Pharma Ltd");
        symbols.put("BAYER", "Bayer CropScience Ltd");
        symbols.put("EMAMI", "Emami Ltd");
        symbols.put("GRINDWELL", "Grindwell Norton Ltd");
        symbols.put("KSCL", "Kores India Ltd");
        symbols.put("LAXMIMACH", "Laxmi Machine Works Ltd");
        symbols.put("LUXIND", "Luxmi Industries Ltd");
        symbols.put("NEWHOTEL", "Crown Hotel Ltd");
        symbols.put("PNBGILTS", "PNB Gilts Ltd");
        symbols.put("RELPOWER", "Reliance Power Ltd");
        symbols.put("TORNTPOWER", "Torrent Power Ltd");
        symbols.put("ADANIPOWER", "Adani Power Ltd");
        symbols.put("MARICO", "Marico Ltd");
        
        // ===== PHARMA (6) =====
        symbols.put("DRREDDY", "Dr. Reddy's Labs Ltd");
        symbols.put("SUNPHARMA", "Sun Pharmaceutical Ltd");
        symbols.put("CIPLA", "Cipla Ltd");
        symbols.put("LUPIN", "Lupin Ltd");
        symbols.put("AURAPHA", "Aura Pharmaceuticals Ltd");
        symbols.put("GLENMARK", "Glenmark Pharmaceuticals Ltd");
        
        // ===== METALS & MINING (6) =====
        symbols.put("TATASTEEL", "Tata Steel Ltd");
        symbols.put("HINDALCO", "Hindalco Industries Ltd");
        symbols.put("HINDZINC", "Hindustan Zinc Ltd");
        symbols.put("VEDL", "Vedanta Ltd");
        symbols.put("COALINDIA", "Coal India Ltd");
        symbols.put("NMDC", "NMDC Ltd");
        
        // ===== POWER (5) =====
        symbols.put("NTPC", "NTPC Ltd");
        symbols.put("POWERGRID", "Power Grid Corp Ltd");
        symbols.put("RELPOWER", "Reliance Power Ltd");
        symbols.put("TORNTPOWER", "Torrent Power Ltd");
        symbols.put("ADANIPOWER", "Adani Power Ltd");
        
        // ===== FINANCE (5) =====
        symbols.put("BAJFINANCE", "Bajaj Finance Ltd");
        symbols.put("BAJAJFINSV", "Bajaj Finserv Ltd");
        symbols.put("SBICARD", "SBI Card Ltd");
        symbols.put("HDFCAMC", "HDFC AMC Ltd");
        symbols.put("MOTILALOFS", "Motilal Oswal Financial Services Ltd");
        
        // ===== REAL ESTATE (5) =====
        symbols.put("DLF", "DLF Ltd");
        symbols.put("PRESTIGE", "Prestige Estates Ltd");
        symbols.put("SOBHA", "Sobha Ltd");
        symbols.put("BRIGADE", "Brigade Enterprises Ltd");
        symbols.put("HIRANIDL", "Hiranandani Gardens Ltd");
        
        // ===== TELECOM (4) =====
        symbols.put("BHARTIARTL", "Bharti Airtel Ltd");
        symbols.put("JIOFINANCE", "Jio Financial Ltd");
        symbols.put("VODAIDEA", "Vodafone Idea Ltd");
        symbols.put("BSNL", "BSNL Ltd");
        
        // ===== CHEMICALS (4) =====
        symbols.put("TATACHEMF", "Tata Chemicals Ltd");
        symbols.put("DEEPAKFERT", "Deepak Fertilizers Ltd");
        symbols.put("AKZOINDIA", "Akzo Nobel Ltd");
        symbols.put("GSKCONS", "GSK Pharma Ltd");
        
        // ===== M-SERIES (Additional)
        
        return symbols;
    }

    /**
     * Get mock securities as fallback (only if database connection fails)
     */
    private static Map<String, Security> getMockSecurities() {
        Map<String, Security> securities = new HashMap<>();
        securities.put("AAPL", new Security("AAPL", "EQUITY", "Apple Inc", 1));
        securities.put("GOOGL", new Security("GOOGL", "EQUITY", "Alphabet Inc", 1));
        securities.put("MSFT", new Security("MSFT", "EQUITY", "Microsoft Corp", 1));
        securities.put("HDFC", new Security("HDFC", "EQUITY", "HDFC Bank", 1));
        securities.put("INFY", new Security("INFY", "EQUITY", "Infosys", 1));
        securities.put("TCS", new Security("TCS", "EQUITY", "Tata Consultancy", 1));
        return securities;
    }

    /**
     * Load all customers from the database into an in-memory Customer Master map.
     * This is called once on startup for credit validation.
     * 
     * @return HashMap<CustomerCode, Customer>
     */
    public static Map<String, Customer> loadCustomerMaster() {
        Map<String, Customer> customers = new HashMap<>();
        String sql = "SELECT customer_code, customer_name, customer_type, credit_limit FROM customer_master";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                String code = rs.getString("customer_code");
                String name = rs.getString("customer_name");
                String type = rs.getString("customer_type");
                double limit = rs.getDouble("credit_limit");

                Customer customer = new Customer(code, name, type, limit);
                customers.put(code, customer);
            }

            System.out.println("[DB] Customer Master loaded: " + customers.size() + " customers");

        } catch (SQLException e) {
            System.err.println("[DB ERROR] Failed to load Customer Master - using mock data for testing");
            
            // LAB 9: Add mock customers for performance testing
            customers.put("CUST001", new Customer("CUST001", "Test Customer 1", "RETAIL", 1000000.00));
            customers.put("CUST002", new Customer("CUST002", "Test Customer 2", "RETAIL", 1000000.00));
            customers.put("CUST003", new Customer("CUST003", "Test Customer 3", "INSTITUTIONAL", 5000000.00));
            customers.put("MINIFIX_CLIENT", new Customer("MINIFIX_CLIENT", "MiniFix Test Client", "TEST", 10000000.00));
            
            System.out.println("[DB] Using mock data: " + customers.size() + " test customers");
        }

        return customers;
    }

    /**
     * Persist an execution (trade) to the database.
     * Called asynchronously whenever orders match.
     * 
     * @param execution The Execution object to persist
     */
    public static void insertExecution(Execution execution) {
        String sql = "INSERT INTO executions (exec_id, order_id, symbol, side, exec_qty, exec_price) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, execution.getExecId());
            pstmt.setString(2, execution.getOrderId());
            pstmt.setString(3, execution.getSymbol());
            pstmt.setString(4, String.valueOf(execution.getSide()));
            pstmt.setInt(5, execution.getExecQty());
            pstmt.setDouble(6, execution.getExecPrice());

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("[DB] Execution persisted: " + execution.getExecId());
            }

        } catch (SQLException e) {
            System.err.println("[DB ERROR] Failed to insert execution: " + execution.getExecId());
            e.printStackTrace();
        }
    }
}
