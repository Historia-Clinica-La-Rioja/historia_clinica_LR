import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

import {LoginDto, OauthConfigDto, PasswordDto} from '@api-rest/api-model';
import { environment } from '@environments/environment';

@Injectable({
	providedIn: 'root'
})
export class AuthService {

	constructor(
		private readonly http: HttpClient,
	) { }

	public login(loginDto: LoginDto, recaptchaResponse): Observable<any> {
		// limpio el cache tg-718
		localStorage.clear();
		let httpHeaders;
		if (recaptchaResponse) {
			httpHeaders = new HttpHeaders({
				recaptcha: recaptchaResponse
			});
		}
		return this.http.post<any>(`${environment.apiBase}/auth`, loginDto, {headers: httpHeaders});
	}

	tokenRefresh(): Observable<any> {
		return this.http.post<any>(`${environment.apiBase}/auth/refresh`, undefined);
	}

	logout(): void {
		this.http.delete<any>(`${environment.apiBase}/auth/refresh`).subscribe();
	}

	completeLoginWith2FA(code: string): Observable<any> {
		return this.http.post<any>(`${environment.apiBase}/auth/login-2fa`, { code });
	}

	getRedirectUrl(): Observable<string> {
		return this.http.get<string>(`${environment.apiBase}/oauth/redirectUrl`, { responseType: 'text' as 'json' });
	}

	getOauthConfig(): Observable<OauthConfigDto> {
		return this.http.get<OauthConfigDto>(`${environment.apiBase}/oauth/config`);
	}

	updatePassword(newPasword: PasswordDto): Observable<any>{
		return this.http.patch<void>(`${environment.apiBase}/passwords`, newPasword);
	}

}
