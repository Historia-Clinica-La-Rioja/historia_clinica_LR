import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { HierarchicalUnitStaffDto } from '@api-rest/api-model';
import { ContextService } from '@core/services/context.service';
import { environment } from '@environments/environment';
import { Observable } from 'rxjs';

@Injectable({
	providedIn: 'root'
})
export class HierarchicalUnitStaffService {

	private readonly URL_BASE = `${environment.apiBase}/institutions`;

	constructor(
		private http: HttpClient,
		private readonly contextService: ContextService
	) { }

	getByUserId(userId: number): Observable<HierarchicalUnitStaffDto[]> {
		const url = `${this.URL_BASE}/${this.contextService.institutionId}/hierarchicalunitstaff/user/${userId}`;
		return this.http.get<HierarchicalUnitStaffDto[]>(url);
	}

	update(userId: number, staff: HierarchicalUnitStaffDto[]): Observable<boolean> {
		const url = `${this.URL_BASE}/${this.contextService.institutionId}/hierarchicalunitstaff/user/${userId}`;
		return this.http.put<boolean>(url, staff);
	}
}
