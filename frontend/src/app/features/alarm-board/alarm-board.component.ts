import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { Subject, interval } from 'rxjs';
import { takeUntil, switchMap, startWith } from 'rxjs/operators';
import { AlarmService } from '../../core/services/alarm.service';
import {
  Alarm, AlarmStats, AlarmFilterParams, Severity, AlarmRequest
} from '../../core/models/alarm.model';
import { SeverityBadgeComponent } from '../../shared/components/severity-badge.component';

@Component({
  selector: 'app-alarm-board',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule, SeverityBadgeComponent],
  templateUrl: './alarm-board.component.html',
  styleUrls: ['./alarm-board.component.scss']
})
export class AlarmBoardComponent implements OnInit, OnDestroy {

  alarms: Alarm[] = [];
  stats: AlarmStats | null = null;
  loading = false;
  error: string | null = null;

  // Filters
  filters: AlarmFilterParams = { page: 0, size: 20, sortBy: 'createdAt', sortDir: 'desc' };
  totalElements = 0;
  totalPages = 0;

  // Create alarm form
  showCreateForm = false;
  createForm: AlarmRequest = { code: '', message: '', severity: 'HIGH' };
  createLoading = false;
  createError: string | null = null;

  // Ack modal
  showAckModal = false;
  ackTargetAlarm: Alarm | null = null;
  ackBy = '';
  ackRemarks = '';
  ackLoading = false;

  severities: Severity[] = ['LOW', 'MEDIUM', 'HIGH', 'CRITICAL'];

  private destroy$ = new Subject<void>();

  constructor(private alarmService: AlarmService) {}

  ngOnInit(): void {
    this.loadData();
    // Poll every 10s
    interval(10000).pipe(
      takeUntil(this.destroy$),
      startWith(0)
    ).subscribe(() => this.loadStats());
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  loadData(): void {
    this.loading = true;
    this.error = null;
    this.alarmService.getAlarms(this.filters).subscribe({
      next: (paged) => {
        this.alarms = paged.content;
        this.totalElements = paged.totalElements;
        this.totalPages = paged.totalPages;
        this.loading = false;
      },
      error: (err) => {
        this.error = err.message;
        this.loading = false;
      }
    });
  }

  loadStats(): void {
    this.alarmService.getStats().subscribe({
      next: (stats) => this.stats = stats,
      error: () => {}
    });
  }

  applyFilter(): void {
    this.filters.page = 0;
    this.loadData();
  }

  clearFilters(): void {
    this.filters = { page: 0, size: 20, sortBy: 'createdAt', sortDir: 'desc' };
    this.loadData();
  }

  goToPage(page: number): void {
    this.filters.page = page;
    this.loadData();
  }

  openCreateForm(): void {
    this.showCreateForm = true;
    this.createForm = { code: '', message: '', severity: 'HIGH' };
    this.createError = null;
  }

  submitCreate(): void {
    if (!this.createForm.code || !this.createForm.message) {
      this.createError = 'Code and message are required.';
      return;
    }
    this.createLoading = true;
    this.alarmService.createAlarm(this.createForm).subscribe({
      next: () => {
        this.showCreateForm = false;
        this.createLoading = false;
        this.loadData();
        this.loadStats();
      },
      error: (err) => {
        this.createError = err.message;
        this.createLoading = false;
      }
    });
  }

  openAckModal(alarm: Alarm): void {
    this.ackTargetAlarm = alarm;
    this.ackBy = '';
    this.ackRemarks = '';
    this.showAckModal = true;
  }

  submitAck(): void {
    if (!this.ackTargetAlarm || !this.ackBy.trim()) return;
    this.ackLoading = true;
    this.alarmService.acknowledgeAlarm(this.ackTargetAlarm.id, {
      acknowledgedBy: this.ackBy,
      remarks: this.ackRemarks
    }).subscribe({
      next: () => {
        this.showAckModal = false;
        this.ackLoading = false;
        this.loadData();
        this.loadStats();
      },
      error: (err) => {
        this.error = err.message;
        this.ackLoading = false;
      }
    });
  }

  clearAlarm(alarm: Alarm): void {
    if (!confirm(`Clear alarm "${alarm.code}"?`)) return;
    this.alarmService.clearAlarm(alarm.id).subscribe({
      next: () => { this.loadData(); this.loadStats(); },
      error: (err) => this.error = err.message
    });
  }

  deleteAlarm(alarm: Alarm): void {
    if (!confirm(`Permanently delete alarm "${alarm.code}"?`)) return;
    this.alarmService.deleteAlarm(alarm.id).subscribe({
      next: () => { this.loadData(); this.loadStats(); },
      error: (err) => this.error = err.message
    });
  }

  trackById(_: number, alarm: Alarm): number {
    return alarm.id;
  }

  get pagesArray(): number[] {
    return Array.from({ length: this.totalPages }, (_, i) => i);
  }

  getSeverityCount(sev: Severity): number {
    return this.stats?.activeBySeverity?.[sev] ?? 0;
  }

  getDonutDashArray(sev: Severity): string {
    const total = this.stats?.totalActive ?? 0;
    if (total === 0) return '0 100';
    const pct = ((this.getSeverityCount(sev)) / total) * 100;
    return `${pct.toFixed(1)} ${(100 - pct).toFixed(1)}`;
  }
}
