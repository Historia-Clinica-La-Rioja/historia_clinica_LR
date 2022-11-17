import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { CareLineDto } from '@api-rest/api-model';
import { ContextService } from '@core/services/context.service';
import { environment } from '@environments/environment';
import { Observable } from 'rxjs';

@Injectable({
	providedIn: 'root'
})
export class CareLineService {

	private readonly BASE_URL: string;

	constructor(
		private readonly http: HttpClient,
		private readonly contextService: ContextService,
	) {
		this.BASE_URL = `${environment.apiBase}/institution/${this.contextService.institutionId}`
	}

	getCareLines(): Observable<CareLineDto[]> {
		const url = `${this.BASE_URL}/carelines`;
		return this.http.get<CareLineDto[]>(url);
	}

	getCareLinesBySpecialty(specialtyId: number): Observable<CareLineDto[]> {
		const url = `${this.BASE_URL}/diary-care-lines/${specialtyId} `;
		return this.http.get<CareLineDto[]>(url);
	}

	getByProblemSnomedIdsAndInstitutionId(institutionId: number, problemSnomedIds: string[]) {
		const url = `${environment.apiBase}/institution/${this.contextService.institutionId}/carelines/problems`;
		let params = new HttpParams();
		params = params.append('problemSnomedIds', problemSnomedIds.join(', '));
		params = params.append('destinationInstitutionId', institutionId);
		return this.http.get<CareLineDto[]>(url, { params });
	}

	getCareLinesAttachedToInstitution(institutionId: number): Observable<CareLineDto[]> {
		const url = `${environment.apiBase}/institution/${institutionId}/carelines/attached`;
		return this.http.get<CareLineDto[]>(url);
	}
}
