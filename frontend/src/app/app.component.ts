import { Component } from '@angular/core';
import { RouterOutlet, RouterLink, RouterLinkActive } from '@angular/router';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, RouterLink, RouterLinkActive],
  template: `
    <div class="app-shell">
      <header class="top-nav">
        <div class="nav-brand">
          <span class="nav-icon">⚠</span>
          <span class="nav-title">HMI Alarm Board</span>
        </div>
        <nav class="nav-links">
          <a routerLink="/alarms" routerLinkActive="active">Dashboard</a>
        </nav>
      </header>
      <main class="content">
        <router-outlet />
      </main>
    </div>
  `,
  styles: [`
    .app-shell { min-height: 100vh; background: #0f1117; }
    .top-nav {
      display: flex; align-items: center; justify-content: space-between;
      padding: 0 2rem; height: 60px; background: #161b27;
      border-bottom: 1px solid #2a3040; position: sticky; top: 0; z-index: 100;
    }
    .nav-brand { display: flex; align-items: center; gap: 0.75rem; }
    .nav-icon { font-size: 1.4rem; color: #f59e0b; }
    .nav-title { font-size: 1.1rem; font-weight: 700; color: #e2e8f0; letter-spacing: 0.5px; }
    .nav-links a {
      color: #94a3b8; text-decoration: none; font-size: 0.9rem;
      padding: 0.35rem 0.9rem; border-radius: 6px; transition: all 0.2s;
    }
    .nav-links a:hover, .nav-links a.active { color: #e2e8f0; background: #2a3040; }
    .content { padding: 1.5rem 2rem; }
  `]
})
export class AppComponent {}
