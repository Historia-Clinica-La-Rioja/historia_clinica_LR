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

	getFormPdf(outpatientConsultation: ConsultationsDto, patientName: string): Observable<any> {
		const pdfPrefixName = "FormularioV";
		const url = this.URL_PREFIX + "outpatient-formv";
		return this.getOutpatientConsultationReport(url, outpatientConsultation, pdfPrefixName, patientName);
	}

	getOutpatientConsultationReport(url: string, outpatientConsultation: ConsultationsDto, pdfPrefixName: string, patientName: string): Observable<any> {
		const consultationDate: string = momentFormat(momentParseDate(String(outpatientConsultation.consultationDate)), DateFormat.FILE_DATE);
		const pdfName = pdfPrefixName + `_${consultationDate}_${patientName}_${outpatientConsultation.completeProfessionalName}`;
		const outpatientId = outpatientConsultation.id;
		return this.downloadService.downloadPdfWithRequestParams(url, pdfName, {outpatientId});
	}

}
