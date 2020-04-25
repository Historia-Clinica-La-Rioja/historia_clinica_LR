import { Injectable } from '@angular/core';
import {
	HttpEvent, HttpInterceptor, HttpHandler, HttpRequest, HttpErrorResponse
} from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError, retry } from 'rxjs/operators';
import { Router } from '@angular/router';


/**
 * Pass untouched request through to the next request handler.
 * https://angular.io/guide/http#http-interceptors
 */
@Injectable()
export class ErrorInterceptor implements HttpInterceptor {

	constructor(private router: Router) {
	}

	intercept(
		request: HttpRequest<any>,
		next: HttpHandler
	): Observable<HttpEvent<any>> {
		return next.handle(request)
			.pipe(
				retry(1),
				catchError((error: HttpErrorResponse) => {
					let errorMessage = '';
					if (this.unauthorizedError(error)) {
						this.router.navigate(['/auth/login']);
						return throwError(errorMessage);
					}

					if (this.clientSideError(error)) {
						errorMessage = `Error: ${error.error.message}`;
					} else {
						errorMessage = `Error Status: ${error.status}\nMessage: ${error.message}`;
					}
					console.error(errorMessage);
					return throwError(errorMessage);
				})
			);
	}

	private clientSideError(error: HttpErrorResponse) {
		return error.error instanceof ErrorEvent;
	}

	private unauthorizedError(error: HttpErrorResponse) {
		console.log(error);
		return error.status === 401;
	}
}
