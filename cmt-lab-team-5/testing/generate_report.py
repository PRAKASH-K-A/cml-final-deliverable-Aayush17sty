#!/usr/bin/env python3
"""
Lab 9: Performance Report Generator
Generates latency vs. throughput graph from test results
"""

import matplotlib.pyplot as plt
import numpy as np
import sys

try:
    from performance_data import test_results
except ImportError:
    print("❌ Error: performance_data.py not found!")
    print("   Please create performance_data.py with test results first.")
    sys.exit(1)

def generate_latency_graph():
    """Generate and save latency vs throughput graph"""
    
    # Extract data from test results
    throughputs = [r["throughput"] for r in test_results]
    latencies_avg = [r["latency_avg"] for r in test_results]
    latencies_min = [r["latency_min"] for r in test_results]
    latencies_max = [r["latency_max"] for r in test_results]
    
    # Create figure with proper size for academic report
    fig, ax = plt.subplots(figsize=(13, 8))
    
    # Main line plot for average latency
    ax.plot(throughputs, latencies_avg, 'o-', 
            linewidth=2.8, 
            markersize=12, 
            label='Average Latency', 
            color='#2E86AB',
            markerfacecolor='#A23B72',
            markeredgewidth=2.5,
            markeredgecolor='#2E86AB',
            zorder=3)
    
    # Fill between min and max range
    ax.fill_between(throughputs, latencies_min, latencies_max, 
                    alpha=0.25, 
                    color='#F18F01',
                    label='Min-Max Latency Range',
                    zorder=1)
    
    # Formatting
    ax.set_xlabel('Throughput (orders/sec)', fontsize=13, fontweight='bold')
    ax.set_ylabel('Latency (microseconds - µs)', fontsize=13, fontweight='bold')
    ax.set_title('Lab 9: Tick-to-Trade Latency vs. Throughput Analysis\nOrder Management System Performance', 
                 fontsize=15, fontweight='bold', pad=20)
    
    # Grid
    ax.grid(True, alpha=0.4, linestyle='--', linewidth=0.7)
    ax.set_axisbelow(True)
    
    # Legend
    ax.legend(loc='upper left', fontsize=12, framealpha=0.95, edgecolor='black', fancybox=True)
    
    # Format axes
    ax.set_xticks(throughputs)
    ax.set_xticklabels([f'{int(t)}' for t in throughputs], fontsize=11, fontweight='bold')
    ax.yaxis.set_major_formatter(plt.FuncFormatter(lambda x, p: f'{x:.0f}'))
    
    # Add value labels on average latency points
    for x, y_avg, y_min, y_max in zip(throughputs, latencies_avg, latencies_min, latencies_max):
        # Average latency label (above the point)
        ax.text(x, y_avg + 15, f'{y_avg:.2f} µs', 
                ha='center', fontsize=11, fontweight='bold', color='#2E86AB')
        
        # Show range
        ax.text(x, y_min - 25, f'({y_min:.0f}-{y_max:.0f})', 
                ha='center', fontsize=9, color='#666', style='italic')
    
    # Add statistics box
    stats_text = (
        f'Test Summary:\n'
        f'• Test 1:  100 orders/sec → {latencies_avg[0]:.2f} µs avg\n'
        f'• Test 2:  500 orders/sec → {latencies_avg[1]:.2f} µs avg\n'
        f'• Test 3: 1000 orders/sec → {latencies_avg[2]:.2f} µs avg\n'
        f'• Change: +{((latencies_avg[2]/latencies_avg[0] - 1) * 100):.1f}% from 100→1000 orders/sec'
    )
    
    ax.text(0.98, 0.05, stats_text, 
            transform=ax.transAxes,
            fontsize=10,
            verticalalignment='bottom',
            horizontalalignment='right',
            bbox=dict(boxstyle='round', facecolor='wheat', alpha=0.5),
            family='monospace')
    
    # Make spine thicker
    for spine in ax.spines.values():
        spine.set_edgecolor('black')
        spine.set_linewidth(1.5)
    
    plt.tight_layout()
    
    # Save as high-quality image
    output_file = 'latency_vs_throughput.png'
    plt.savefig(output_file, dpi=300, bbox_inches='tight', facecolor='white')
    print(f"✅ Graph saved: {output_file}")
    print(f"   Resolution: 300 DPI (high-quality for printing)")
    print(f"   Format: PNG with transparent background")
    
    # Also save as PDF for better report integration
    output_pdf = 'latency_vs_throughput.pdf'
    plt.savefig(output_pdf, format='pdf', bbox_inches='tight')
    print(f"✅ Also saved as: {output_pdf}")
    
    # Display
    plt.show()

def print_data_summary():
    """Print summary of collected data"""
    print("\n" + "="*60)
    print("LAB 9 PERFORMANCE TEST DATA SUMMARY")
    print("="*60)
    
    for result in test_results:
        print(f"\nTest: {result['throughput']} orders/sec")
        print(f"  • Orders processed: {result['orders_tested']}")
        print(f"  • Average Latency:  {result['latency_avg']:.2f} µs")
        print(f"  • Min Latency:      {result['latency_min']:.2f} µs")
        print(f"  • Max Latency:      {result['latency_max']:.2f} µs")
        print(f"  • Range:            {result['latency_max'] - result['latency_min']:.2f} µs")
    
    # Calculate trend
    print("\n" + "-"*60)
    latency_change = ((test_results[2]['latency_avg'] / test_results[0]['latency_avg']) - 1) * 100
    print(f"Latency Increase (100→1000 orders/sec): {latency_change:+.1f}%")
    print("-"*60 + "\n")

if __name__ == "__main__":
    print("\n🔄 Generating Lab 9 Performance Report...\n")
    
    # Print data summary
    print_data_summary()
    
    # Generate graph
    try:
        generate_latency_graph()
        print("\n✅ Report generation complete!")
        print("\n📊 Next steps:")
        print("   1. Review the generated PNG file")
        print("   2. Include in your Lab 9 Assessment Report")
        print("   3. Add JVisualVM screenshots for CPU and Memory profiling")
    except Exception as e:
        print(f"❌ Error generating graph: {e}")
        sys.exit(1)
