import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { environment } from '@environments/environment';
import { Observable } from 'rxjs';
import { DepartmentDto } from '@api-rest/api-model';
import { ContextService } from '@core/services/context.service';
@Injectable({
	providedIn: 'root'
})
export class AddressMasterDataService {

	constructor(
		private http: HttpClient,
		private readonly contextService: ContextService,
	) { }

	getAllCountries(): Observable<any[]> {
		const url = `${environment.apiBase}/address/masterdata/countries`;
		return this.http.get<any[]>(url);
	}

	getByCountry(countryId: number): Observable<any[]> {
		const url = `${environment.apiBase}/address/masterdata/country/${countryId}/provinces`;
		return this.http.get<any[]>(url);
	}

	getDepartmentsByProvince(provinceId: number): Observable<any[]> {
		const url = `${environment.apiBase}/address/masterdata/province/${provinceId}/departments`;
		return this.http.get<any[]>(url);
	}

	getCitiesByDepartment(departmentId: number): Observable<any[]> {
		const url = `${environment.apiBase}/address/masterdata/department/${departmentId}/cities`;
		return this.http.get<any[]>(url);
	}

	getDepartmentById(departmentId: number): Observable<DepartmentDto> {
		const url = `${environment.apiBase}/address/masterdata/department/${departmentId}`;
		return this.http.get<DepartmentDto>(url);
	}

	getDeparmentsByCareLineAndClinicalSpecialty(clinicalSpecialtyIds: number[], careLineId?: number): Observable<AddressProjection[]> {
		const url = `${environment.apiBase}/address/masterdata/institution/${this.contextService.institutionId}/departments/by-reference-clinical-specialty-filter`;
		let queryParams = new HttpParams();
		clinicalSpecialtyIds.forEach(clinicalSpecialtyId => queryParams = queryParams.append('clinicalSpecialtyIds', JSON.stringify(clinicalSpecialtyId)));
		if (careLineId !== undefined && careLineId !== null)
			queryParams = queryParams.append('careLineId', JSON.stringify(careLineId));
		return this.http.get<AddressProjection[]>(url, { params: queryParams });
	}

	getDepartmentsByCareLineAndPracticesAndClinicalSpecialty(practiceSnomedId: number, clinicalSpecialtyIds?: number[], careLineId?: number): Observable<AddressProjection[]> {
		const url = `${environment.apiBase}/address/masterdata/institution/${this.contextService.institutionId}/departments/by-reference-practice-filter`;

		let queryParams = new HttpParams().append('practiceSnomedId', practiceSnomedId.toString());

		if (careLineId !== undefined && careLineId !== null)
			queryParams = queryParams.append('careLineId', careLineId.toString());

		if (clinicalSpecialtyIds !== undefined && clinicalSpecialtyIds !== null && clinicalSpecialtyIds.length)
			clinicalSpecialtyIds.forEach(clinicalSpecialtyId => queryParams = queryParams.append('clinicalSpecialtyIds', clinicalSpecialtyId.toString()));
		return this.http.get<AddressProjection[]>(url, { params: queryParams });
	}

	getDepartmentsByInstitutions(): Observable<AddressProjection[]> {
		const url = `${environment.apiBase}/address/masterdata/departments`;
		return this.http.get<AddressProjection[]>(url);
	}
}

export interface AddressProjection {
	id: number;
	description: string;
}