import { DashboardFilters } from '@access-management/components/reference-dashboard-filters/reference-dashboard-filters.component';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { PageDto, ReferenceReportDto } from '@api-rest/api-model';
import { Observable } from 'rxjs';
import { environment } from '@environments/environment';

@Injectable({
	providedIn: 'root'
})
export class InstitutionalNetworkReferenceReportService {

	private readonly BASE_URL = `${environment.apiBase}/references-report`;

	constructor(
		private readonly http: HttpClient,
	) { }

	getReferencesByManagerRole(filters: DashboardFilters, pageSize: number, pageNumber: number): Observable<PageDto<ReferenceReportDto>> {
		const url = `${this.BASE_URL}/manager`;
		let params: HttpParams = new HttpParams();
		params = params.append('filter', JSON.stringify(filters));
		params = params.append('pageNumber', pageNumber);
		params = params.append('pageSize', pageSize);
		return this.http.get<PageDto<ReferenceReportDto>>(url, { params });
	}
}
