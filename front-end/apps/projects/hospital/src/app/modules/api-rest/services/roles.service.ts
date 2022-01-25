import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { RoleDto, UserRoleDto } from '@api-rest/api-model';
import { Observable } from "rxjs";
import { environment } from "@environments/environment";
import { ContextService } from '@core/services/context.service';

@Injectable({
	providedIn: 'root'
})
export class RolesService {

	constructor(private http: HttpClient, private contextService: ContextService) { }

	getAllInstitutionalRoles(): Observable<RoleDto[]> {
		const url = `${environment.apiBase}/roles`;
		return this.http.get<RoleDto[]>(url);
	}


	getRolesByUser(userId: number): Observable<UserRoleDto[]> {
		const url = `${environment.apiBase}/user-role/institution/${this.contextService.institutionId}/user/${userId}`;
		return this.http.get<UserRoleDto[]>(url);
	}

	hasBackofficeRole(userId: number): Observable<boolean> {
		const url = `${environment.apiBase}/user-role/institution/${this.contextService.institutionId}/user/${userId}/has-backoffice-role`;
		return this.http.get<boolean>(url);
	}
	updateRoles(userId: number, userRole: UserRoleDto []): Observable<boolean> {
		const url = `${environment.apiBase}/user-role/institution/${this.contextService.institutionId}/user/${userId}`;
		return this.http.put<boolean>(url, userRole);
	}
}
