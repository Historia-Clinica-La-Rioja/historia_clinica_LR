import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {ContextService} from '@core/services/context.service';
import {environment} from '@environments/environment';
import {Observable} from 'rxjs';
import {CompleteRequestDto, DiagnosticReportDto, NewServiceRequestListDto} from '@api-rest/api-model';

@Injectable({
	providedIn: 'root'
})
export class ServiceRequestService {

	constructor(
		private readonly http: HttpClient,
		private readonly contextService: ContextService
	) {
	}

	getList(patientId: number, serviceRequestId: number, status: string, serviceRequest: string, healthCondition: string): Observable<DiagnosticReportDto[]> {
		let queryParams: HttpParams = new HttpParams();
		queryParams = queryParams.append('statusId', status);
		queryParams = queryParams.append('serviceRequest', serviceRequest);
		queryParams = queryParams.append('healthCondition', healthCondition);

		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/patient/${patientId}/service-requests/${serviceRequestId}`;
		return this.http.get<DiagnosticReportDto[]>(url, {params: queryParams});
	}

	newServiceRequest(patientId: number, serviceRequestListDto: NewServiceRequestListDto): Observable<number> {
		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/patient/${patientId}/service-requests`;
		return this.http.post<number>(url, serviceRequestListDto);
	}


	download(patientId: number, serviceRequestId: number): Observable<string> {
		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/patient/${patientId}/service-requests/${serviceRequestId}/downloadFile`;
		return this.http.get<string>(url);
	}


	complete(patientId: number, serviceRequestId: number, completeRequestDto: CompleteRequestDto): Observable<string> {
		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/patient/${patientId}/service-requests/${serviceRequestId}`;
		return this.http.put<string>(url, completeRequestDto);
	}


	delete(patientId: number, serviceRequestId: number): Observable<string> {
		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/patient/${patientId}/service-requests/${serviceRequestId}`;
		return this.http.delete<string>(url);
	}

	get(patientId: number, serviceRequestId: number): Observable<DiagnosticReportDto> {
		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/patient/${patientId}/service-requests/${serviceRequestId}`;
		return this.http.get<DiagnosticReportDto>(url);
	}
}
