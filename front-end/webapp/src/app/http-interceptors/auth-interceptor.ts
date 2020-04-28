import { Injectable } from '@angular/core';
import {
	HttpInterceptor,
	HttpHandler,
	HttpRequest,
	HttpErrorResponse,
	HttpEvent,
} from '@angular/common/http';
import { Router } from '@angular/router';
import { throwError, Observable } from 'rxjs';
import { catchError } from 'rxjs/operators';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {

	constructor(
		private router: Router,
	) {

	}

	intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
		const authToken = localStorage.getItem('token');

		// Clone the request and replace the original headers with
		// cloned headers, updated with the authorization.
		const authReq = authToken && !req.url.endsWith('/auth/refresh') ? this.addToken(req,authToken) : req;

		// send cloned request with header to the next handler.
		return next.handle(authReq).pipe(
			catchError((error: any) => {
				if (error instanceof HttpErrorResponse && error.status === 401) {
					this.handle401Error();
				}
				return throwError(error);
			})
		);
	}

	private handle401Error() {
		// console.log('auth-interceptor handle401Error');
		this.router.navigate(['/auth/login']);
	}

	private addToken(req: HttpRequest<any>, token: string): HttpRequest<any> {
		return req.clone({ headers: req.headers.append('X-Auth-Token', token) });
	}
}
