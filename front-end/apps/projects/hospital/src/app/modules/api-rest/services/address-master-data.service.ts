import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '@environments/environment';
import { Observable } from 'rxjs';


@Injectable({
	providedIn: 'root'
})
export class AddressMasterDataService {

	constructor(
		private http: HttpClient
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


}
