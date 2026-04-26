import { Routes } from '@angular/router';

export const routes: Routes = [
  {
    path: '',
    redirectTo: 'alarms',
    pathMatch: 'full'
  },
  {
    path: 'alarms',
    loadComponent: () =>
      import('./features/alarm-board/alarm-board.component').then(m => m.AlarmBoardComponent)
  },
  {
    path: 'alarms/:id',
    loadComponent: () =>
      import('./features/alarm-detail/alarm-detail.component').then(m => m.AlarmDetailComponent)
  },
  {
    path: '**',
    redirectTo: 'alarms'
  }
];
