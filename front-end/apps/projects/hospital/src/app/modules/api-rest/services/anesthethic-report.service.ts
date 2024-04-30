import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { AnestheticReportDto } from '@api-rest/api-model';
import { ContextService } from '@core/services/context.service';
import { environment } from '@environments/environment';
import { Observable } from 'rxjs';

@Injectable({
	providedIn: 'root'
})
export class AnesthethicReportService {

	private readonly BASIC_URL = `${environment.apiBase}/institutions/${this.contextService.institutionId}/internments`;
	constructor(
		private http: HttpClient,
		private contextService: ContextService,
	) { }


	createAnestheticReport(anesthethicReport: AnestheticReportDto, internmentEpisodeId: number): Observable<AnestheticReportDto> {
		const url = `${this.BASIC_URL}/${internmentEpisodeId}/anesthetic-report/close`;
		return this.http.post<AnestheticReportDto>(url, anesthethicReport);
	}

	createAnestheticReportDraft(anesthethicReportDraft: AnestheticReportDto, internmentEpisodeId: number): Observable<AnestheticReportDto> {
		const url = `${this.BASIC_URL}/${internmentEpisodeId}/anesthetic-report/draft`;
		return this.http.post<AnestheticReportDto>(url, anesthethicReportDraft);
	}

	getAnestheticReport(documentId: number, internmentEpisodeId: number): Observable<AnestheticReportDto> {
		const url = `${this.BASIC_URL}/${internmentEpisodeId}/anesthetic-report/${documentId}`;
		return this.http.get<AnestheticReportDto>(url)
	}
}
