# Performance Data - Update with actual test results
# After running each test, copy the Average Latency values from console output

test_results = [
    {
        "throughput": 100,           # orders/sec
        "latency_avg": 3121.55,       # microseconds - REPLACE AFTER TEST 1
        "latency_min": 0.00,
        "latency_max": 152199.90,
        "orders_tested": 1000
    },
    {
        "throughput": 500,           # orders/sec
        "latency_avg": 287.34,       # microseconds - REPLACE AFTER TEST 2
        "latency_min": 201.45,
        "latency_max": 2134.56,
        "orders_tested": 1000
    },
    {
        "throughput": 1000,          # orders/sec
        "latency_avg": 342.12,       # microseconds - REPLACE AFTER TEST 3
        "latency_min": 215.78,
        "latency_max": 3456.89,
        "orders_tested": 3000
    }
]

# Example of what to look for in console output:
# ═══ PERFORMANCE REPORT ═══ Processed 1,000 orders | Avg: 245.67 µs | Min: 189.23 µs | Max: 1,245.89 µs
#                                                           ^^^^^^
#                                                      Copy this value
