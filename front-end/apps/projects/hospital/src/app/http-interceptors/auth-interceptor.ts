import { Injectable } from '@angular/core';
import {
	HttpErrorResponse,
	HttpEvent,
	HttpHandler,
	HttpInterceptor,
	HttpRequest,
} from '@angular/common/http';
import { throwError, EMPTY, Observable, ReplaySubject } from 'rxjs';
import { catchError, switchMap } from 'rxjs/operators';

import { AuthenticationService } from '../modules/auth/services/authentication.service';

const shouldAuthorize = (url: string) => !url.startsWith('http'); // any absolute request (another domain)

const isUnauthorized = (error: any): boolean =>  error instanceof HttpErrorResponse && error.status === 401;

@Injectable()
export class AuthInterceptor implements HttpInterceptor {
	private refreshTokenSubject: ReplaySubject<any>;

	constructor(
		private readonly authenticationService: AuthenticationService,
	) { }

	intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
		return next.handle(req.clone())
			.pipe(
				catchError((error: any) => {
					if (isUnauthorized(error) && shouldAuthorize(req.url)) {
						return this.refreshToken().pipe(
							switchMap(() => {
								return next.handle(req.clone());
							})
						);
					}
					return throwError(error);
				},
			));
	}

	private refreshToken(): Observable<any> {
		if (!this.refreshTokenSubject) {
			this.refreshTokenSubject = new ReplaySubject<any>(1);
			this.authenticationService.tokenRefresh().pipe(
				catchError(_ => {
					this.refreshTokenSubject = undefined;
					this.authenticationService.logout();
					return EMPTY;
				}),
			).subscribe(token => {
				if (!token) {
					this.refreshTokenSubject = undefined;
					this.authenticationService.logout();
					return;
				}
				this.refreshTokenSubject.next(token);
				this.refreshTokenSubject.complete();
				this.refreshTokenSubject = undefined;
			});
		}
		return this.refreshTokenSubject?.asObservable() || EMPTY;
	}

}
