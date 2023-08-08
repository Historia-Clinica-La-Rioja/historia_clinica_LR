import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
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

	getDepartmentsBySpecialy(provinceId: number, clinicalSpecialtyId: number): Observable<any[]> {
		const url = `${environment.apiBase}/address/masterdata/province/${provinceId}/departments/with-specialty/${clinicalSpecialtyId}`;
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

	getActiveDiariesInInstitutionByClinicalSpecialty(provinceId: number, careLineId: number, clinicalSpecialtyId: number) {
		const url = `${environment.apiBase}/address/masterdata/province/${provinceId}/departments/with-specialty/${clinicalSpecialtyId}`;
		const queryParams = { careLineId: careLineId.toString() };
		return this.http.get<any[]>(url, { params: queryParams });
	};


	getDepartmentsForReference(clinicalSpecialtyId: number, careLineId?: number) {
		const url = `${environment.apiBase}/address/masterdata/institution/${this.contextService.institutionId}/departments/with-specialty/${clinicalSpecialtyId}`;
		if (careLineId) {
			const queryParams = { careLineId: careLineId.toString() };
			return this.http.get<any[]>(url, { params: queryParams });
		}
		else
			return this.http.get<any[]>(url);

	}
}
