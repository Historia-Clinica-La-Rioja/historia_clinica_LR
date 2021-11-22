import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {ConsultationsDto} from "@api-rest/api-model";
import {Observable} from "rxjs";
import {environment} from "@environments/environment";
import {ContextService} from "@core/services/context.service";
import {DateFormat, momentFormat, momentParseDate} from "@core/utils/moment.utils";
import {DownloadService} from "@core/services/download.service";

@Injectable({
	providedIn: 'root'
})
export class PatientReportsService {

	private URL_PREFIX = `${environment.apiBase}/reports/${this.contextService.institutionId}/`;

	constructor(private http: HttpClient, private readonly contextService: ContextService, private readonly downloadService: DownloadService) {
	}

	getFormPdf(consultation: ConsultationsDto, patientName: string): Observable<any> {
		const pdfPrefixName = 'FormularioV';
		const url = this.URL_PREFIX + 'consultation-formv';
		return this.getOutpatientConsultationReport(url, consultation, pdfPrefixName, patientName);
	}

	getAnnexPdf(consultation: ConsultationsDto, patientName: string): Observable<any> {
		const pdfPrefixName = 'AnexoII';
		const url = this.URL_PREFIX + 'consultations-annex';
		return this.getOutpatientConsultationReport(url, consultation, pdfPrefixName, patientName);
	}

	getOutpatientConsultationReport(url: string, consultationDto: ConsultationsDto, pdfPrefixName: string, patientName: string): Observable<any> {
		const consultationDate: string = momentFormat(momentParseDate(String(consultationDto.consultationDate)), DateFormat.FILE_DATE);
		const pdfName = pdfPrefixName + `_${patientName}_${consultationDate}_${consultationDto.completeProfessionalName}`;
		const documentId = consultationDto.documentId;
		return this.downloadService.downloadPdfWithRequestParams(url, pdfName, {documentId});
	}

	getConsultations(patientId: number): Observable<ConsultationsDto[]> {
		const url = `${environment.apiBase}/reports/institution/${this.contextService.institutionId}/patient/${patientId}/consultations-list`;
		return this.http.get<ConsultationsDto[]>(url);
	}

}
