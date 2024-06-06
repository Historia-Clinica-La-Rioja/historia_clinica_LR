import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { CareLineDto } from '@api-rest/api-model';
import { ContextService } from '@core/services/context.service';
import { environment } from '@environments/environment';
import { Observable } from 'rxjs/internal/Observable';

@Injectable({
	providedIn: 'root'
})
export class DiaryCareLineService {

	private readonly BASE_URL: string;

	constructor(
		private readonly http: HttpClient,
		private readonly contextService: ContextService,
	) {
		this.BASE_URL = `${environment.apiBase}/institution`;
	}

	getPossibleCareLinesForDiary(clinicalSpecialtyId: number): Observable<CareLineDto[]> {
		const url = `${this.BASE_URL}/${this.contextService.institutionId}/diary-care-lines/${clinicalSpecialtyId} `;
		return this.http.get<CareLineDto[]>(url);
	}

	getPossibleCareLinesForDiaryByPracticesAndSpecialty(practicesId: number[], clinicalSpecialtyId: number): Observable<CareLineDto[]> {
		const url = `${this.BASE_URL}/${this.contextService.institutionId}/diary-care-lines/practices`;
		let params = new HttpParams();
		params = params.append('practicesId', practicesId.join(', '));
		if (clinicalSpecialtyId)
			params = params.append('clinicalSpecialtyId', clinicalSpecialtyId);
		return this.http.get<CareLineDto[]>(url, { params });
	}
}
