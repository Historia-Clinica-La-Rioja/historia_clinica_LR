import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { AddressDto, InstitutionBasicInfoDto, InstitutionDto } from '@api-rest/api-model';
import { environment } from '@environments/environment';
import { ContextService } from '@core/services/context.service';

@Injectable({
	providedIn: 'root'
})
export class InstitutionService {

	constructor(
		private http: HttpClient,
		private readonly contextService: ContextService,

	) { }

	public getInstitutions(ids: number[]): Observable<InstitutionDto[]> {
		if (!ids || ids.length === 0) {
			return of([]);
		}
		return this.http.get<InstitutionDto[]>(
			`${environment.apiBase}/institution`,
			{
				params: { ids: `${ids.join(',')}` }
			}
		);
	}

	public getAllInstitutions(): Observable<InstitutionBasicInfoDto[]> {
		return this.http.get<InstitutionBasicInfoDto[]>(`${environment.apiBase}/institution/all`);
	}

	public getAddress(institutionId: number): Observable<AddressDto> {
		return this.http.get<AddressDto>(`${environment.apiBase}/institution/${institutionId}/address`);
	}


	public getInstitutionAddress(institutionId: number): Observable<AddressDto> {
		if (!institutionId) {
			return of();
		}
		return this.http.get<AddressDto>(`${environment.apiBase}/institution/${institutionId}/address`);
	}

	public findByDepartmentId(departmentId: number): Observable<InstitutionBasicInfoDto[]> {
		if (!departmentId) {
			return of([]);
		}
		return this.http.get<InstitutionBasicInfoDto[]>(`${environment.apiBase}/institution/department/${departmentId}`);
	}

	public findByProvinceId(provinceId: number): Observable<InstitutionBasicInfoDto[]> {
		return this.http.get<InstitutionBasicInfoDto[]>(`${environment.apiBase}/institution/province/${provinceId}`);
	}

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

	getVirtualConsultationInstitutions():Observable<InstitutionBasicInfoDto[]>{
		return this.http.get<InstitutionBasicInfoDto[]>(`${environment.apiBase}/institution/virtual-consultation`);
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
