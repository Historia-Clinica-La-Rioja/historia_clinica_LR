import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map, tap } from 'rxjs/operators';

import {LoginDto, JWTokenDto, OauthConfigDto, RefreshTokenDto, PasswordDto} from '@api-rest/api-model';
import { environment } from '@environments/environment';

const TOKEN_KEY = 'token';
const REFRESH_TOKEN_KEY = 'refreshtoken';

@Injectable({
	providedIn: 'root'
})
export class AuthService {


	constructor(
		private readonly http: HttpClient,
	) { }

	public login(loginDto: LoginDto, recaptchaResponse): Observable<any> {
		let httpHeaders;
		if (recaptchaResponse) {
			httpHeaders = new HttpHeaders({
				recaptcha: recaptchaResponse
			});
		}
		return this.http.post<JWTokenDto>(`${environment.apiBase}/auth`, loginDto, {headers: httpHeaders})
			.pipe(
				map(tokens => this.storeTokens(tokens))
			);
	}

	tokenRefresh(refreshToken: string): Observable<string> {
		const body: RefreshTokenDto = { refreshToken };
		return this.http.post<JWTokenDto>(`${environment.apiBase}/auth/refresh`, body).pipe(
			tap(tokens => this.storeTokens(tokens)),
			map(tokens => tokens.token),
		);
	}



	logout() {
		localStorage.removeItem(TOKEN_KEY);
		localStorage.removeItem(REFRESH_TOKEN_KEY);
	}

	loginOauth(code: string): Observable<JWTokenDto> {
		let queryParams: HttpParams = new HttpParams();
		queryParams = queryParams.append('code', code);
		return this.http.get<JWTokenDto>(`${environment.apiBase}/oauth/login`, {
			params: queryParams
		});
	}

	completeLoginWith2FA(code: string): Observable<any> {
		return this.http.post<JWTokenDto>(`${environment.apiBase}/auth/login-2fa`, { code : code }).pipe(
			map(tokens => this.storeTokens(tokens))
		);
	}

	getRedirectUrl(): Observable<string> {
		return this.http.get<string>(`${environment.apiBase}/oauth/redirectUrl`, { responseType: 'text' as 'json' });
	}

	getOauthConfig(): Observable<OauthConfigDto> {
		return this.http.get<OauthConfigDto>(`${environment.apiBase}/oauth/config`);
	}

	private storeTokens(tokens: JWTokenDto) {
		localStorage.setItem(TOKEN_KEY, tokens.token);
		if (tokens.refreshToken) {
			localStorage.setItem(REFRESH_TOKEN_KEY, tokens.refreshToken);
		}
	}

	updatePassword(newPasword: PasswordDto): Observable<any>{
		return this.http.patch<void>(`${environment.apiBase}/passwords`, newPasword);
	}

}
