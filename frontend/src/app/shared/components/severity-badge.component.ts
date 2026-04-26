import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Severity } from '../../core/models/alarm.model';

@Component({
  selector: 'app-severity-badge',
  standalone: true,
  imports: [CommonModule],
  template: `
    <span class="badge" [ngClass]="severity.toLowerCase()">{{ severity }}</span>
  `,
  styles: [`
    .badge {
      display: inline-block; padding: 2px 10px; border-radius: 12px;
      font-size: 0.72rem; font-weight: 700; letter-spacing: 0.8px; text-transform: uppercase;
    }
    .low    { background: #14532d; color: #4ade80; border: 1px solid #166534; }
    .medium { background: #713f12; color: #fbbf24; border: 1px solid #92400e; }
    .high   { background: #7c2d12; color: #fb923c; border: 1px solid #9a3412; }
    .critical { background: #450a0a; color: #f87171; border: 1px solid #991b1b;
                animation: pulse 1.5s infinite; }
    @keyframes pulse {
      0%, 100% { box-shadow: 0 0 0 0 rgba(248,113,113,0.4); }
      50%       { box-shadow: 0 0 0 4px rgba(248,113,113,0); }
    }
  `]
})
export class SeverityBadgeComponent {
  @Input({ required: true }) severity!: Severity;
}
