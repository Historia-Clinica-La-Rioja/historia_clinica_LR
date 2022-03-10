import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { PermissionsDto, LoggedUserDto } from '@api-rest/api-model';
import { HttpClient } from '@angular/common/http';

import { environment } from '@environments/environment';
@Injectable({
  providedIn: 'root'
})
export class AccountService {

	constructor(
		private readonly http: HttpClient,
	) { }

	public getPermissions(): Observable<PermissionsDto> {
		return this.http.get<PermissionsDto>(`${environment.apiBase}/account/permissions`);
	}

	public getInfo(): Observable<LoggedUserDto> {
		return this.http.get<LoggedUserDto>(`${environment.apiBase}/account/info`);
	}

}
