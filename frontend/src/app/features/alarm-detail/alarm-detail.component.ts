import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { AlarmService } from '../../core/services/alarm.service';
import { Alarm, AlarmEvent } from '../../core/models/alarm.model';
import { SeverityBadgeComponent } from '../../shared/components/severity-badge.component';

@Component({
  selector: 'app-alarm-detail',
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule, SeverityBadgeComponent],
  templateUrl: './alarm-detail.component.html',
  styleUrls: ['./alarm-detail.component.scss']
})
export class AlarmDetailComponent implements OnInit {

  alarm: Alarm | null = null;
  events: AlarmEvent[] = [];
  loading = false;
  error: string | null = null;

  showAckModal = false;
  ackBy = '';
  ackRemarks = '';
  ackLoading = false;

  constructor(
    private route: ActivatedRoute,
    private alarmService: AlarmService
  ) {}

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.loadAlarm(id);
    this.loadEvents(id);
  }

  loadAlarm(id: number): void {
    this.loading = true;
    this.alarmService.getAlarmById(id).subscribe({
      next: (a) => { this.alarm = a; this.loading = false; },
      error: (err) => { this.error = err.message; this.loading = false; }
    });
  }

  loadEvents(id: number): void {
    this.alarmService.getEventsForAlarm(id).subscribe({
      next: (e) => this.events = e,
      error: () => {}
    });
  }

  openAckModal(): void {
    this.ackBy = '';
    this.ackRemarks = '';
    this.showAckModal = true;
  }

  submitAck(): void {
    if (!this.alarm || !this.ackBy.trim()) return;
    this.ackLoading = true;
    this.alarmService.acknowledgeAlarm(this.alarm.id, {
      acknowledgedBy: this.ackBy,
      remarks: this.ackRemarks
    }).subscribe({
      next: (updated) => {
        this.alarm = updated;
        this.showAckModal = false;
        this.ackLoading = false;
        this.loadEvents(updated.id);
      },
      error: (err) => { this.error = err.message; this.ackLoading = false; }
    });
  }

  clearAlarm(): void {
    if (!this.alarm || !confirm('Clear this alarm?')) return;
    this.alarmService.clearAlarm(this.alarm.id).subscribe({
      next: (updated) => {
        this.alarm = updated;
        this.loadEvents(updated.id);
      },
      error: (err) => this.error = err.message
    });
  }

  stateClass(state: string): string {
    const map: Record<string, string> = {
      RAISED: 'state-raised',
      ACKNOWLEDGED: 'state-acked',
      CLEARED: 'state-cleared'
    };
    return map[state] ?? '';
  }
}
