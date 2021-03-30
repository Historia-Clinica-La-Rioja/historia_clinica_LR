import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '@environments/environment';
import { HttpClient } from '@angular/common/http';
import { PersonBasicDataResponseDto } from '@api-rest/api-model';

@Injectable({
	providedIn: 'root'
})
export class PersonService {

	constructor(private http: HttpClient) {
	}

	getRenaperPersonData(params): Observable<PersonBasicDataResponseDto> {
		const url = `${environment.apiBase}/renaper/searchPerson`;
		return this.http.get<PersonBasicDataResponseDto>(url, { params });
	}

	getPersonalInformation<PersonalInformationDto>(personId): Observable<PersonalInformationDto> {
		const url = `${environment.apiBase}/person/${personId}/personalInformation`;
		return this.http.get<PersonalInformationDto>(url);
	}

	getCompletePerson<BMPersonDto>(personId): Observable<BMPersonDto> {
		const url = `${environment.apiBase}/person/${personId}`;
		return this.http.get<BMPersonDto>(url);
	}

}
