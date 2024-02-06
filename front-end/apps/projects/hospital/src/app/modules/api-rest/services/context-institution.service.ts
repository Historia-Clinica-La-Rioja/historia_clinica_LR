import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

import { InstitutionBasicInfoDto } from '@api-rest/api-model';
import { environment } from '@environments/environment';
import { ContextService } from '@core/services/context.service';

@Injectable({
	providedIn: 'root'
})
export class ContextInstitutionService {

	constructor(
		private http: HttpClient,
		private readonly contextService: ContextService,

	) { }

	getInstitutionsByReferenceByClinicalSpecialtyFilter(departmentId: number, clinicalSpecialtyIds: number[], careLine: number): Observable<InstitutionBasicInfoDto[]> {
		const url = `${environment.apiBase}/institution/${this.contextService.institutionId}/by-reference-clinical-specialty-filter`;
		let queryParams = new HttpParams().append('departmentId', JSON.stringify(departmentId));
		clinicalSpecialtyIds.forEach(clinicalSpecialtyId => queryParams = queryParams.append('clinicalSpecialtyIds', JSON.stringify(clinicalSpecialtyId)));
		if (careLine) {
			queryParams = queryParams.append('careLineId', JSON.stringify(careLine));
			return this.http.get<any[]>(url, { params: queryParams });
		}
		else
			return this.http.get<any[]>(url, { params: queryParams });
	}

	getInstitutionsByReferenceByPracticeFilter(practiceSnomedId: number, departmentId: number, careLineId?: number, clinicalSpecialtyIds?: number[]): Observable<InstitutionBasicInfoDto[]> {
		const url = `${environment.apiBase}/institution/${this.contextService.institutionId}/by-reference-practice-filter`;
		let queryParams = new HttpParams().append('practiceSnomedId', practiceSnomedId.toString());

		queryParams = queryParams.append('departmentId', departmentId.toString());

		if (careLineId !== undefined && careLineId !== null)
			queryParams = queryParams.append('careLineId', careLineId.toString());


		if (clinicalSpecialtyIds !== undefined && clinicalSpecialtyIds !== null && clinicalSpecialtyIds.length)
			clinicalSpecialtyIds.forEach(clinicalSpecialtyId => queryParams = queryParams.append('clinicalSpecialtyIds', clinicalSpecialtyId.toString()));

		return this.http.get<any[]>(url, { params: queryParams });
	}
}
