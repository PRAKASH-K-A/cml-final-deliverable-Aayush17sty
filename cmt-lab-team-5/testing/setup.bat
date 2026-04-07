@echo off
echo ================================================
echo FIX Order Sender - Setup Script
echo ================================================
echo.

echo [1/3] Installing Python dependencies...
python -m pip install --upgrade pip
pip install -r requirements.txt

if %ERRORLEVEL% NEQ 0 (
    echo.
    echo ERROR: Failed to install dependencies
    echo Make sure Python is installed and in your PATH
    pause
    exit /b 1
)

echo.
echo [2/3] Creating necessary directories...
if not exist "store" mkdir store
if not exist "log" mkdir log

echo.
echo [3/3] Setup complete!
echo.
echo ================================================
echo           Ready to Send Orders!
echo ================================================
echo.
echo Usage examples:
echo   python simple_fix_sender.py                    (50 orders, 0.5s delay)
echo   python simple_fix_sender.py 100                (100 orders, 0.5s delay)
echo   python simple_fix_sender.py 200 0.2            (200 orders, 0.2s delay)
echo   python simple_fix_sender.py 100 0.1 burst      (100 orders in burst mode)
echo.
echo IMPORTANT: Make sure your order-service is running first!
echo.
pause
