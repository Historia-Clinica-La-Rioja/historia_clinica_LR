import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { LoginDto, JWTokenDto, OauthConfigDto } from '@api-rest/api-model';
import { environment } from '@environments/environment';

const TOKEN_KEY = 'token';

@Injectable({
	providedIn: 'root'
})
export class AuthService {


	constructor(
		private readonly http: HttpClient,
	) { }

	public login(loginDto: LoginDto, recaptchaResponse): Observable<any> {
		let httpHeaders;
		if(recaptchaResponse) {
			httpHeaders = new HttpHeaders({
				recaptcha: recaptchaResponse
			});
		}
		return this.http.post<JWTokenDto>(`${environment.apiBase}/auth`, loginDto, {headers: httpHeaders})
			.pipe(
				map(tokenDto => localStorage.setItem(TOKEN_KEY, tokenDto.token))
			);
	}

	public logout() {
		localStorage.removeItem(TOKEN_KEY);
	}

	loginOauth(code: string): Observable<JWTokenDto> {
		let queryParams: HttpParams = new HttpParams();
		queryParams = queryParams.append('code', code);
		return this.http.get<JWTokenDto>(`${environment.apiBase}/oauth/login`, {
			params: queryParams
		});
	}

	getRedirectUrl(): Observable<string> {
		return this.http.get<string>(`${environment.apiBase}/oauth/redirectUrl`, { responseType: 'text' as 'json' });
	}

	getOauthConfig(): Observable<OauthConfigDto> {
		return this.http.get<OauthConfigDto>(`${environment.apiBase}/oauth/config`);
	}

}
