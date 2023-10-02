import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
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

	getInstitutionsByReferenceByClinicalSpecialtyFilter(departmentId: number,clinicalSpecialtyId: number, careLine: number): Observable<InstitutionBasicInfoDto[]> {
		const url = `${environment.apiBase}/institution/${this.contextService.institutionId}/by-reference-clinical-specialty-filter`;
		const queryParams = {departmentId: departmentId.toString(), clinicalSpecialtyId: clinicalSpecialtyId.toString()};
		if (careLine) {
			queryParams['careLineId'] = careLine.toString();
			return this.http.get<any[]>(url, { params: queryParams });
		}
		else
			return this.http.get<any[]>(url, { params: queryParams });
	}

	getVirtualConsultationInstitutions():Observable<InstitutionBasicInfoDto[]>{
		return this.http.get<InstitutionBasicInfoDto[]>(`${environment.apiBase}/institution/virtual-consultation`);
	}

	getInstitutionsByReferenceByPracticeFilter(practiceSnomedId: number, departmentId: number, careLineId?: number, clinicalSpecialtyId?: number): Observable<InstitutionBasicInfoDto[]> {
		const url = `${environment.apiBase}/institution/${this.contextService.institutionId}/by-reference-practice-filter`;
		let queryParams = { practiceSnomedId: practiceSnomedId.toString() };

		queryParams['departmentId'] = departmentId.toString();

		if (careLineId !== undefined && careLineId !== null) {
			queryParams['careLineId'] = careLineId.toString();
		}

		if (clinicalSpecialtyId !== undefined && clinicalSpecialtyId !== null) {
			queryParams['clinicalSpecialtyId'] = clinicalSpecialtyId.toString();
		}

		return this.http.get<any[]>(url, { params: queryParams });
	}
}
