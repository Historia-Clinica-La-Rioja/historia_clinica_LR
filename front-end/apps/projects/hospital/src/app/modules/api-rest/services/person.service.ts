import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '@environments/environment';
import { HttpClient } from '@angular/common/http';
import { PersonBasicDataResponseDto } from '@api-rest/api-model';
import {ContextService} from "@core/services/context.service";

@Injectable({
	providedIn: 'root'
})
export class PersonService {

	constructor(private http: HttpClient, private contextService: ContextService) {
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

	canEditUserData(personId): Observable<boolean> {
		const url = `${environment.apiBase}/person/${personId}/institutionId/${this.contextService.institutionId}/can-edit-user-data`;
		return this.http.get<boolean>(url);
	}

}
