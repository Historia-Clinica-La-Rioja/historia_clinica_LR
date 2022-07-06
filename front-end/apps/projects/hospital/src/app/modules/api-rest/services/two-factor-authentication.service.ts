import { Injectable } from '@angular/core';
import { HttpClient } from "@angular/common/http";
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

	confirmTwoFactorAuthenticationCode(verificationCode): Observable<boolean> {
		const url = `${environment.apiBase}/2fa/confirm`;
		return this.http.post<boolean>(url, { code: verificationCode });
	}

	loggedUserHasTwoFactorAuthenticationEnabled(): Observable<boolean> {
		const url = `${environment.apiBase}/2fa/enabled-for-logged-user`;
		return this.http.get<boolean>(url);
	}

}
