#!/bin/bash
echo "================================================"
echo "FIX Order Sender - Setup Script"
echo "================================================"
echo ""

echo "[1/3] Installing Python dependencies..."
python3 -m pip install --upgrade pip
pip3 install -r requirements.txt

if [ $? -ne 0 ]; then
    echo ""
    echo "ERROR: Failed to install dependencies"
    echo "Make sure Python 3 is installed"
    exit 1
fi

echo ""
echo "[2/3] Creating necessary directories..."
mkdir -p store
mkdir -p log

echo ""
echo "[3/3] Setup complete!"
echo ""
echo "================================================"
echo "           Ready to Send Orders!"
echo "================================================"
echo ""
echo "Usage examples:"
echo "  python3 fix_order_sender.py                    (50 orders, 0.5s delay)"
echo "  python3 fix_order_sender.py 100                (100 orders, 0.5s delay)"
echo "  python3 fix_order_sender.py 200 0.2            (200 orders, 0.2s delay)"
echo "  python3 fix_order_sender.py 100 0.1 burst      (100 orders in burst mode)"
echo ""
echo "IMPORTANT: Make sure your order-service is running first!"
echo ""
