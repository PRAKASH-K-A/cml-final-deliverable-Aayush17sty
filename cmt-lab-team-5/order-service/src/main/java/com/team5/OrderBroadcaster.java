package com.team5;

import com.google.gson.Gson;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

/**
 * WebSocket server for broadcasting order updates to connected UI clients.
 * Listens on port 8080 and pushes real-time order data as JSON.
 */
public class OrderBroadcaster extends WebSocketServer {
    private final Gson gson = new Gson();

    public OrderBroadcaster(int port) {
        super(new InetSocketAddress(port));
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        System.out.println("UI Connected: " + conn.getRemoteSocketAddress());
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        // We don't expect messages from the UI in this lab
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        System.out.println("UI Disconnected: " + conn.getRemoteSocketAddress());
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        System.err.println("WebSocket error: " + ex.getMessage());
    }

    @Override
    public void onStart() {
        System.out.println("WebSocket Server started on port " + getPort());
    }

    /**
     * Broadcast order to all connected UI clients
     */
    public void broadcastOrder(Order order) {
        String json = gson.toJson(order);
        broadcast(json);
        System.out.println("Broadcasted order to UI: " + order.getClOrdID());
    }

    /**
     * LAB 7/8: Broadcast trade execution to all connected UI clients
     * 
     * Sends Execution object as JSON with trade details (qty, price, symbol)
     * UI can update trade blotter / ticker with this data
     */
    public void broadcastTrade(Execution execution) {
        // Wrap execution for client clarity
        java.util.Map<String, Object> tradeMessage = new java.util.HashMap<>();
        tradeMessage.put("type", "TRADE");
        tradeMessage.put("exec_id", execution.getExecId());
        tradeMessage.put("symbol", execution.getSymbol());
        tradeMessage.put("side", execution.getSide() == '1' ? "BUY" : "SELL");
        tradeMessage.put("quantity", execution.getExecQty());
        tradeMessage.put("price", execution.getExecPrice());
        tradeMessage.put("notional", execution.getNotionalValue());
        tradeMessage.put("timestamp", execution.getMatchTime().toString());
        
        String json = gson.toJson(tradeMessage);
        broadcast(json);
        System.out.printf("Broadcasted trade to UI: %s %d @ %.2f%n", 
                execution.getSymbol(), execution.getExecQty(), execution.getExecPrice());
    }
}
