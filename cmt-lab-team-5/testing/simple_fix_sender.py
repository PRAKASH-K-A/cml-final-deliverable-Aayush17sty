#!/usr/bin/env python3
"""
Simple FIX Order Sender - Automated test script using pure Python
Sends NewOrderSingle messages to the order-service for UI testing
Uses simplefix library (no C++ compilation required)
"""

import simplefix
import socket
import time
import random
import datetime
import sys
import logging
from threading import Thread, Event, Lock
from queue import Queue
from concurrent.futures import ThreadPoolExecutor
import statistics

# Configure logging
logging.basicConfig(
    level=logging.INFO,
    format='%(asctime)s - %(levelname)s - %(message)s'
)
logger = logging.getLogger(__name__)


class SimpleFIXClient:
    """Simple FIX client using simplefix library"""
    
    def __init__(self, host='localhost', port=9876):
        self.host = host
        self.port = port
        self.sock = None
        self.connected = False
        self.sequence_num = 1
        self.sender_comp_id = b'MINIFIX_CLIENT'
        self.target_comp_id = b'EXEC_SERVER'
        self.heartbeat_interval = 30
        self.last_heartbeat = time.time()
        self.running = Event()
        self.session_active = False  # Track if logon completed
        self.order_count = 0
        self.parser = simplefix.FixParser()  # Parser for incoming messages
        
        # Order queue for multithreaded sending
        self.order_queue = Queue()
        self.send_lock = Lock()  # Protect socket writes
        
        # LAB 9: Performance tracking
        self.order_timestamps = {}  # Map ClOrdID -> send_time
        self.latencies = []  # List of latencies in microseconds
        self.exec_count = 0  # Count of execution reports received
        
    def connect(self):
        """Connect to FIX server"""
        try:
            logger.info(f"Connecting to {self.host}:{self.port}...")
            self.sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
            self.sock.settimeout(10)
            self.sock.connect((self.host, self.port))
            self.connected = True
            logger.info("✓ Socket connected")
            return True
        except Exception as e:
            logger.error(f"Connection failed: {e}")
            return False
            
    def disconnect(self):
        """Disconnect from FIX server"""
        if self.sock:
            try:
                self.send_logout()
                self.sock.close()
            except:
                pass
        self.connected = False
        self.running.clear()
        logger.info("Disconnected")
        
    def send_message(self, msg):
        """Send a FIX message"""
        try:
            encoded = msg.encode()
            self.sock.send(encoded)
            self.sequence_num += 1
            return True
        except Exception as e:
            logger.error(f"Error sending message: {e}")
            return False
            
    def send_logon(self):
        """Send Logon message"""
        msg = simplefix.FixMessage()
        msg.append_string('8=FIX.4.4')
        msg.append_pair(35, 'A')  # MsgType = Logon
        msg.append_pair(49, self.sender_comp_id)
        msg.append_pair(56, self.target_comp_id)
        msg.append_pair(34, self.sequence_num)
        msg.append_utc_timestamp(52)  # SendingTime
        msg.append_pair(98, 0)  # EncryptMethod = None
        msg.append_pair(108, self.heartbeat_interval)  # HeartBtInt
        msg.append_pair(141, 'Y')  # ResetSeqNumFlag
        
        logger.info("→ Sending Logon...")
        return self.send_message(msg)
        
    def send_logout(self):
        """Send Logout message"""
        msg = simplefix.FixMessage()
        msg.append_string('8=FIX.4.4')
        msg.append_pair(35, '5')  # MsgType = Logout
        msg.append_pair(49, self.sender_comp_id)
        msg.append_pair(56, self.target_comp_id)
        msg.append_pair(34, self.sequence_num)
        msg.append_utc_timestamp(52)
        
        logger.info("→ Sending Logout...")
        return self.send_message(msg)
        
    def send_heartbeat(self):
        """Send Heartbeat message"""
        msg = simplefix.FixMessage()
        msg.append_string('8=FIX.4.4')
        msg.append_pair(35, '0')  # MsgType = Heartbeat
        msg.append_pair(49, self.sender_comp_id)
        msg.append_pair(56, self.target_comp_id)
        msg.append_pair(34, self.sequence_num)
        msg.append_utc_timestamp(52)
        
        return self.send_message(msg)
        
    def send_new_order(self, symbol, side, price, quantity):
        """Send NewOrderSingle message"""
        try:
            self.order_count += 1
            cl_ord_id = f"ORD{int(time.time())}{self.order_count:04d}"
            
            # LAB 9: Record send timestamp (nanoseconds for precision)
            send_time_ns = time.time_ns()
            self.order_timestamps[cl_ord_id] = send_time_ns
            
            msg = simplefix.FixMessage()
            msg.append_string('8=FIX.4.4')
            msg.append_pair(35, 'D')  # MsgType = NewOrderSingle
            msg.append_pair(49, self.sender_comp_id)
            msg.append_pair(56, self.target_comp_id)
            msg.append_pair(34, self.sequence_num)
            msg.append_utc_timestamp(52)
            
            # Order fields
            msg.append_pair(11, cl_ord_id)  # ClOrdID
            msg.append_pair(55, symbol)  # Symbol
            msg.append_pair(54, '1' if side == 'BUY' else '2')  # Side
            msg.append_pair(40, '2')  # OrdType = Limit
            msg.append_pair(44, f"{price:.2f}")  # Price
            msg.append_pair(38, quantity)  # OrderQty
            msg.append_utc_timestamp(60)  # TransactTime
            msg.append_pair(21, '1')  # HandlInst = Automated
            
            if self.send_message(msg):
                side_str = "BUY " if side == "BUY" else "SELL"
                logger.info(f"→ Sent: {cl_ord_id} | {symbol:6s} | {side_str} | "
                          f"{quantity:4d} @ ${price:7.2f}")
                return True
            return False
            
        except Exception as e:
            logger.error(f"Error creating order: {e}")
            return False
    
    def send_orders_from_queue(self, delay=0.001):
        """Send orders from queue as fast as possible (multithreaded sender)"""
        while self.connected and self.session_active:
            try:
                # Get order from queue with timeout
                order_data = self.order_queue.get(timeout=1)
                if order_data is None:  # Sentinel to stop
                    break
                
                symbol, side, price, quantity = order_data
                self.order_count += 1
                cl_ord_id = f"ORD{int(time.time())}{self.order_count:04d}"
                
                # Record send timestamp
                send_time_ns = time.time_ns()
                self.order_timestamps[cl_ord_id] = send_time_ns
                
                msg = simplefix.FixMessage()
                msg.append_string('8=FIX.4.4')
                msg.append_pair(35, 'D')  # MsgType = NewOrderSingle
                msg.append_pair(49, self.sender_comp_id)
                msg.append_pair(56, self.target_comp_id)
                msg.append_pair(34, self.sequence_num)
                msg.append_utc_timestamp(52)
                
                # Order fields
                msg.append_pair(11, cl_ord_id)  # ClOrdID
                msg.append_pair(55, symbol)  # Symbol
                msg.append_pair(54, '1' if side == 'BUY' else '2')  # Side
                msg.append_pair(40, '2')  # OrdType = Limit
                msg.append_pair(44, f"{price:.2f}")  # Price
                msg.append_pair(38, quantity)  # OrderQty
                msg.append_utc_timestamp(60)  # TransactTime
                msg.append_pair(21, '1')  # HandlInst = Automated
                
                with self.send_lock:  # Protect socket write
                    if self.send_message(msg):
                        side_str = "BUY " if side == "BUY" else "SELL"
                        logger.info(f"→ Sent: {cl_ord_id} | {symbol:6s} | {side_str} | "
                                  f"{quantity:4d} @ ${price:7.2f}")
                
                # Minimal delay for throughput control
                if delay > 0:
                    time.sleep(delay)
                    
            except Exception as e:
                if self.connected:
                    logger.debug(f"Queue sender: {e}")
                break
            
    def receive_messages(self):
        """Receive and process incoming messages"""
        buffer = b''
        
        while self.connected:  # Changed: run while socket is connected
            try:
                # Check for heartbeat timing
                if self.session_active and time.time() - self.last_heartbeat > self.heartbeat_interval:
                    self.send_heartbeat()
                    self.last_heartbeat = time.time()
                
                # Receive data
                self.sock.settimeout(1.0)
                data = self.sock.recv(4096)
                if not data:
                    break
                    
                buffer += data
                logger.debug(f"[RX] Received {len(data)} bytes")
                
                # Process complete messages using parser
                self.parser.append_buffer(data)
                msg = self.parser.get_message()
                
                while msg:
                    self.handle_message(msg)
                    msg = self.parser.get_message()
                    
            except socket.timeout:
                continue
            except Exception as e:
                if self.connected:
                    logger.error(f"Error receiving: {e}")
                break
                
    def print_performance_summary(self, delay):
        """Print performance data in Python format for performance_data.py"""
        if not self.latencies:
            logger.error("No execution reports received - cannot calculate performance data")
            return
        
        latencies = sorted(self.latencies)
        avg_us = statistics.mean(latencies)
        min_us = min(latencies)
        max_us = max(latencies)
        
        # Calculate throughput
        # throughput = 1 / delay (in orders per second)
        throughput = 1.0 / delay if delay > 0 else 0
        
        logger.info("\n" + "="*70)
        logger.info("LAB 9 PERFORMANCE DATA - Copy below into performance_data.py")
        logger.info("="*70)
        logger.info(f"""
{{
    "throughput": {int(throughput)},           # orders/sec
    "latency_avg": {avg_us:.2f},       # microseconds
    "latency_min": {min_us:.2f},
    "latency_max": {max_us:.2f},
    "orders_tested": {self.exec_count}
}},
""")
        logger.info("="*70 + "\n")
        
        logger.info(f"Summary:")
        logger.info(f"  Total Orders Sent: {self.order_count}")
        logger.info(f"  Execution Reports Received: {self.exec_count}")
        logger.info(f"  Throughput: {throughput:.1f} orders/sec")
        logger.info(f"  Average Latency: {avg_us:.2f} µs")
        logger.info(f"  Min Latency: {min_us:.2f} µs")
        logger.info(f"  Max Latency: {max_us:.2f} µs")
        logger.info(f"  Median Latency: {statistics.median(latencies):.2f} µs")
    
    def handle_message(self, msg):
        """Handle incoming FIX message"""
        try:
            msg_type = msg.get(35)
            
            if msg_type == b'A':  # Logon
                logger.info("← Logon accepted")
                self.session_active = True
                self.running.set()
            elif msg_type == b'0':  # Heartbeat
                pass  # Silent heartbeat
            elif msg_type == b'1':  # Test Request
                # Respond with heartbeat
                self.send_heartbeat()
            elif msg_type == b'8':  # Execution Report
                self.handle_execution_report(msg)
            elif msg_type == b'5':  # Logout
                logger.info("← Logout received")
                self.session_active = False
                self.running.clear()
                
        except Exception as e:
            logger.error(f"Error handling message: {e}")
            
    def handle_execution_report(self, msg):
        """Process execution report"""
        try:
            # Safely extract fields
            cl_ord_id_val = msg.get(11)
            cl_ord_id = cl_ord_id_val.decode('utf-8') if isinstance(cl_ord_id_val, bytes) else str(cl_ord_id_val) if cl_ord_id_val else 'N/A'
            
            symbol_val = msg.get(55)
            symbol = symbol_val.decode('utf-8') if isinstance(symbol_val, bytes) else str(symbol_val) if symbol_val else 'N/A'
            
            side_val = msg.get(54)
            ord_status_val = msg.get(39)
            
            if not side_val or not ord_status_val:
                return  # Skip incomplete execution reports
            
            side = side_val.decode('utf-8') if isinstance(side_val, bytes) else str(side_val)
            ord_status = ord_status_val.decode('utf-8') if isinstance(ord_status_val, bytes) else str(ord_status_val)
            
            # Map codes to strings
            side_str = "BUY " if side == '1' else "SELL"
            
            status_map = {
                '0': 'NEW',
                '1': 'PARTIALLY_FILLED',
                '2': 'FILLED',
                '8': 'REJECTED',
                '4': 'CANCELED'
            }
            status_str = status_map.get(ord_status, 'UNKNOWN')
            
            # LAB 9: Calculate latency
            if cl_ord_id in self.order_timestamps:
                recv_time_ns = time.time_ns()
                send_time_ns = self.order_timestamps[cl_ord_id]
                latency_ns = recv_time_ns - send_time_ns
                latency_us = latency_ns / 1000.0  # Convert nanoseconds to microseconds
                self.latencies.append(latency_us)
                self.exec_count += 1
                
                logger.info(f"← Exec Report: {cl_ord_id} | {symbol:6s} | {side_str} | {status_str} | Latency: {latency_us:.2f} µs")
            else:
                logger.info(f"← Exec Report: {cl_ord_id} | {symbol:6s} | {side_str} | {status_str}")
            
        except Exception as e:
            logger.debug(f"Note: Execution report parsing (non-critical): {e}")


class OrderGenerator:
    """Generates random order parameters for testing"""
    
    # LAB 9: Expanded symbol list with 179 Indian & International stocks
    SYMBOLS = [
        # Original 6
        "AAPL", "GOOGL", "MSFT", "AMZN", "TSLA", "IBM",
        # Major Indian Banks
        "HDFC", "ICICIBANK", "INDUSINDBK", "AXISBANK", "KOTAK", "HDFC", "INDIGO",
        # IT Companies
        "TCS", "INFY", "WIPRO", "TECHM", "HCL", "LTTS", "MINDTREE", "MPHASIS",
        # Auto Companies
        "MARUTI", "TATAMOTORS", "HEROMOTOCO", "ASHOKLEY", "BAJAJ-AUTO", "M&M",
        # FMCG
        "ITC", "BRITANNIA", "NESTLEIND", "HINDUNILVR", "COLPAL", "MARICO",
        # Pharma
        "DRREDDY", "SUNPHARMA", "CIPLA", "LUPIN", "AURAPHA", "GLENMARK",
        # Metals & Mining
        "TATASTEEL", "HINDALCO", "HINDZINC", "VEDL", "COALINDIA", "NMDC",
        # Power
        "NTPC", "POWERGRID", "RELPOWER", "TORNTPOWER", "ADANIPOWER",
        # Finance
        "BAJFINANCE", "BAJAJFINSV", "SBICARD", "HDFCAMC", "MOTILALOFS",
        # Real Estate
        "DLF", "PRESTIGE", "SOBHA", "BRIGADE", "HIRANIDL",
        # Telecom
        "BHARTIARTL", "JIOFINANCE", "VODAIDEA", "BSNL",
        # Chemicals
        "TATACHEMF", "DEEPAKFERT", "AKZOINDIA", "GSKCONS",
        # Additional M-Series from your list
        "MOSERBAER", "MAZDA", "MEDICAPSBE", "MUTHOOTCAP", "MATRIXLABS", "MTEDUCARE",
        "MTNL", "MADHAV", "MAANALU", "MAGNUM", "MANAPPURAM", "MOLTKPAK", "MIRCELECTR",
        "MPHASIS", "MANINFRA", "MALCO", "MAXINDIA", "MARUTI", "MOTHERSUMI",
        # W-Series
        "WHIRLPOOL", "WIPRO", "WELSPUNIND", "WONDERLA", "WOCKPHARMA",
        # H-Series  
        "HONAUT", "HINDPETRO", "HAVELLS", "HEXAWARE", "HCLTECH",
        # T-Series
        "TATACOMM", "TATAGLOBAL", "TVSMOTOR", "TATAELXSI", "TRIVENI", "TRIDENT",
        # O-Series
        "OIL", "ONGC", "ORIENTBANK", "ORIENTHOT", "OFSS",
        # B-Series
        "BOSCHLTD", "BHARTIARTL", "BPCL", "BALKRISIND", "BRITANNIA",
        # Additional high-cap
        "RIL", "SBI", "BHARATIIND", "HDFCBANK", "INFY", "TCS", "WIPRO",
        # Mid-cap selection
        "PAGEIND", "ALEMBIC", "BAYER", "COLPAL", "EMAMI", "GRINDWELL",
        "KSCL", "LAXMIMACH", "LUXIND", "NEWHOTEL", "PAGEIND", "PNBGILTS",
        # More symbols for variety
        "VAIBHAVGBL", "VEDL", "VOLTAS", "VRLLOG", "VSTIND", "VASWANI",
        "UFLEX", "ULTRACEMCO", "UNIONBANK", "UNIPHOS", "UNITECH", "UNITEDBN",
        "UMARKTIND", "USHARBI", "UTIMASTER", "VANGUARD"
    ]
    
    PRICE_RANGES = {
        # Original 6
        "AAPL": (150, 200), "GOOGL": (140, 160), "MSFT": (300, 400),
        "AMZN": (3000, 3500), "TSLA": (200, 300), "IBM": (140, 160),
        # Banks
        "HDFC": (1500, 1800), "ICICIBANK": (900, 1100), "INDUSINDBK": (900, 1100),
        "AXISBANK": (900, 1100), "KOTAK": (2400, 2800),
        # IT
        "TCS": (3200, 3800), "INFY": (1400, 1700), "WIPRO": (400, 500),
        "TECHM": (1100, 1400), "HCL": (1200, 1500),
        # Auto
        "MARUTI": (8000, 9500), "TATAMOTORS": (450, 550), "HEROMOTOCO": (2800, 3500),
        "ASHOKLEY": (140, 180), "BAJAJ-AUTO": (3800, 4500),
        # FMCG
        "ITC": (410, 470), "BRITANNIA": (4200, 5000), "NESTLEIND": (21000, 24000),
        "HINDUNILVR": (2400, 2800), "COLPAL": (2200, 2600),
        # Pharma
        "DRREDDY": (4000, 4800), "CIPLA": (1100, 1300), "LUPIN": (800, 1000),
        # Metals
        "TATASTEEL": (1100, 1400), "HINDALCO": (520, 650), "VEDL": (200, 280),
        # Power
        "NTPC": (200, 280), "POWERGRID": (240, 320),
        # Finance
        "BAJFINANCE": (7200, 8500), "BAJAJFINSV": (1600, 2000),
        # Default for unknown symbols
        "MOSERBAER": (80, 150), "MAZDA": (100, 200), "MEDICAPSBE": (200, 300),
        "MUTHOOTCAP": (250, 350), "MATRIXLABS": (300, 450), "MTEDUCARE": (120, 180),
        "MTNL": (15, 40), "MADHAV": (200, 350), "MAANALU": (150, 250),
        "MAGNUM": (100, 200), "MANAPPURAM": (150, 250), "MOLTKPAK": (80, 150),
        "MIRCELECTR": (180, 280), "MPHASIS": (2200, 2800), "MANINFRA": (80, 150),
        "MALCO": (50, 100), "MAXINDIA": (50, 120), "MOTHERSUMI": (110, 180),
        "WHIRLPOOL": (1400, 1800), "WELSPUNIND": (450, 600), "WONDERLA": (1800, 2400),
        "WOCKPHARMA": (500, 750), "HONAUT": (33000, 42000), "HINDPETRO": (320, 420),
        "HAVELLS": (1400, 1800), "HEXAWARE": (1800, 2400), "HCLTECH": (1200, 1600),
        "TATACOMM": (1400, 1900), "TATAGLOBAL": (330, 430), "TVSMOTOR": (450, 600),
        "TATAELXSI": (2200, 2900), "TRIVENI": (200, 320), "TRIDENT": (70, 140),
        "OIL": (280, 380), "ONGC": (140, 200), "ORIENTBANK": (45, 90),
        "ORIENTHOT": (60, 120), "OFSS": (3600, 4800), "BOSCHLTD": (24000, 32000),
        "BHARTIARTL": (800, 1100), "BPCL": (350, 480), "BALKRISIND": (1200, 1600),
        "BRITANNIA": (3800, 4600), "RIL": (2400, 2900), "SBI": (600, 800),
        "PAGEIND": (35000, 45000), "ALEMBIC": (900, 1300), "BAYER": (5000, 7000),
        "EMAMI": (450, 600), "GRINDWELL": (3000, 4000), "KSCL": (2200, 3000),
        "LAXMIMACH": (4000, 5500), "LUXIND": (8000, 11000), "PNBGILTS": (95, 110),
        "VAIBHAVGBL": (200, 350), "VOLTAS": (1200, 1600), "VRLLOG": (1100, 1500),
        "VSTIND": (3600, 4800), "UFLEX": (450, 650), "ULTRACEMCO": (6800, 8200),
        "UNIONBANK": (70, 110), "UNIPHOS": (400, 600), "UNITECH": (40, 80),
    }
    
    @staticmethod
    def generate_order():
        """Generate random order parameters"""
        symbol = random.choice(OrderGenerator.SYMBOLS)
        side = random.choice(["BUY", "SELL"])
        
        price_range = OrderGenerator.PRICE_RANGES.get(symbol, (100, 200))
        price = round(random.uniform(price_range[0], price_range[1]), 2)
        
        if random.random() < 0.99:
            quantity = random.randint(10, 200)
        else:
            quantity = random.randint(500, 2000)
            
        return symbol, side, price, quantity


def run_test_session(num_orders=50, delay=0.001, burst_mode=False):
    """Run a test session sending multiple orders using multithreading"""
    
    client = SimpleFIXClient()
    
    try:
        # Connect
        if not client.connect():
            logger.error("Failed to connect")
            return
            
        # Send logon
        if not client.send_logon():
            logger.error("Failed to send logon")
            return
            
        # Start receiver thread (NON-DAEMON so it continues receiving messages)
        receiver = Thread(target=client.receive_messages, daemon=False)
        receiver.start()
        
        # Wait for logon confirmation
        logger.info("Waiting for logon confirmation...")
        timeout = 10
        elapsed = 0
        while not client.running.is_set() and elapsed < timeout:
            time.sleep(0.5)
            elapsed += 0.5
            
        if not client.running.is_set():
            logger.error("Logon timeout")
            client.disconnect()
            return
            
        logger.info(f"\n{'='*70}")
        logger.info(f"✓ Session established - Starting to send {num_orders} orders...")
        logger.info(f"✓ Using MULTITHREADING for faster order preparation")
        logger.info(f"{'='*70}\n")
        
        # Start sender thread (pulls from queue and sends to FIX)
        sender = Thread(target=client.send_orders_from_queue, args=(delay,), daemon=False)
        sender.start()
        
        # Use ThreadPoolExecutor to prepare orders in parallel
        # This parallelizes the random order generation while sender sends concurrently
        with ThreadPoolExecutor(max_workers=4) as executor:
            futures = []
            
            for i in range(num_orders):
                if not client.session_active:
                    break
                
                # Submit order generation to thread pool
                future = executor.submit(OrderGenerator.generate_order)
                futures.append(future)
            
            # Collect results and queue them for sending
            for i, future in enumerate(futures):
                try:
                    symbol, side, price, quantity = future.result(timeout=5)
                    client.order_queue.put((symbol, side, price, quantity))
                except Exception as e:
                    logger.error(f"Error preparing order {i}: {e}")
        
        logger.info(f"\n{'='*70}")
        logger.info(f"✓ All {num_orders} orders queued - waiting for transmission...")
        logger.info(f"{'='*70}\n")
        
        # Wait for queue to be emptied (all orders sent)
        client.order_queue.join()
        time.sleep(1)  # Brief pause
        
        # Signal sender thread to stop
        client.order_queue.put(None)
        sender.join(timeout=5)
        
        logger.info(f"\n{'='*70}")
        logger.info(f"✓ All orders sent! Sent {client.order_count} orders")
        logger.info(f"{'='*70}\n")
        
        # Wait for final responses
        total_send_time = (client.order_count - 1) * delay if client.order_count > 1 else 0
        wait_time = max(30, int(total_send_time) + 15)
        
        logger.info(f"Waiting for execution reports (timeout: {wait_time}s)...")
        logger.info(f"(Waiting up to {wait_time} seconds for backend to send ExecutionReports back)\n")
        time.sleep(wait_time)
        
        # Print performance data in Python format
        client.print_performance_summary(delay)
        
    except KeyboardInterrupt:
        logger.info("\n\nTest interrupted by user")
    except Exception as e:
        logger.error(f"Error during test: {e}")
        import traceback
        traceback.print_exc()
    finally:
        client.disconnect()
        time.sleep(1)


def get_user_input():
    """Interactively get configuration from user"""
    print("\n" + "="*70)
    print("FIX ORDER SENDER - Configuration (Multithreaded)")
    print("="*70 + "\n")
    
    # Get number of orders
    while True:
        try:
            num_orders_input = input("📊 How many orders to send? (default 50): ").strip()
            num_orders = int(num_orders_input) if num_orders_input else 50
            if num_orders <= 0:
                print("   ❌ Please enter a positive number")
                continue
            break
        except ValueError:
            print("   ❌ Invalid input. Please enter a number")
    
    # Get delay between orders
    while True:
        try:
            delay_input = input("⏱️  Delay between orders in seconds? (default 0.001 = 1000 orders/sec): ").strip()
            delay = float(delay_input) if delay_input else 0.001
            if delay < 0:
                print("   ❌ Please enter a non-negative number")
                continue
            break
        except ValueError:
            print("   ❌ Invalid input. Please enter a number")
    
    # Show confirmation
    print("\n" + "-"*70)
    print("✓ Configuration Summary:")
    print(f"  📊 Orders: {num_orders}")
    print(f"  ⏱️  Delay: {delay}s per order")
    print(f"  🚀 Mode: Multithreaded (4 worker threads)")
    throughput = 1.0 / delay if delay > 0 else 0
    print(f"  📈 Target Throughput: {throughput:.0f} orders/sec")
    if num_orders > 1000:
        estimated_time = (num_orders - 1) * delay
        print(f"  ⏳ Estimated time: {estimated_time:.1f} seconds (~{estimated_time/60:.1f} minutes)")
    print("-"*70 + "\n")
    
    # Confirm before proceeding
    confirm = input("Proceed? (y/n, default y): ").strip().lower()
    if confirm in ['n', 'no']:
        print("Cancelled.")
        return None
    
    return num_orders, delay


def main():
    """Main entry point"""
    print("""
╔════════════════════════════════════════════════════════════════╗
║       Simple FIX Order Sender - Automated Test Tool           ║
║                    (MULTITHREADED VERSION)                    ║
║                                                                ║
║   Sends random NewOrderSingle messages to order-service       ║
║       Using 4 parallel threads for order preparation          ║
║              (Pure Python - No compilation needed)            ║
╚════════════════════════════════════════════════════════════════╝
    """)
    
    # Get interactive input from user
    config = get_user_input()
    if config is None:
        sys.exit(0)
    
    num_orders, delay = config
        
    logger.info(f"Starting multithreaded test session...")
    logger.info(f"")
    
    try:
        run_test_session(num_orders, delay)
    except KeyboardInterrupt:
        logger.info("\n\nTest interrupted by user")
        sys.exit(0)
    except Exception as e:
        logger.error(f"Test failed: {e}")
        import traceback
        traceback.print_exc()
        sys.exit(1)


if __name__ == "__main__":
    main()