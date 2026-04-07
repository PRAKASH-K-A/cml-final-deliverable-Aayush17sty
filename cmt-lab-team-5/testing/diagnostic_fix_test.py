#!/usr/bin/env python3
"""
Diagnostic FIX Test - Shows exactly what messages are sent and received
"""

import simplefix
import socket
import time
import logging

logging.basicConfig(
    level=logging.DEBUG,
    format='%(asctime)s - [%(levelname)s] %(message)s'
)
logger = logging.getLogger(__name__)

def test_connection():
    """Test basic FIX connection and message exchange"""
    
    print("\n" + "="*70)
    print("DIAGNOSTIC: FIX Connection Test")
    print("="*70 + "\n")
    
    sock = None
    try:
        # Step 1: Connect
        logger.info("Step 1: Connecting to localhost:9876...")
        sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        sock.settimeout(5)
        sock.connect(('127.0.0.1', 9876))
        logger.info("✓ Connected")
        
        # Step 2: Send Logon
        logger.info("\nStep 2: Sending FIX Logon message...")
        logon = simplefix.FixMessage()
        logon.append_string('8=FIX.4.4')
        logon.append_pair(35, 'A')  # Logon
        logon.append_pair(49, 'MINIFIX_CLIENT')
        logon.append_pair(56, 'EXEC_SERVER')
        logon.append_pair(34, 1)
        logon.append_utc_timestamp(52)
        logon.append_pair(98, 0)  # EncryptMethod
        logon.append_pair(108, 30)  # HeartBtInt
        logon.append_pair(141, 'Y')  # ResetSeqNumFlag
        
        msg_bytes = logon.encode()
        logger.info(f"   Sending {len(msg_bytes)} bytes...")
        sock.send(msg_bytes)
        logger.info(f"   Raw message:\n   {msg_bytes[:200]}...")
        
        # Step 3: Receive Logon Response
        logger.info("\nStep 3: Waiting for Logon response...")
        sock.settimeout(3)
        parser = simplefix.FixParser()
        data = sock.recv(4096)
        
        if data:
            logger.info(f"✓ Received {len(data)} bytes")
            logger.info(f"  Raw data: {data[:200]}...")
            parser.append_buffer(data)
            msg = parser.get_message()
            if msg:
                msg_type = msg.get(35)
                logger.info(f"  Message Type: {msg_type}")
                if msg_type == b'A':
                    logger.info("  ✓ Logon accepted!")
                else:
                    logger.warning(f"  Unexpected message type: {msg_type}")
        else:
            logger.error("✗ No response received")
            
        # Step 4: Send one test order
        logger.info("\nStep 4: Sending test NewOrderSingle...")
        sock.settimeout(5)
        order = simplefix.FixMessage()
        order.append_string('8=FIX.4.4')
        order.append_pair(35, 'D')  # NewOrderSingle
        order.append_pair(49, 'MINIFIX_CLIENT')
        order.append_pair(56, 'EXEC_SERVER')
        order.append_pair(34, 2)
        order.append_utc_timestamp(52)
        order.append_pair(11, 'TEST_ORDER_001')  # ClOrdID
        order.append_pair(55, 'AAPL')  # Symbol
        order.append_pair(54, '1')  # Side (Buy)
        order.append_pair(40, '2')  # OrdType (Limit)
        order.append_pair(44, '150.00')  # Price
        order.append_pair(38, 100)  # Qty
        order.append_utc_timestamp(60)
        order.append_pair(21, '1')  # HandlInst
        
        order_bytes = order.encode()
        logger.info(f"   Sending {len(order_bytes)} bytes...")
        sock.send(order_bytes)
        logger.info(f"   ✓ Order sent")
        
        # Step 5: Wait for Execution Report
        logger.info("\nStep 5: Waiting for Execution Report (5 sec timeout)...")
        sock.settimeout(5)
        parser2 = simplefix.FixParser()
        
        responses_received = []
        try:
            while len(responses_received) < 2:  # Expect at least ACK and maybe trade report
                data = sock.recv(4096)
                if not data:
                    logger.warning("Connection closed")
                    break
                    
                logger.info(f"   Received {len(data)} bytes")
                logger.info(f"   Raw: {data[:150]}...")
                
                parser2.append_buffer(data)
                msg = parser2.get_message()
                
                while msg:
                    msg_type = msg.get(35)
                    logger.info(f"   → Message Type: {msg_type}")
                    
                    if msg_type == b'8':  # Execution Report
                        responses_received.append(msg)
                        ord_id = msg.get(11, b'?').decode('utf-8')
                        status = msg.get(39, b'?').decode('utf-8')
                        logger.info(f"     ✓ Execution Report - Order: {ord_id}, Status: {status}")
                    elif msg_type == b'0':  # Heartbeat
                        logger.info(f"     → Heartbeat")
                    else:
                        logger.info(f"     → Other ({msg_type})")
                    
                    msg = parser2.get_message()
        except socket.timeout:
            logger.warning("   ✗ Timeout waiting for response")
            
        if responses_received:
            logger.info(f"\n✓ SUCCESS: Received {len(responses_received)} execution report(s)")
        else:
            logger.error(f"\n✗ FAILED: No execution reports received")
            
    except Exception as e:
        logger.error(f"✗ Error: {e}", exc_info=True)
    finally:
        if sock:
            sock.close()
            logger.info("\nConnection closed")

if __name__ == "__main__":
    test_connection()
