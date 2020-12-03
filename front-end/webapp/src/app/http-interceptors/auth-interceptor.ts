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
import { environment } from '@environments/environment';
import { canRefreshToken, addToken } from '@core/utils/auth.utils';

const PUBLIC_ENDPOINTS = [
	'auth',
];

const urlIsPublic = (url: string) => PUBLIC_ENDPOINTS.some(endpointPrefix => url.startsWith(`${environment.apiBase}/${endpointPrefix}`));


const isUnauthorized = (error: any): boolean =>  error instanceof HttpErrorResponse && error.status === 401;

@Injectable()
export class AuthInterceptor implements HttpInterceptor {
	private refreshTokenSubject: ReplaySubject<string>;

	constructor(
		private authenticationService: AuthenticationService,
	) { }

	intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {

		if (urlIsPublic(req.url)) {
			return next.handle(req.clone());
		}

		return next.handle(addToken(req, localStorage.getItem('token')))
			.pipe(
				catchError((error: any) => {
						if (isUnauthorized(error)) {
							return this.refreshToken().pipe(
								switchMap(token => {
									return next.handle(addToken(req, token));
								})
							);
						}
						return throwError(error);
					},
				));

	}

	private refreshToken(): Observable<string> {
		if (!this.refreshTokenSubject) {
			this.refreshTokenSubject = new ReplaySubject<string>(1);
			this.callRefreshToken().pipe(
				catchError(_ => {
					this.refreshTokenSubject = undefined;
					this.authenticationService.logout();
					return EMPTY;
				}),
			).subscribe(token => {
				this.refreshTokenSubject.next(token);
				this.refreshTokenSubject.complete();
				this.refreshTokenSubject = undefined;
			});
		}
		return this.refreshTokenSubject.asObservable();
	}

	private callRefreshToken(): Observable<string>  {
		const refreshToken: string = localStorage.getItem('refreshtoken');
		if (canRefreshToken(refreshToken)) {
			return this.authenticationService.tokenRefresh(refreshToken);
		}
		return throwError(undefined);
	}

}
