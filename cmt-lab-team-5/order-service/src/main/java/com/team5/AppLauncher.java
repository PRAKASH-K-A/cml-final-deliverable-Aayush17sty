package com.team5;

import quickfix.DefaultMessageFactory;
import quickfix.FileStoreFactory;
import quickfix.ScreenLogFactory;
import quickfix.SessionSettings;
import quickfix.SocketAcceptor;

import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.Map;

public class AppLauncher {
 public static void main(String[] args) {
 try {
 Path settingsPath = resolveSettingsPath();
 SessionSettings settings = new SessionSettings(settingsPath.toString());
 
 // 1. Load Reference Data (Static Masters)
 System.out.println("[INIT] Loading reference data...");
 Map<String, Security> securityMaster = DatabaseManager.loadSecurityMaster();
 Map<String, Customer> customerMaster = DatabaseManager.loadCustomerMaster();
 System.out.println("[INIT] Reference data loaded successfully");
 
 // 2. Create the shared persistence queue (now handles both Order and Execution objects for Lab 7)
 BlockingQueue<Object> persistenceQueue = new LinkedBlockingQueue<>();
 
 // 3. Start the persistence worker thread
 OrderPersister persister = new OrderPersister(persistenceQueue);
 Thread persisterThread = new Thread(persister, "OrderPersisterWorker");
 persisterThread.setDaemon(false);
 persisterThread.start();
 System.out.println("[INIT] Persistence worker thread started");
 
 // 4. Start WebSocket server for UI
 OrderBroadcaster broadcaster = new OrderBroadcaster(8080);
 broadcaster.start();
 System.out.println("[INIT] WebSocket server started on port 8080");
 
 // 5. Create OrderApplication with persistence queue and reference data
 OrderApplication application = new OrderApplication(broadcaster, persistenceQueue, securityMaster, customerMaster);
 FileStoreFactory storeFactory = new FileStoreFactory(settings);
 ScreenLogFactory logFactory = new ScreenLogFactory(settings);
 DefaultMessageFactory messageFactory = new DefaultMessageFactory();
 SocketAcceptor acceptor = new SocketAcceptor(application, storeFactory, settings,
logFactory, messageFactory);
 acceptor.start();
 System.out.println("Order Service started. Listening on port 9876...");

 // Keep the process running
 System.in.read();
 
 // Graceful shutdown
 System.out.println("[SHUTDOWN] Shutting down Order Service...");
 acceptor.stop();
 broadcaster.stop();
 persister.stop();
 persisterThread.join(5000); // Wait max 5 seconds for worker to finish
 System.out.println("[SHUTDOWN] Order Service stopped.");
 } catch (Exception e) {
 e.printStackTrace();
 }
 }

 private static Path resolveSettingsPath() throws FileNotFoundException {
 Path directPath = Paths.get("order-service.cfg");
 if (Files.exists(directPath)) {
 return directPath;
 }

 Path repoRootPath = Paths.get("order-service", "order-service.cfg");
 if (Files.exists(repoRootPath)) {
 return repoRootPath;
 }

 throw new FileNotFoundException("Unable to locate order-service.cfg in the current directory or ./order-service");
 }
}