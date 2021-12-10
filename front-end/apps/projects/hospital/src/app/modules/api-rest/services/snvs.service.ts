import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { SnvsReportDto, SnvsToReportDto } from '@api-rest/api-model';
import { ContextService } from '@core/services/context.service';
import { Observable } from 'rxjs';
import { environment } from '@environments/environment';

@Injectable({
	providedIn: 'root'
})
export class SnvsService {

	private readonly URL_BASE: string;

	constructor(
		private readonly http: HttpClient,
		private readonly contextService: ContextService
	) {
		this.URL_BASE = `${environment.apiBase}/institutions/${this.contextService.institutionId}/patient`;
	}

	reportSnvs(patientId: number, toReport: SnvsToReportDto[]): Observable<SnvsReportDto[]> {
		const url = `${this.URL_BASE}/${patientId}/snvs/report`;
		return this.http.post<SnvsReportDto[]>(url, toReport);
	}
}
