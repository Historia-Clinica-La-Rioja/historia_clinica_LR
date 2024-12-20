import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { PageDto, ReferenceCompleteDataDto, ReferenceDto, ReferenceReportDto } from '@api-rest/api-model';
import { ContextService } from '@core/services/context.service';
import { environment } from '@environments/environment';
import { DashboardFilters } from '@shared-appointment-access-management/components/reference-dashboard-filters/reference-dashboard-filters.component';
import { Observable } from 'rxjs';

@Injectable({
	providedIn: 'root'
})
export class InstitutionalReferenceReportService {

	private readonly BASE_URL: string;
	private readonly PREFIX_URL: string;

	constructor(
		private readonly http: HttpClient,
		private readonly contextService: ContextService,
	) {
		this.BASE_URL = `${environment.apiBase}/institutions`;
		this.PREFIX_URL = `references-report`;
	}

	getAllReceivedReferences(filters: DashboardFilters, pageSize: number, pageNumber: number): Observable<PageDto<ReferenceReportDto>> {
		const url = `${this.BASE_URL}/${this.contextService.institutionId}/${this.PREFIX_URL}/received`;
		let params: HttpParams = new HttpParams();
		params = params.append('filter', JSON.stringify(filters));
		params = params.append('pageNumber', pageNumber);
		params = params.append('pageSize', pageSize);
		return this.http.get<PageDto<ReferenceReportDto>>(url, { params });
	}

	getAllRequestedReferences(filters: DashboardFilters, pageSize: number, pageNumber: number): Observable<PageDto<ReferenceReportDto>> {
		const url = `${this.BASE_URL}/${this.contextService.institutionId}/${this.PREFIX_URL}/requested`;
		let params: HttpParams = new HttpParams();
		params = params.append('filter', JSON.stringify(filters));
		params = params.append('pageNumber', pageNumber);
		params = params.append('pageSize', pageSize);
		return this.http.get<PageDto<ReferenceReportDto>>(url, { params });
	}

	getReferenceDetail(id: number): Observable<ReferenceCompleteDataDto> {
		const url = `${this.BASE_URL}/${this.contextService.institutionId}/${this.PREFIX_URL}/reference-detail/${id}`;
		return this.http.get<ReferenceCompleteDataDto>(url);
	}

	addObservation(referenceId: number, observation: string): Observable<Object> {
		let queryParams: HttpParams = new HttpParams();
		queryParams = (observation) ? queryParams.append('observation', observation) : queryParams;
		const url = `${this.BASE_URL}/${this.contextService.institutionId}/references-report/${referenceId}/add-observation`;
		return this.http.post<boolean>(url, {}, { params: queryParams });
	}

	changeReferenceRegulationStateAsGestorInstitucional(referenceId: number, stateId: number, reason?: string): Observable<boolean> {
		const url = `${this.BASE_URL}/${this.contextService.institutionId}/references-report/${referenceId}/change-regulation-state`;
		let params: HttpParams = new HttpParams();
		params = params.append('stateId', stateId);
		if (!reason)
			return this.http.put<boolean>(url, null, { params });

		params = params.append('reason', reason);
		return this.http.put<boolean>(url, null, { params });
	}

	changeReferenceApprovalStateAsGestorInstitucional(referenceId: number, stateId: number, reason?: string): Observable<boolean> {
		const url = `${this.BASE_URL}/${this.contextService.institutionId}/references-report/${referenceId}/change-administrative-state`;
		let params: HttpParams = new HttpParams();
		params = params.append('stateId', stateId);
		if (!reason)
			return this.http.put<boolean>(url, null, { params });

		params = params.append('reason', reason);
		return this.http.put<boolean>(url, null, { params }); 
	}

	cancelReference(referenceId: number): Observable<boolean> {
		const url = `${this.BASE_URL}/${this.contextService.institutionId}/references-report/${referenceId}/cancel`;
		return this.http.put<boolean>(url, {});
	}

	modifyReference(referenceId: number, reference: ReferenceDto): Observable<boolean> {
		const url = `${this.BASE_URL}/${this.contextService.institutionId}/references-report/${referenceId}/modify`;
		return this.http.put<boolean>(url, reference);
	}

	modifyReferenceAsGestor(referenceId: number, fileIds: number[]): Observable<boolean> {
		const url = `${this.BASE_URL}/${this.contextService.institutionId}/reference/${referenceId}/update-by-manager`;
		return this.http.put<boolean>(url, {}, { 
			params: {
				fileIds: fileIds
			}
		});
	}

	modifyReferenceWidhInstitutionAsGestor(referenceId: number, destinationInstitutionId: number, fileIds: number[]): Observable<boolean> {
		const url = `${this.BASE_URL}/${this.contextService.institutionId}/reference/${referenceId}/update-by-manager`;
		return this.http.put<boolean>(url, {}, { 
			params: {
				destinationInstitutionId: destinationInstitutionId,
				fileIds: fileIds
			}
		});
	}
}
