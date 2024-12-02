import { DashboardFilters } from '@shared-appointment-access-management/components/reference-dashboard-filters/reference-dashboard-filters.component';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { PageDto, ReferenceCompleteDataDto, ReferenceReportDto } from '@api-rest/api-model';
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

	getReferenceDetail(id: number): Observable<ReferenceCompleteDataDto> {
		const url = `${this.BASE_URL}/reference-detail/${id}`;
		return this.http.get<ReferenceCompleteDataDto>(url);
	}

	changeReferenceRegulationState(referenceId: number, stateId: number, reason?: string): Observable<boolean> {
		const url = `${this.BASE_URL}/${referenceId}/change-state`;
		let params: HttpParams = new HttpParams();
		params = params.append('stateId', stateId);
		if (!reason)
			return this.http.post<boolean>(url, null, { params });

		params = params.append('reason', reason);
		return this.http.post<boolean>(url, null, { params });
	}

	changeReferenceApprovalState(referenceId: number, stateId: number, reason?: string): Observable<boolean> {
		const url = `${this.BASE_URL}/${referenceId}/change-administrative-state`;
		let params: HttpParams = new HttpParams();
		params = params.append('stateId', stateId);
		if (!reason)
			return this.http.put<boolean>(url, null, { params });

		params = params.append('reason', reason);
		return this.http.put<boolean>(url, null, { params });
	}

	addObservation(referenceId: number, observation: string): Observable<Object> {
		const url = `${this.BASE_URL}/${referenceId}/add-observation`;
		let queryParams: HttpParams = new HttpParams();
		queryParams = (observation) ? queryParams.append('observation', observation) : queryParams;
		return this.http.post<boolean>(url, {}, { params: queryParams });
	}

	addDerivation(referenceId: number, derivation: string): Observable<boolean> {
		const url = `${this.BASE_URL}/forwarding/${referenceId}`;
		let queryParams: HttpParams = new HttpParams();
		queryParams = (derivation) ? queryParams.append('observation', derivation) : queryParams;
		return this.http.post<boolean>(url, {}, { params: queryParams });
	}

	updateDerivation(forwardingId: number, derivation: string): Observable<boolean> {
		const url = `${this.BASE_URL}/update-forwarding/${forwardingId}`;
		let queryParams: HttpParams = new HttpParams();
		queryParams = (derivation) ? queryParams.append('observation', derivation) : queryParams;
		return this.http.put<boolean>(url, {}, { params: queryParams });
	}

}
