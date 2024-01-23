import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { SharedSnomedDto } from '@api-rest/api-model';
import { ContextService } from '@core/services/context.service';
import { environment } from '@environments/environment';
import { Observable } from 'rxjs/internal/Observable';

@Injectable({
	providedIn: 'root'
})
export class PracticesService {

	private readonly PREFIX_URL = `${environment.apiBase}/practices`;

	constructor(
		private readonly http: HttpClient,
		private readonly contextService: ContextService,
	) { }

	get(institutionId?: number): Observable<SharedSnomedDto[]> {
		const institutionIdPathVariable = institutionId || this.contextService.institutionId;
		const url = `${this.PREFIX_URL}/institution/${institutionIdPathVariable}/by-institution`;
		return this.http.get<SharedSnomedDto[]>(url);
	}

	getByActiveDiaries(): Observable<SharedSnomedDto[]> {
		const url = `${this.PREFIX_URL}/institution/${this.contextService.institutionId}/by-active-diaries`;
		return this.http.get<SharedSnomedDto[]>(url);
	}

	getPracticesFromInstitutions(): Observable<SharedSnomedDto[]> {
		const url = `${this.PREFIX_URL}/institution/${this.contextService.institutionId}`;
		return this.http.get<SharedSnomedDto[]>(url);
	}

	getAll(careLineId?: number): Observable<SharedSnomedDto[]> {
		const url = `${environment.apiBase}/practices`;
		if (!careLineId)
			return this.http.get<SharedSnomedDto[]>(url)
		const params: HttpParams = new HttpParams().append('careLineId', careLineId);
		return this.http.get<SharedSnomedDto[]>(url, { params });
	}

	getAllByDepartment(departmentId: number): Observable<SharedSnomedDto[]> {
		const url =  `${environment.apiBase}/practices/department/${departmentId}`;
		return this.http.get<SharedSnomedDto[]>(url);
	}
}
