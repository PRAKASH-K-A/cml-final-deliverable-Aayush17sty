# FIX Order Testing Tool

Automated test script for sending random FIX NewOrderSingle messages to the order-service.

## 🚀 Quick Setup

1. **Install Python dependencies:**
   ```powershell
   pip install -r requirements.txt
   ```
   This installs `simplefix` - a pure Python FIX library (no C++ compilation needed!)

2. **Ensure the order-service is running:**
   - The order-service must be running and listening on port 9876
   - The UI should be running to see the orders appear in real-time

## Usage

### Basic Usage
Send 50 random orders with 0.5s delay between each:
```powershell
python simple_fix_sender.py
```

### Custom Number of Orders
Send 100 orders:
```powershell
python simple_fix_sender.py 100
```

### Custom Delay
Send 200 orders with 0.2s delay:
```powershell
python simple_fix_sender.py 200 0.2
```

### Burst Mode
Send 100 orders in bursts of 10 (fast batches with pauses):
```powershell
python simple_fix_sender.py 100 0.1 burst
```

## What It Does

The script:
- Connects to the FIX server (order-service) on localhost:9876
- Generates random orders with:
  - **20 different symbols** (AAPL, GOOGL, MSFT, TSLA, etc.)
  - **Realistic price ranges** for each symbol
  - **Random quantities** (10-500 shares typically, occasionally 500-2000)
  - **Random BUY/SELL sides**
- Sends NewOrderSingle (35=D) messages
- Logs all sent orders and received execution reports
- Colored console output for easy monitoring

## Order Generation

The script generates diverse test data:
- **Symbols**: 20 popular stocks with appropriate price ranges
- **Prices**: Randomized within realistic ranges for each symbol
- **Quantities**: Weighted random (80% small orders, 20% large)
- **Sides**: 50/50 split between BUY and SELL
- **Order IDs**: Unique timestamps-based IDs

## Logs and Store

The script creates:
- `store/` - QuickFIX session state files
- `log/` - QuickFIX message logs

These directories are created automatically.

## Examples

**Light testing (10 orders slowly):**
```powershell
python simple_fix_sender.py 10 1.0
```

**Moderate load (100 orders):**
```powershell
python simple_fix_sender.py 100 0.5
```

**Heavy load (500 orders fast):**
```powershell
python simple_fix_sender.py 500 0.1
```

**Stress test (1000 orders in burst mode):**
```powershell
python simple_fix_sender.py 1000 0.05 burst
```

## Testing the UI Features

Use this script to test all the new UI features:

1. **Filters**: Watch different symbols, sides, and statuses flow through
2. **Statistics**: See real-time metrics update (total value, buy/sell counts, etc.)
3. **Search**: Try searching for specific symbols while orders arrive
4. **Sorting**: Sort by different columns while the data flows
5. **Notifications**: Rejected orders will trigger browser notifications
6. **Performance**: Test how the UI handles high-volume order flow

## Troubleshooting

**"Failed to connect to FIX server"**  
- Ensure order-service is running
- Check that port 9876 is accessible
- Verify no firewall is blocking the connection

**"ModuleNotFoundError: No module named 'simplefix'"**  
- Run `pip install -r requirements.txt`
- Uses pure Python library, no compilation needed

**Orders not appearing in UI**  
- Check that the UI WebSocket is connected (localhost:8080)
- Verify the order-service is broadcasting to WebSocket
- Check browser console for errors

## Notes

- The script waits for successful logon before sending orders
- Each order has a unique ClOrdID based on timestamp
- Execution reports are logged as they arrive
- Press Ctrl+C to stop the script at any time
