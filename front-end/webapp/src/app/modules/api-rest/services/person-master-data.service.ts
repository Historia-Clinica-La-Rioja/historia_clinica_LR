import { Injectable } from '@angular/core';
import { environment } from '@environments/environment';
import { HttpClient } from '@angular/common/http';
import { GenderDto, IdentificationTypeDto } from '@api-rest/api-model';
import { Observable } from 'rxjs';

@Injectable({
	providedIn: 'root'
})
export class PersonMasterDataService {

	constructor(
		private http: HttpClient
	) { }

	getGenders(): Observable<GenderDto[]> {
		let url = `${environment.apiBase}/person/masterdata/genders`;
		return this.http.get<GenderDto[]>(url);
	}


	getIdentificationTypes(): Observable<IdentificationTypeDto[]> {
		let url = `${environment.apiBase}/person/masterdata/identificationTypes`;
		return this.http.get<IdentificationTypeDto[]>(url);
	}
}
