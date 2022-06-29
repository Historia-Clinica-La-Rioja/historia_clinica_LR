import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from "@angular/common/http";
import { Observable } from "rxjs";
import { environment } from "@environments/environment";
import { TwoFactorAuthenticationDto } from "@api-rest/api-model";

@Injectable({
  providedIn: 'root'
})
export class TwoFactorAuthenticationService {

	constructor(private http: HttpClient) {
	}

	generateTwoFactorAuthenticationCodes(): Observable<TwoFactorAuthenticationDto> {
		const url = `${environment.apiBase}/2fa`;
		return this.http.post<TwoFactorAuthenticationDto>(url, {});
	}

	confirmTwoFactorAuthenticationCode(verificationCode): Observable<Boolean> {
		const url = `${environment.apiBase}/2fa/confirm`;
		let params: HttpParams = new HttpParams();
		params = params.append('code', verificationCode);
		return this.http.post<Boolean>(url, {}, {params});
	}

}
