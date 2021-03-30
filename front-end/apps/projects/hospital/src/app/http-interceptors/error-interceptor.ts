import { Injectable } from '@angular/core';
import {
	HttpEvent, HttpInterceptor, HttpHandler, HttpRequest, HttpErrorResponse
} from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';


/**
 * Pass untouched request through to the next request handler.
 * https://angular.io/guide/http#http-interceptors
 */
@Injectable()
export class ErrorInterceptor implements HttpInterceptor {

	constructor() {
	}

	intercept(
		request: HttpRequest<any>,
		next: HttpHandler
	): Observable<HttpEvent<any>> {

		return next.handle(request)
			.pipe(
				catchError((error: HttpErrorResponse) => {

					if (this.clientSideError(error)) {
						throwError(`Error: ${error.error.message}`);
					}
					if (error.error) {
						// errores de BACKEND
						return throwError(error.error);
					}
					return throwError(`Error Status: ${error.status}\nMessage: ${error.message}`);
				})
			);
	}

	private clientSideError(error: HttpErrorResponse) {
		return error.error instanceof ErrorEvent;
	}

}
