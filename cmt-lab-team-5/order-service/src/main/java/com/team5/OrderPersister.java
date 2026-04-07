package com.team5;

import java.util.concurrent.BlockingQueue;

/**
 * LAB 7: OrderPersister - Background worker thread that asynchronously persists orders and executions to MySQL.
 * 
 * This worker decouples the fast FIX message ingestion path from the slow database write path.
 * Orders and Execution objects are queued in memory and written to the database without blocking the trading engine.
 * 
 * Updated for Lab 7: Now handles both Order and Execution objects.
 */
public class OrderPersister implements Runnable {

    private final BlockingQueue<Object> queue;
    private volatile boolean running = true;

    public OrderPersister(BlockingQueue<Object> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        System.out.println("[PERSISTENCE] Worker thread started. Watching queue for orders and executions...");

        while (running) {
            try {
                Object item = queue.take();
                
                // Determine type and persist accordingly
                if (item instanceof Order) {
                    Order order = (Order) item;
                    DatabaseManager.insertOrder(order);
                    System.out.printf("[PERSISTENCE] Persisted Order: %s%n", order.getClOrdID());
                    
                } else if (item instanceof Execution) {
                    Execution execution = (Execution) item;
                    DatabaseManager.insertExecution(execution);
                    System.out.printf("[PERSISTENCE] Persisted Execution: %s (%d @ %.2f)%n", 
                            execution.getExecId(), execution.getExecQty(), execution.getExecPrice());
                    
                } else {
                    System.err.printf("[PERSISTENCE] WARNING: Unknown object type in queue: %s%n", 
                            item.getClass().getName());
                }

            } catch (InterruptedException e) {
                if (running) {
                    System.out.println("[PERSISTENCE] Interrupted while waiting for items");
                }
                Thread.currentThread().interrupt();
            } catch (Exception e) {
                System.err.printf("[PERSISTENCE] ERROR persisting object: %s%n", e.getMessage());
                e.printStackTrace();
            }
        }

        System.out.println("[PERSISTENCE] Worker thread stopped.");
    }
    
    public void stop() {
        running = false;
    }
}
