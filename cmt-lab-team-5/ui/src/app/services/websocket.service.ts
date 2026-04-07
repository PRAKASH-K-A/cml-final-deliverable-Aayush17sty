import { Injectable } from '@angular/core';
import { Subject, Observable } from 'rxjs';

export interface Order {
  clOrdID: string;
  symbol: string;
  side: string;
  price: number;
  quantity: number;
  cumQty: number;
  ordStatus: string;
  isNew?: boolean;
  timestamp?: number;
}

export interface Trade {
  type: string;
  exec_id: string;
  symbol: string;
  side: string;
  quantity: number;
  price: number;
  notional: number;
  timestamp: string;
}

export interface Message {
  type: 'ORDER' | 'TRADE';
  data: Order | Trade;
}

@Injectable({
  providedIn: 'root'
})
export class WebsocketService {
  private socket: WebSocket | null = null;
  private messagesSubject = new Subject<Order>();
  private tradesSubject = new Subject<Trade>();
  private connectedSubject = new Subject<boolean>();

  public messages$: Observable<Order> = this.messagesSubject.asObservable();
  public trades$: Observable<Trade> = this.tradesSubject.asObservable();
  public connected$: Observable<boolean> = this.connectedSubject.asObservable();

  constructor() {
    this.connect();
  }

  private connect(): void {
    try {
      this.socket = new WebSocket('ws://localhost:8080');

      this.socket.onopen = () => {
        console.log('✅ Connected to Order Service WebSocket');
        this.connectedSubject.next(true);
      };

      this.socket.onmessage = (event) => {
        try {
          const data = JSON.parse(event.data);
          
          // Handle Trade messages (Lab 7 Matching Engine)
          if (data.type === 'TRADE') {
            const trade: Trade = data;
            trade.timestamp = trade.timestamp || new Date().toISOString();
            console.log('💥 TRADE EXECUTED:', trade);
            this.tradesSubject.next(trade);
          } 
          // Handle Order messages
          else {
            const order: Order = data;
            if (!order.timestamp) {
              order.timestamp = Date.now();
            }
            console.log('📦 Received order:', order);
            this.messagesSubject.next(order);
          }
        } catch (error) {
          console.error('Error parsing message:', error);
        }
      };

      this.socket.onclose = () => {
        console.log('❌ Disconnected from Order Service');
        this.connectedSubject.next(false);
        // Attempt reconnection after 3 seconds
        setTimeout(() => this.connect(), 3000);
      };

      this.socket.onerror = (error) => {
        console.error('WebSocket error:', error);
        this.connectedSubject.next(false);
      };
    } catch (error) {
      console.error('Failed to connect:', error);
      this.connectedSubject.next(false);
    }
  }

  public disconnect(): void {
    if (this.socket) {
      this.socket.close();
      this.socket = null;
    }
  }
}
