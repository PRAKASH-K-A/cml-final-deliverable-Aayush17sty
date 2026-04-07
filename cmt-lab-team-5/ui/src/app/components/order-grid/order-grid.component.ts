import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { WebsocketService, Order, Trade } from '../../services/websocket.service';
import { Subscription } from 'rxjs';

interface ColumnConfig {
  key: string;
  label: string;
  visible: boolean;
}

interface Stats {
  totalOrders: number;
  totalValue: number;
  buyOrders: number;
  sellOrders: number;
  filledOrders: number;
  rejectedOrders: number;
  avgOrderSize: number;
  fillRate: number;
}

interface TradeStats {
  totalTrades: number;
  totalTradeValue: number;
  totalTradeQty: number;
  buyTrades: number;
  sellTrades: number;
  avgTradeSize: number;
  avgTradePrice: number;
}

type SortColumn = 'timestamp' | 'clOrdID' | 'symbol' | 'price' | 'quantity' | 'value' | 'ordStatus';
type TimeFilter = 'all' | '5min' | '1hour' | 'today';

@Component({
  selector: 'app-order-grid',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './order-grid.component.html',
  styleUrls: ['./order-grid.component.css']
})
export class OrderGridComponent implements OnInit, OnDestroy {
  orders: Order[] = [];
  filteredOrders: Order[] = [];
  trades: Trade[] = [];  // LAB 7: Trade history from matching engine
  connected = false;
  
  // Filtering & Search
  searchTerm = '';
  sideFilter: 'all' | 'buy' | 'sell' = 'all';
  statusFilter: 'all' | 'NEW' | 'FILLED' | 'PARTIALLY_FILLED' | 'REJECTED' = 'all';
  timeFilter: TimeFilter = 'all';
  
  // Sorting
  sortColumn: SortColumn = 'timestamp';
  sortDirection: 'asc' | 'desc' = 'desc';
  
  // Column Visibility
  columns: ColumnConfig[] = [
    { key: 'timestamp', label: 'Time', visible: true },
    { key: 'clOrdID', label: 'Order ID', visible: true },
    { key: 'symbol', label: 'Symbol', visible: true },
    { key: 'side', label: 'Side', visible: true },
    { key: 'quantity', label: 'Quantity', visible: true },
    { key: 'cumQty', label: 'Net Qty', visible: true },
    { key: 'price', label: 'Price', visible: true },
    { key: 'value', label: 'Value', visible: true },
    { key: 'ordStatus', label: 'Status', visible: true }
  ];
  
  // UI State
  showStats = true;
  showFilters = false;
  showColumnConfig = false;
  theme: 'dark' | 'light' = 'dark';
  autoScroll = true;
  soundEnabled = true;
  
  // Statistics
  stats: Stats = {
    totalOrders: 0,
    totalValue: 0,
    buyOrders: 0,
    sellOrders: 0,
    filledOrders: 0,
    rejectedOrders: 0,
    avgOrderSize: 0,
    fillRate: 0
  };
  
  // LAB 7: Trade Statistics from Matching Engine
  tradeStats: TradeStats = {
    totalTrades: 0,
    totalTradeValue: 0,
    totalTradeQty: 0,
    buyTrades: 0,
    sellTrades: 0,
    avgTradeSize: 0,
    avgTradePrice: 0
  };
  
  // Cumulative counters for high-frequency trading (not limited by display array)
  private cumulativeStats = {
    totalOrders: 0,
    totalValue: 0,
    buyOrders: 0,
    sellOrders: 0,
    filledOrders: 0,
    rejectedOrders: 0,
    totalQuantity: 0
  };
  
  private subscription?: Subscription;
  private connectionSubscription?: Subscription;
  private tradeSubscription?: Subscription;
  private audio?: HTMLAudioElement;

  constructor(private wsService: WebsocketService) {
    // Load preferences from localStorage
    this.loadPreferences();
  }

  ngOnInit(): void {
    // Subscribe to incoming orders
    this.subscription = this.wsService.messages$.subscribe((order) => {
      // Add timestamp if not present
      if (!order.timestamp) {
        order.timestamp = Date.now();
      }
      
      // Update cumulative stats (not limited by display array)
      this.cumulativeStats.totalOrders++;
      this.cumulativeStats.totalValue += order.price * order.quantity;
      this.cumulativeStats.totalQuantity += order.quantity;
      
      if (order.side === '1') {
        this.cumulativeStats.buyOrders++;
      } else if (order.side === '2') {
        this.cumulativeStats.sellOrders++;
      }
      
      if (order.ordStatus === 'FILLED') {
        this.cumulativeStats.filledOrders++;
      } else if (order.ordStatus === 'REJECTED') {
        this.cumulativeStats.rejectedOrders++;
      }
      
      // Add new order to the top of the list with animation class
      this.orders.unshift({ ...order, isNew: true });
      
      // Play sound for rejected orders
      if (order.ordStatus === 'REJECTED' && this.soundEnabled) {
        this.playAlert();
      }
      
      // Show browser notification for rejected orders
      if (order.ordStatus === 'REJECTED') {
        this.showNotification('Order Rejected', `Order ${order.clOrdID} for ${order.symbol} was rejected`);
      }
      
      // Remove animation class after animation completes
      setTimeout(() => {
        const orderIndex = this.orders.findIndex(o => o.clOrdID === order.clOrdID);
        if (orderIndex !== -1 && this.orders[orderIndex]) {
          this.orders[orderIndex].isNew = false;
        }
      }, 500);

      // Limit to 1000 orders to prevent memory issues
      if (this.orders.length > 1000) {
        this.orders = this.orders.slice(0, 1000);
      }
      
      // Update filtered orders and stats
      this.applyFilters();
      this.updateStats();
    });

    // Subscribe to connection status
    this.connectionSubscription = this.wsService.connected$.subscribe((status) => {
      this.connected = status;
    });
    
    // LAB 7: Subscribe to trade stream from matching engine
    this.tradeSubscription = this.wsService.trades$.subscribe((trade) => {
      console.log('💥 TRADE RECEIVED IN UI:', trade);
      
      // Add to trades array at top for live feed
      this.trades.unshift(trade);

      // Update trade statistics
      this.tradeStats.totalTrades++;
      this.tradeStats.totalTradeValue += trade.notional;
      this.tradeStats.totalTradeQty += trade.quantity;
      
      if (trade.side === 'BUY') {
        this.tradeStats.buyTrades++;
      } else {
        this.tradeStats.sellTrades++;
      }
      
      this.tradeStats.avgTradeSize = this.tradeStats.totalTradeQty / this.tradeStats.totalTrades;
      this.tradeStats.avgTradePrice = this.tradeStats.totalTradeValue / this.tradeStats.totalTradeQty;

      // Limit to 500 trades to prevent memory issues
      if (this.trades.length > 500) {
        this.trades = this.trades.slice(0, 500);
      }
      
      // Play sound and show notification for trades
      if (this.soundEnabled) {
        this.playTradeAlert();
      }
      
      this.showNotification(
        '💥 TRADE EXECUTED',
        `${trade.symbol} ${trade.side} ${trade.quantity} @ $${trade.price.toFixed(2)}`
      );
    });
    
    // Request notification permission
    if ('Notification' in window && Notification.permission === 'default') {
      Notification.requestPermission();
    }
  }

  ngOnDestroy(): void {
    this.subscription?.unsubscribe();
    this.connectionSubscription?.unsubscribe();
    this.tradeSubscription?.unsubscribe();
  }
  
  // Filtering Methods
  applyFilters(): void {
    let filtered = [...this.orders];
    
    // Apply search filter
    if (this.searchTerm) {
      const term = this.searchTerm.toLowerCase();
      filtered = filtered.filter(order => 
        order.clOrdID.toLowerCase().includes(term) ||
        order.symbol.toLowerCase().includes(term) ||
        order.ordStatus.toLowerCase().includes(term)
      );
    }
    
    // Apply side filter
    if (this.sideFilter !== 'all') {
      filtered = filtered.filter(order => 
        this.sideFilter === 'buy' ? order.side === '1' : order.side === '2'
      );
    }
    
    // Apply status filter
    if (this.statusFilter !== 'all') {
      filtered = filtered.filter(order => order.ordStatus === this.statusFilter);
    }
    
    // Apply time filter
    if (this.timeFilter !== 'all') {
      const now = Date.now();
      const timeThresholds: Record<TimeFilter, number> = {
        'all': 0,
        '5min': 5 * 60 * 1000,
        '1hour': 60 * 60 * 1000,
        'today': now - new Date().setHours(0, 0, 0, 0)
      };
      
      const threshold = timeThresholds[this.timeFilter];
      if (threshold > 0) {
        filtered = filtered.filter(order => 
          order.timestamp && (now - order.timestamp) <= threshold
        );
      }
    }
    
    // Apply sorting
    filtered.sort((a, b) => {
      let aVal: any, bVal: any;
      
      switch (this.sortColumn) {
        case 'timestamp':
          aVal = a.timestamp || 0;
          bVal = b.timestamp || 0;
          break;
        case 'clOrdID':
          aVal = a.clOrdID;
          bVal = b.clOrdID;
          break;
        case 'symbol':
          aVal = a.symbol;
          bVal = b.symbol;
          break;
        case 'price':
          aVal = a.price;
          bVal = b.price;
          break;
        case 'quantity':
          aVal = a.quantity;
          bVal = b.quantity;
          break;
        case 'value':
          aVal = a.price * a.quantity;
          bVal = b.price * b.quantity;
          break;
        case 'ordStatus':
          aVal = a.ordStatus;
          bVal = b.ordStatus;
          break;
        default:
          aVal = a.timestamp || 0;
          bVal = b.timestamp || 0;
      }
      
      const comparison = aVal < bVal ? -1 : aVal > bVal ? 1 : 0;
      return this.sortDirection === 'asc' ? comparison : -comparison;
    });
    
    this.filteredOrders = filtered;
  }
  
  onSearchChange(): void {
    this.applyFilters();
  }
  
  setSideFilter(side: 'all' | 'buy' | 'sell'): void {
    this.sideFilter = side;
    this.applyFilters();
  }
  
  setStatusFilter(status: 'all' | 'NEW' | 'FILLED' | 'PARTIALLY_FILLED' | 'REJECTED'): void {
    this.statusFilter = status;
    this.applyFilters();
  }
  
  setTimeFilter(filter: TimeFilter): void {
    this.timeFilter = filter;
    this.applyFilters();
  }
  
  // Sorting Methods
  sort(column: SortColumn): void {
    if (this.sortColumn === column) {
      this.sortDirection = this.sortDirection === 'asc' ? 'desc' : 'asc';
    } else {
      this.sortColumn = column;
      this.sortDirection = 'desc';
    }
    this.applyFilters();
  }
  
  // Statistics Methods
  updateStats(): void {
    // Use cumulative counters for accurate stats across all orders
    const totalOrders = this.cumulativeStats.totalOrders;
    const totalValue = this.cumulativeStats.totalValue;
    const buyOrders = this.cumulativeStats.buyOrders;
    const sellOrders = this.cumulativeStats.sellOrders;
    const filledOrders = this.cumulativeStats.filledOrders;
    const rejectedOrders = this.cumulativeStats.rejectedOrders;
    const avgOrderSize = totalOrders > 0 ? this.cumulativeStats.totalQuantity / totalOrders : 0;
    const fillRate = totalOrders > 0 ? (filledOrders / totalOrders) * 100 : 0;
    
    this.stats = {
      totalOrders,
      totalValue,
      buyOrders,
      sellOrders,
      filledOrders,
      rejectedOrders,
      avgOrderSize,
      fillRate
    };
  }
  
  // Export Methods
  exportToCSV(): void {
    const headers = this.columns
      .filter(col => col.visible && col.key !== 'side')
      .map(col => col.label);
    
    const rows = this.filteredOrders.map(order => {
      const row: string[] = [];
      this.columns.forEach(col => {
        if (!col.visible) return;
        
        switch (col.key) {
          case 'timestamp':
            row.push(order.timestamp ? new Date(order.timestamp).toLocaleString() : '');
            break;
          case 'clOrdID':
            row.push(order.clOrdID);
            break;
          case 'symbol':
            row.push(order.symbol);
            break;
          case 'side':
            row.push(this.getSideLabel(order.side));
            break;
          case 'quantity':
            row.push(order.quantity.toString());
            break;
          case 'cumQty':
            row.push(order.cumQty.toString());
            break;
          case 'price':
            row.push(order.price.toString());
            break;
          case 'value':
            row.push((order.price * order.quantity).toString());
            break;
          case 'ordStatus':
            row.push(order.ordStatus);
            break;
        }
      });
      return row;
    });
    
    const csvContent = [
      headers.join(','),
      ...rows.map(row => row.map(cell => `"${cell}"`).join(','))
    ].join('\n');
    
    const blob = new Blob([csvContent], { type: 'text/csv;charset=utf-8;' });
    const link = document.createElement('a');
    const url = URL.createObjectURL(blob);
    
    link.setAttribute('href', url);
    link.setAttribute('download', `orders_${new Date().toISOString()}.csv`);
    link.style.visibility = 'hidden';
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
  }
  
  // Column Visibility Methods
  toggleColumn(columnKey: string): void {
    const column = this.columns.find(col => col.key === columnKey);
    if (column) {
      column.visible = !column.visible;
      this.savePreferences();
    }
  }
  
  // Theme Methods
  toggleTheme(): void {
    this.theme = this.theme === 'dark' ? 'light' : 'dark';
    document.body.classList.toggle('light-theme', this.theme === 'light');
    this.savePreferences();
  }
  
  // Notification Methods
  showNotification(title: string, body: string): void {
    if ('Notification' in window && Notification.permission === 'granted') {
      new Notification(title, { body, icon: '/favicon.ico' });
    }
  }
  
  playAlert(): void {
    // Create a simple beep using Web Audio API
    try {
      const audioContext = new (window.AudioContext || (window as any).webkitAudioContext)();
      const oscillator = audioContext.createOscillator();
      const gainNode = audioContext.createGain();
      
      oscillator.connect(gainNode);
      gainNode.connect(audioContext.destination);
      
      oscillator.frequency.value = 800;
      oscillator.type = 'sine';
      
      gainNode.gain.setValueAtTime(0.3, audioContext.currentTime);
      gainNode.gain.exponentialRampToValueAtTime(0.01, audioContext.currentTime + 0.5);
      
      oscillator.start(audioContext.currentTime);
      oscillator.stop(audioContext.currentTime + 0.5);
    } catch (e) {
      console.error('Audio playback failed:', e);
    }
  }
  
  // LAB 7: Play sound for trade execution (different from rejection alert)
  playTradeAlert(): void {
    try {
      const audioContext = new (window.AudioContext || (window as any).webkitAudioContext)();
      
      // Create a two-tone alert for trades (ascending frequencies)
      const now = audioContext.currentTime;
      
      // First tone
      const osc1 = audioContext.createOscillator();
      const gain1 = audioContext.createGain();
      osc1.connect(gain1);
      gain1.connect(audioContext.destination);
      osc1.frequency.value = 1200;
      osc1.type = 'sine';
      gain1.gain.setValueAtTime(0.2, now);
      gain1.gain.exponentialRampToValueAtTime(0.01, now + 0.15);
      osc1.start(now);
      osc1.stop(now + 0.15);
      
      // Second tone
      const osc2 = audioContext.createOscillator();
      const gain2 = audioContext.createGain();
      osc2.connect(gain2);
      gain2.connect(audioContext.destination);
      osc2.frequency.value = 1600;
      osc2.type = 'sine';
      gain2.gain.setValueAtTime(0.2, now + 0.1);
      gain2.gain.exponentialRampToValueAtTime(0.01, now + 0.25);
      osc2.start(now + 0.1);
      osc2.stop(now + 0.25);
    } catch (e) {
      console.error('Trade alert audio failed:', e);
    }
  }
  
  toggleSound(): void {
    this.soundEnabled = !this.soundEnabled;
    this.savePreferences();
  }
  
  // Preference Methods
  savePreferences(): void {
    const prefs = {
      columns: this.columns,
      theme: this.theme,
      showStats: this.showStats,
      soundEnabled: this.soundEnabled
    };
    localStorage.setItem('orderGridPreferences', JSON.stringify(prefs));
  }
  
  loadPreferences(): void {
    const saved = localStorage.getItem('orderGridPreferences');
    if (saved) {
      try {
        const prefs = JSON.parse(saved);
        if (prefs.columns) this.columns = prefs.columns;
        if (prefs.theme) {
          this.theme = prefs.theme;
          document.body.classList.toggle('light-theme', this.theme === 'light');
        }
        if (prefs.showStats !== undefined) this.showStats = prefs.showStats;
        if (prefs.soundEnabled !== undefined) this.soundEnabled = prefs.soundEnabled;
      } catch (e) {
        console.error('Failed to load preferences:', e);
      }
    }
  }

  getSideClass(side: string): string {
    return side === '1' ? 'buy' : 'sell';
  }

  getSideLabel(side: string): string {
    return side === '1' ? 'BUY' : 'SELL';
  }

  formatPrice(price: number): string {
    return price.toFixed(2);
  }

  formatQuantity(qty: number): string {
    return qty.toLocaleString();
  }
  
  formatTimestamp(timestamp: number | undefined): string {
    if (!timestamp) return '';
    const date = new Date(timestamp);
    return date.toLocaleTimeString();
  }
  
  formatDate(timestamp: number | undefined): string {
    if (!timestamp) return '';
    const date = new Date(timestamp);
    return date.toLocaleDateString() + ' ' + date.toLocaleTimeString();
  }

  getStatusClass(status: string): string {
    switch(status) {
      case 'FILLED':
        return 'status-filled';
      case 'PARTIALLY_FILLED':
        return 'status-partial';
      case 'NEW':
        return 'status-new';
      case 'REJECTED':
        return 'status-rejected';
      default:
        return 'status-unknown';
    }
  }

  clearOrders(): void {
    this.orders = [];
    this.filteredOrders = [];
    // Reset cumulative stats
    this.cumulativeStats = {
      totalOrders: 0,
      totalValue: 0,
      buyOrders: 0,
      sellOrders: 0,
      filledOrders: 0,
      rejectedOrders: 0,
      totalQuantity: 0
    };
    this.updateStats();
  }

  trackByOrderId(index: number, order: Order): string {
    return order.clOrdID;
  }
  
  toggleStats(): void {
    this.showStats = !this.showStats;
    this.savePreferences();
  }
  
  toggleFilters(): void {
    this.showFilters = !this.showFilters;
  }
  
  toggleColumnConfig(): void {
    this.showColumnConfig = !this.showColumnConfig;
  }
  
  isColumnVisible(key: string): boolean {
    const column = this.columns.find(col => col.key === key);
    return column ? column.visible : true;
  }

  // LAB 7: Format trade timestamp with milliseconds for better precision
  formatTradeTime(timestamp: string | undefined): string {
    if (!timestamp) return '';
    try {
      const date = new Date(timestamp);
      const hours = String(date.getHours()).padStart(2, '0');
      const mins = String(date.getMinutes()).padStart(2, '0');
      const secs = String(date.getSeconds()).padStart(2, '0');
      const ms = String(date.getMilliseconds()).padStart(3, '0');
      return `${hours}:${mins}:${secs}.${ms}`;
    } catch (e) {
      return '';
    }
  }

  // LAB 7: Track by trade ID for *ngFor performance
  trackByTradeId(index: number, trade: Trade): string {
    return trade.exec_id;
  }
}
