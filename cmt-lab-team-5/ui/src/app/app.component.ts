import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { OrderGridComponent } from './components/order-grid/order-grid.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, OrderGridComponent],
  template: `
    <div class="app-container">
      <header class="app-header">
        <div class="header-content">
          <div class="logo">
            <svg width="32" height="32" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
              <path d="M13 2L3 14h9l-1 8 10-12h-9l1-8z" fill="#2dd4bf" stroke="#2dd4bf" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
            </svg>
            <h1>Order Management System</h1>
          </div>
          <div class="connection-status" [class.connected]="true">
            <span class="status-dot"></span>
            <span>Live</span>
          </div>
        </div>
      </header>
      
      <main class="app-main">
        <app-order-grid></app-order-grid>
      </main>
    </div>
  `,
  styles: [`
    .app-container {
      display: flex;
      flex-direction: column;
      min-height: 100vh;
    }

    .app-header {
      background: rgba(30, 41, 59, 0.8);
      backdrop-filter: blur(10px);
      border-bottom: 1px solid var(--border-color);
      padding: 1rem 2rem;
      position: sticky;
      top: 0;
      z-index: 100;
    }

    .header-content {
      max-width: 1400px;
      margin: 0 auto;
      display: flex;
      justify-content: space-between;
      align-items: center;
    }

    .logo {
      display: flex;
      align-items: center;
      gap: 1rem;
    }

    .logo h1 {
      font-size: 1.5rem;
      font-weight: 600;
      color: var(--text-primary);
    }

    .connection-status {
      display: flex;
      align-items: center;
      gap: 0.5rem;
      padding: 0.5rem 1rem;
      border-radius: 50px;
      background: rgba(239, 68, 68, 0.1);
      font-size: 0.875rem;
      font-weight: 500;
      color: var(--accent-red);
      transition: all 0.3s ease;
    }

    .connection-status.connected {
      background: rgba(16, 185, 129, 0.1);
      color: var(--accent-green);
    }

    .status-dot {
      width: 8px;
      height: 8px;
      border-radius: 50%;
      background: var(--accent-red);
      animation: pulse 2s infinite;
    }

    .connection-status.connected .status-dot {
      background: var(--accent-green);
    }

    @keyframes pulse {
      0%, 100% { opacity: 1; }
      50% { opacity: 0.5; }
    }

    .app-main {
      flex: 1;
      padding: 2rem;
      max-width: 1400px;
      width: 100%;
      margin: 0 auto;
    }

    @media (max-width: 768px) {
      .app-header {
        padding: 1rem;
      }
      
      .logo h1 {
        font-size: 1.25rem;
      }
      
      .app-main {
        padding: 1rem;
      }
    }
  `]
})
export class AppComponent {
}
