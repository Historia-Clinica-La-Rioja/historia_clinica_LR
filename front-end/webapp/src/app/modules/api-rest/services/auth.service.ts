import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { LoginDto, JWTokenDto } from '@api-rest/api-model';
import { environment } from '@environments/environment';

const TOKEN_KEY = 'token';

@Injectable({
	providedIn: 'root'
})
export class AuthService {


	constructor(
		private http: HttpClient,
	) { }

	public login(loginDto: LoginDto): Observable<any> {
		return this.http.post<JWTokenDto>(`${environment.apiBase}/auth`, loginDto)
			.pipe(
				map(tokenDto => localStorage.setItem(TOKEN_KEY, tokenDto.token))
			);
	}

	public logout() {
		localStorage.removeItem(TOKEN_KEY);
	}

	loginChaco(code: string): Observable<JWTokenDto> {
		let queryParams: HttpParams = new HttpParams();
		queryParams = queryParams.append('code', code);
		return this.http.get<JWTokenDto>(`${environment.apiBase}/oauth/chaco`, {
			params: queryParams
		});
	}

	getRedirectUrl(): Observable<string> {
		return this.http.get<string>(`${environment.apiBase}/oauth/chaco/redirectUrl`,{responseType: 'text' as 'json'});
	}

}
