import { Injectable } from '@angular/core';
import { Observable, of } from "rxjs";
import { environment } from "@environments/environment";
import { HttpClient } from "@angular/common/http";
import { BasicPatientDto, PersonalInformationDto, PersonBasicDataResponseDto } from '@api-rest/api-model';

@Injectable({
	providedIn: 'root'
})
export class PersonService {

	constructor(private http: HttpClient) {
	}

	getRenaperPersonData(params): Observable<PersonBasicDataResponseDto> {
		let url = `${environment.apiBase}/renaper/searchPerson`;
		return this.http.get<PersonBasicDataResponseDto>(url, { params: params });
	}

	getPersonalInformation<PersonalInformationDto>(personId): Observable<PersonalInformationDto> {
		let url = `${environment.apiBase}/person/${personId}/personalInformation`;
		return this.http.get<PersonalInformationDto>(url);
	}

}
