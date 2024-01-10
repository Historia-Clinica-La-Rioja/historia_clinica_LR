import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ReferenceDataDto, ReferenceSummaryDto } from '@api-rest/api-model';
import { ContextService } from '@core/services/context.service';
import { environment } from '@environments/environment';
import { SearchAppointmentCriteria } from '@turnos/components/search-appointments-in-care-network/search-appointments-in-care-network.component';
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

	getReferences(patientId: number, clinicalSpecialtyIds: number[]): Observable<ReferenceDataDto[]> {
		const url = `${this.URL_BASE}${patientId}`;
		return this.http.get<ReferenceDataDto[]>(url, {
			params: {
				clinicalSpecialtyIds
			}
		});
	}

	getReferencesSummary(patientId: number, searchAppointmentCriteria: SearchAppointmentCriteria): Observable<ReferenceSummaryDto[]> {
		const url = `${this.URL_BASE}${patientId}/requested`
		let params = new HttpParams();
		if (searchAppointmentCriteria.specialtyId)
			params = params.append("clinicalSpecialtyId", searchAppointmentCriteria.specialtyId);

		if (searchAppointmentCriteria.practiceId)
			params = params.append("practiceId", searchAppointmentCriteria.practiceId);

		if (searchAppointmentCriteria.careLineId)
			params = params.append("careLineId", searchAppointmentCriteria.careLineId);

		return this.http.get<ReferenceSummaryDto[]>(url, { params });
	}
}
