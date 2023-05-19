import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { ContextService } from '@core/services/context.service';
import { environment } from '@environments/environment';
import { Observable } from 'rxjs';
import {
	CompleteRequestDto,
	DiagnosticReportInfoDto, DiagnosticReportInfoWithFilesDto,
	PrescriptionDto,
	SnomedDto,
	TranscribedPrescriptionDto,
} from '@api-rest/api-model';

import { switchMap } from 'rxjs/operators';
import { ViewPdfService } from '@presentation/dialogs/view-pdf/view-pdf.service';
import {DownloadService} from "@core/services/download.service";

@Injectable({
	providedIn: 'root'
})
export class ServiceRequestService {

	constructor(
		private readonly http: HttpClient,
		private readonly contextService: ContextService,
		private readonly viewPdfService: ViewPdfService,
		private readonly downloadService: DownloadService,
	) { }

	getList(patientId: number, statusId: string, study: string, healthCondition: string, categoryId: string): Observable<DiagnosticReportInfoDto[]> {
		let queryParams: HttpParams = new HttpParams();
		queryParams = statusId ? queryParams.append('statusId', statusId) : queryParams;
		queryParams = study ? queryParams.append('study', study) : queryParams;
		queryParams = categoryId ? queryParams.append('category', categoryId) : queryParams;
		queryParams = healthCondition ? queryParams.append('healthCondition', healthCondition) : queryParams;
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

	getStudyStatus(patientId: number, serviceRequestId: number): Observable<boolean>{
		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/patient/${patientId}/service-requests/${serviceRequestId}/existCheck`;
		return this.http.get<boolean>(url);
	}

	create(patientId: number, prescriptionDto: PrescriptionDto): Observable<number[]> {
		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/patient/${patientId}/service-requests`;
		return this.http.post<number[]>(url, prescriptionDto);
	}

	createTranscribedOrder(patientId: number, study: SnomedDto, healthCondition: SnomedDto, professional: string, institution: string){
		let data: TranscribedPrescriptionDto = {
			study,
			healthCondition,
			healthcareProfessionalName: professional,
			institutionName: institution
		}
		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/patient/${patientId}/service-requests/transcribed`;
		return this.http.post<number>(url, data)
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

	delete(patientId: number, serviceRequestId: number): Observable<string> {
		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/patient/${patientId}/service-requests/${serviceRequestId}`;
		return this.http.delete<string>(url);
	}

	get(patientId: number, diagnosticReportId: number): Observable<DiagnosticReportInfoWithFilesDto> {
		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/patient/${patientId}/service-requests/${diagnosticReportId}`;
		return this.http.get<DiagnosticReportInfoWithFilesDto>(url);
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
		this.viewPdfService.showDialog(url, 'Orden ' + serviceRequestId);
	}

}
