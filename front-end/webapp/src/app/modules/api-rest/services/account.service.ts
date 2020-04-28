import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { PermissionsDto } from '@api-rest/api-model';
import { HttpClient } from '@angular/common/http';

import { environment } from '@environments/environment';
@Injectable({
  providedIn: 'root'
})
export class AccountService {

	constructor(
		private http: HttpClient,
	) { }

	public getPermissions(): Observable<PermissionsDto> {
		return this.http.get<PermissionsDto>(`${environment.apiBase}/account/permissions`);
	}

}
