import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ExternalClinicalHistoryDto } from '@api-rest/api-model';
import { ContextService } from '@core/services/context.service';
import { environment } from '@environments/environment';
import { Observable } from 'rxjs';

@Injectable({
	providedIn: 'root'
})
export class ExternalClinicalHistoryService {

	private readonly URL_BASE: string;
	private SUFFIX_URL: string;

	constructor(
		private readonly http: HttpClient,
		private readonly contextService: ContextService
	) {
		this.URL_BASE = `${environment.apiBase}/institutions/${this.contextService.institutionId}/patient`;
		this.SUFFIX_URL = 'outpatient/consultations';
	}

	public getExternalClinicalHistoryList(patientId: number): Observable<ExternalClinicalHistoryDto[]> {
		const url = `${this.URL_BASE}/${patientId}/${this.SUFFIX_URL}/getExternalClinicalHistoryList`;
		return this.http.get<ExternalClinicalHistoryDto[]>(url);
	}

}
