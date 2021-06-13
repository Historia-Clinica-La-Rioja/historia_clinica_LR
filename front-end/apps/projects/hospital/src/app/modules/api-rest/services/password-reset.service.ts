import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '@environments/environment';
import { Observable } from 'rxjs';
import { UserDto, PasswordResetDto } from '@api-rest/api-model';

@Injectable({
	providedIn: 'root'
})
export class PasswordResetService {

	constructor(
		private http: HttpClient,
	) { }

	public setPassword(passwordReset: PasswordResetDto): Observable<UserDto> {
		return this.http.post<UserDto>(`${environment.apiBase}/password-reset`, passwordReset);
	}
}
