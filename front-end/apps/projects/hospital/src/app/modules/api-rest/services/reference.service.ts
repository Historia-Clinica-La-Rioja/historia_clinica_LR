import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ReferenceDto, ReferenceSummaryDto } from '@api-rest/api-model';
import { ContextService } from '@core/services/context.service';
import { environment } from '@environments/environment';
import { Observable } from 'rxjs';

@Injectable({
	providedIn: 'root'
})

export class ReferenceService {

	private readonly URL_BASE: string;

	constructor(
		private readonly http: HttpClient,
		private readonly contextService: ContextService,
	) {
		this.URL_BASE = `${environment.apiBase}/institutions/${this.contextService.institutionId}/reference/patient/`;
	}

	getReferences(patientId: number, clinicalSpecialtyIds: number[]): Observable<ReferenceDto[]> {
		const url = `${this.URL_BASE}${patientId}`;
		return this.http.get<ReferenceDto[]>(url, {
			params: {
				clinicalSpecialtyIds
			}
		});
	}

	getReferencesSummary(patientId: number, diarySpecialtyId: number, diaryId: number): Observable<ReferenceSummaryDto[]> {
		const url = `${this.URL_BASE}${patientId}/requested`
		return this.http.get<ReferenceSummaryDto[]>(url, {
			params: {
				clinicalSpecialtyId: diarySpecialtyId,
				diaryId: diaryId
			}
		});
	}
}
