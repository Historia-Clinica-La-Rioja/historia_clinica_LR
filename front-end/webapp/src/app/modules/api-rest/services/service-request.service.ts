import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {ContextService} from '@core/services/context.service';
import {environment} from '@environments/environment';
import {Observable} from 'rxjs';
import {
	CompleteRequestDto,
	DiagnosticReportInfoDto,
	PrescriptionDto
} from '@api-rest/api-model';
import {flatMap, map} from 'rxjs/operators';

@Injectable({
	providedIn: 'root'
})
export class ServiceRequestService {

	constructor(
		private readonly http: HttpClient,
		private readonly contextService: ContextService
	) {
	}

	getList(patientId: number, statusId: string, diagnosticReport: string, healthCondition: string): Observable<DiagnosticReportInfoDto[]> {
		let queryParams: HttpParams = new HttpParams();
		statusId ? queryParams = queryParams.append('statusId', statusId) : queryParams
		diagnosticReport ? queryParams = queryParams.append('diagnosticReport', diagnosticReport) : queryParams
		healthCondition ? queryParams = queryParams.append('healthCondition', healthCondition) : queryParams
		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/patient/${patientId}/service-requests/`;
		return this.http.get<DiagnosticReportInfoDto[]>(url, {params: queryParams});
	}

	create(patientId: number, prescriptionDto: PrescriptionDto): Observable<number[]> {
		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/patient/${patientId}/service-requests`;
		return this.http.post<number[]>(url, prescriptionDto);
	}


	download(patientId: number, serviceRequestId: number): Observable<string> {
		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/patient/${patientId}/service-requests/${serviceRequestId}/downloadFile`;
		return this.http.get<string>(url);
	}


	complete(patientId: number, diagnosticReportId: number, completeRequestDto: CompleteRequestDto, files: File[]): Observable<void> {
		const commonUrl = `${environment.apiBase}/institutions/${this.contextService.institutionId}/patient/${patientId}/service-requests/${diagnosticReportId}`;
		const uploadFileUrl = commonUrl + '/uploadFile';
		const completeUrl = commonUrl + '/complete';
		const filesFormdata = new FormData();
		files.forEach(file => filesFormdata.append('files', file));
		return this.http.post<number[]>(uploadFileUrl, filesFormdata).pipe(
			flatMap(fileIds => {
				completeRequestDto.fileIds = fileIds;
				return this.http.put<void>(completeUrl, completeRequestDto);
			}));
	}


	delete(patientId: number, serviceRequestId: number): Observable<string> {
		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/patient/${patientId}/service-requests/${serviceRequestId}`;
		return this.http.delete<string>(url);
	}

	get(patientId: number, serviceRequestId: number): Observable<DiagnosticReportInfoDto> {
		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/patient/${patientId}/service-requests/${serviceRequestId}`;
		return this.http.get<DiagnosticReportInfoDto>(url);
	}
}
