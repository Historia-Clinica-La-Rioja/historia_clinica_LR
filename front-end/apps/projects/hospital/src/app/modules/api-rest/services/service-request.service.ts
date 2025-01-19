import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { ContextService } from '@core/services/context.service';
import { environment } from '@environments/environment';
import { Observable } from 'rxjs';
import {
	CompleteRequestDto,
	DiagnosticReportInfoDto, DiagnosticReportInfoWithFilesDto,
	PrescriptionDto,
	StudyOrderReportInfoDto,
	StudyTranscribedOrderReportInfoDto,
	StudyWithoutOrderReportInfoDto,
	TranscribedServiceRequestDto,
	TranscribedServiceRequestSummaryDto,
	AddDiagnosticReportObservationsCommandDto,
	GetDiagnosticReportObservationGroupDto,
} from '@api-rest/api-model';

import { switchMap } from 'rxjs/operators';
import { DownloadService } from '@core/services/download.service';

@Injectable({
	providedIn: 'root'
})
export class ServiceRequestService {

	constructor(
		private readonly http: HttpClient,
		private readonly contextService: ContextService,
		private readonly downloadService: DownloadService,
	) { }

	getList(patientId: number, statusId?: string, study?: string, healthCondition?: string, categoryId?: string, categoriesToBeExcluded?: string): Observable<DiagnosticReportInfoDto[]> {
		let queryParams: HttpParams = new HttpParams();
		queryParams = statusId ? queryParams.append('statusId', statusId) : queryParams;
		queryParams = study ? queryParams.append('study', study) : queryParams;
		queryParams = categoryId ? queryParams.append('category', categoryId) : queryParams;
		queryParams = healthCondition ? queryParams.append('healthCondition', healthCondition) : queryParams;
		queryParams = categoriesToBeExcluded ? queryParams.append('categoriesToBeExcluded', categoriesToBeExcluded) : queryParams;
		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/patient/${patientId}/service-requests/`;
		return this.http.get<DiagnosticReportInfoDto[]>(url, { params: queryParams });
	}

	getListByRoles(patientId: number, statusId: string, study: string, healthCondition: string, categoryId: string): Observable<DiagnosticReportInfoDto[]> {
		let queryParams: HttpParams = new HttpParams();
		queryParams = statusId ? queryParams.append('statusId', statusId) : queryParams;
		queryParams = study ? queryParams.append('study', study) : queryParams;
		queryParams = categoryId ? queryParams.append('category', categoryId) : queryParams;
		queryParams = healthCondition ? queryParams.append('healthCondition', healthCondition) : queryParams;
		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/patient/${patientId}/service-requests/`;
		return this.http.get<DiagnosticReportInfoDto[]>(url, { params: queryParams });
	}


	create(patientId: number, prescriptionDto: PrescriptionDto): Observable<number[]> {
		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/patient/${patientId}/service-requests`;
		return this.http.post<number[]>(url, prescriptionDto);
	}

	createTranscribedOrder(patientId: number, transcribedInfo: TranscribedServiceRequestDto){
		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/patient/${patientId}/service-requests/transcribed`;
		return this.http.post<number>(url, transcribedInfo)
	}

	deleteTranscribedOrder(patientId: number, orderId: number): Observable<void>{
		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/patient/${patientId}/service-requests/${orderId}/delete-transcribed`;
		return this.http.delete<void>(url)
	}

	getMedicalOrders(patientId: number, statusId: string, categoryId: string): Observable<DiagnosticReportInfoDto[]> {
		let queryParams: HttpParams = new HttpParams();
		queryParams = statusId ? queryParams.append('statusId', statusId) : queryParams;
		queryParams = categoryId ? queryParams.append('category', categoryId) : queryParams;
		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/patient/${patientId}/service-requests/medicalOrders`;
		return this.http.get<DiagnosticReportInfoDto[]>(url, { params: queryParams });
	}

	getTranscribedOrders(patientId: number): Observable<TranscribedServiceRequestSummaryDto[]>{
		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/patient/${patientId}/service-requests/transcribedOrders`;
		return this.http.get<TranscribedServiceRequestSummaryDto[]>(url)
	}

	saveAttachedFiles(patientId: number, serviceRequestId: number, selectedFiles: File[]): Observable<void> {
		const filesFormdata = new FormData();
		Array.from(selectedFiles).forEach(file => filesFormdata.append('files', file));
		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/patient/${patientId}/service-requests/${serviceRequestId}/uploadFiles`;
		return this.http.post<void>(url, filesFormdata)
	}

	complete(patientId: number, diagnosticReportId: number, completeRequestDto: CompleteRequestDto, files: File[]): Observable<void> {
		const commonUrl = `${environment.apiBase}/institutions/${this.contextService.institutionId}/patient/${patientId}/service-requests/${diagnosticReportId}`;
		const uploadFileUrl = commonUrl + '/uploadFile';
		const completeUrl = commonUrl + '/complete';
		const filesFormdata = new FormData();
		Array.from(files).forEach(file => filesFormdata.append('files', file));
		if(filesFormdata.get("files")){
			return this.http.post<number[]>(uploadFileUrl, filesFormdata).pipe(
				switchMap(fileIds => {
					completeRequestDto.fileIds = fileIds;
					return this.http.put<void>(completeUrl, completeRequestDto);
				}));
		}
		else
			return this.http.put<void>(completeUrl, completeRequestDto);
	}

	addObservations(patientId: number, diagnosticReportId: number, diagnosticReport: AddDiagnosticReportObservationsCommandDto): Observable<void> {
		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/patient/${patientId}/service-requests/${diagnosticReportId}/observations`;
		return this.http.put<void>(url, diagnosticReport);
	}

	completeByRdi(patientId: number, appointmentId: number): Observable<void> {
		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/patient/${patientId}/service-requests/${appointmentId}/completeByRDI`
		return this.http.put<void>(url, {})
	}

	delete(patientId: number, serviceRequestId: number): Observable<string> {
		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/patient/${patientId}/service-requests/${serviceRequestId}`;
		return this.http.delete<string>(url);
	}

	get(patientId: number, diagnosticReportId: number): Observable<DiagnosticReportInfoWithFilesDto> {
		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/patient/${patientId}/service-requests/${diagnosticReportId}`;
		return this.http.get<DiagnosticReportInfoWithFilesDto>(url);
	}

	getObservations(patientId: number, diagnosticReportId: number): Observable<GetDiagnosticReportObservationGroupDto> {
		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/patient/${patientId}/service-requests/${diagnosticReportId}/observations`;
		return this.http.get<GetDiagnosticReportObservationGroupDto>(url);
	}

	download(patientId: number, fileId: number, fileName: string) {
		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/patient/${patientId}/service-requests/download/${fileId}`;
		this.downloadService.downloadPdf(
			url,
			fileName,
		).subscribe();
	}

	downloadPdf(patientId: number, serviceRequestId: number) {
		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/patient/${patientId}/service-requests/${serviceRequestId}/download-pdf`;
		this.downloadService.fetchFile(url, 'Orden ' + serviceRequestId);
	}

	downloadTranscribedOrderPdf(patientId: number, serviceRequestId: number, appointmentId: number) {
		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/patient/${patientId}/service-requests/transcribed/${serviceRequestId}/download-pdf`;
		this.downloadService.fetchFile(url, 'Orden ' + serviceRequestId, { appointmentId: appointmentId.toString() });
	}

	getStudyTranscribedOrder(patientId: number): Observable<StudyTranscribedOrderReportInfoDto[]>
	{
		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/patient/${patientId}/service-requests/studyTranscribedOrder`;
		return this.http.get<StudyTranscribedOrderReportInfoDto[]>(url)
	}

	getStudyWithoutOrder(patientId: number): Observable<StudyWithoutOrderReportInfoDto[]>
	{
		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/patient/${patientId}/service-requests/studyWithoutOrder`;
		return this.http.get<StudyWithoutOrderReportInfoDto[]>(url)
	}

	getListStudyOrder(patientId: number):Observable<StudyOrderReportInfoDto[]>
	{
		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/patient/${patientId}/service-requests/studyOrder`;
		return this.http.get<StudyOrderReportInfoDto[]>(url)
	}
}
