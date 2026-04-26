import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable, map } from 'rxjs';
import { environment } from '../../../environments/environment';
import {
  Alarm,
  AlarmRequest,
  AcknowledgeRequest,
  AlarmEvent,
  AlarmStats,
  AlarmFilterParams,
  PagedResponse,
  ApiResponse
} from '../models/alarm.model';

@Injectable({ providedIn: 'root' })
export class AlarmService {

  private readonly baseUrl = `${environment.apiBaseUrl}/alarms`;

  constructor(private http: HttpClient) {}

  getAlarms(filters: AlarmFilterParams = {}): Observable<PagedResponse<Alarm>> {
    let params = new HttpParams();
    if (filters.active !== undefined) params = params.set('active', String(filters.active));
    if (filters.severity) params = params.set('severity', filters.severity);
    if (filters.page !== undefined) params = params.set('page', String(filters.page));
    if (filters.size !== undefined) params = params.set('size', String(filters.size));
    if (filters.sortBy) params = params.set('sortBy', filters.sortBy);
    if (filters.sortDir) params = params.set('sortDir', filters.sortDir);

    return this.http.get<ApiResponse<PagedResponse<Alarm>>>(this.baseUrl, { params })
      .pipe(map(r => r.data));
  }

  getAlarmById(id: number): Observable<Alarm> {
    return this.http.get<ApiResponse<Alarm>>(`${this.baseUrl}/${id}`)
      .pipe(map(r => r.data));
  }

  getActiveAlarms(): Observable<Alarm[]> {
    return this.http.get<ApiResponse<Alarm[]>>(`${this.baseUrl}/active`)
      .pipe(map(r => r.data));
  }

  getStats(): Observable<AlarmStats> {
    return this.http.get<ApiResponse<AlarmStats>>(`${this.baseUrl}/stats`)
      .pipe(map(r => r.data));
  }

  createAlarm(request: AlarmRequest): Observable<Alarm> {
    return this.http.post<ApiResponse<Alarm>>(this.baseUrl, request)
      .pipe(map(r => r.data));
  }

  acknowledgeAlarm(id: number, request: AcknowledgeRequest): Observable<Alarm> {
    return this.http.patch<ApiResponse<Alarm>>(`${this.baseUrl}/${id}/acknowledge`, request)
      .pipe(map(r => r.data));
  }

  clearAlarm(id: number): Observable<Alarm> {
    return this.http.patch<ApiResponse<Alarm>>(`${this.baseUrl}/${id}/clear`, {})
      .pipe(map(r => r.data));
  }

  deleteAlarm(id: number): Observable<void> {
    return this.http.delete<ApiResponse<void>>(`${this.baseUrl}/${id}`)
      .pipe(map(() => void 0));
  }

  getEventsForAlarm(alarmId: number): Observable<AlarmEvent[]> {
    return this.http.get<ApiResponse<AlarmEvent[]>>(`${this.baseUrl}/${alarmId}/events`)
      .pipe(map(r => r.data));
  }
}
