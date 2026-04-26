import { HttpInterceptorFn, HttpErrorResponse } from '@angular/common/http';
import { catchError, throwError } from 'rxjs';

export const errorInterceptor: HttpInterceptorFn = (req, next) => {
  return next(req).pipe(
    catchError((error: HttpErrorResponse) => {
      let errorMessage = 'An unexpected error occurred';

      if (error.error?.message) {
        errorMessage = error.error.message;
      } else if (error.status === 0) {
        errorMessage = 'Cannot connect to server. Please ensure the backend is running.';
      } else if (error.status === 404) {
        errorMessage = 'Resource not found.';
      } else if (error.status === 409) {
        errorMessage = error.error?.message || 'Conflict error.';
      } else if (error.status === 500) {
        errorMessage = 'Internal server error. Please contact support.';
      }

      console.error(`[HTTP Error] ${error.status} — ${errorMessage}`, error);
      return throwError(() => ({ message: errorMessage, status: error.status, raw: error }));
    })
  );
};
