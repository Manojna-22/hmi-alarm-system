// Enums
export type Severity = 'LOW' | 'MEDIUM' | 'HIGH' | 'CRITICAL';
export type AlarmState = 'RAISED' | 'ACKNOWLEDGED' | 'CLEARED';

// Alarm model
export interface Alarm {
  id: number;
  code: string;
  message: string;
  severity: Severity;
  active: boolean;
  acknowledged: boolean;
  acknowledgedBy: string | null;
  acknowledgedAt: string | null;
  createdAt: string;
  updatedAt: string;
}

// Alarm Event model
export interface AlarmEvent {
  id: number;
  alarmId: number;
  ts: string;
  state: AlarmState;
  remarks: string;
}

// DTOs for requests
export interface AlarmRequest {
  code: string;
  message: string;
  severity: Severity;
}

export interface AcknowledgeRequest {
  acknowledgedBy: string;
  remarks?: string;
}

// Paged response
export interface PagedResponse<T> {
  content: T[];
  pageNumber: number;
  pageSize: number;
  totalElements: number;
  totalPages: number;
  last: boolean;
}

// API wrapper
export interface ApiResponse<T> {
  success: boolean;
  message: string;
  data: T;
  timestamp: string;
}

// Stats
export interface AlarmStats {
  totalActive: number;
  totalCleared: number;
  totalAcknowledged: number;
  activeBySeverity: { [key in Severity]?: number };
}

// Filter params
export interface AlarmFilterParams {
  active?: boolean;
  severity?: Severity;
  page?: number;
  size?: number;
  sortBy?: string;
  sortDir?: 'asc' | 'desc';
}
