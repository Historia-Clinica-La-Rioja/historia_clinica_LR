import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { AnestheticReportDto, PostCloseAnestheticReportDto } from '@api-rest/api-model';
import { ContextService } from '@core/services/context.service';
import { environment } from '@environments/environment';
import { Observable } from 'rxjs';

@Injectable({
	providedIn: 'root'
})
export class AnesthethicReportService {

	private readonly BASIC_URL = `${environment.apiBase}/institutions/${this.contextService.institutionId}/anesthetic-report`;
	constructor(
		private http: HttpClient,
		private contextService: ContextService,
	) { }


	createAnestheticReport(anesthethicReport: AnestheticReportDto): Observable<AnestheticReportDto> {
		const url = `${this.BASIC_URL}/close`;
		return this.http.post<AnestheticReportDto>(url, anesthethicReport);
	}

	createAnestheticReportDraft(anesthethicReportDraft: AnestheticReportDto): Observable<AnestheticReportDto> {
		const url = `${this.BASIC_URL}/draft`;
		return this.http.post<AnestheticReportDto>(url, anesthethicReportDraft);
	}

	getAnestheticReport(documentId: number): Observable<AnestheticReportDto> {
		const url = `${this.BASIC_URL}/by-document/${documentId}`;
		return this.http.get<AnestheticReportDto>(url)
	}

    editAnestheticReport(anestheticReport: PostCloseAnestheticReportDto): Observable<AnestheticReportDto> {
        const url = `${this.BASIC_URL}/close`;
		return this.http.put<AnestheticReportDto>(url, anestheticReport);
    }

    deleteAnestheticReport(documentId: number, modificationReason: string): Observable<boolean> {
        const url = `${this.BASIC_URL}/by-document/${documentId}`;
		return this.http.delete<boolean>(url, { 
            body: { modificationReason }
        });
    }
}
