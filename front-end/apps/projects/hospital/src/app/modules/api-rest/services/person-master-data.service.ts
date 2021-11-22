import { Injectable } from '@angular/core';
import { environment } from '@environments/environment';
import { HttpClient } from '@angular/common/http';
import { EducationLevelDto, EthnicityDto, GenderDto, IdentificationTypeDto, PersonOccupationDto, SelfPerceivedGenderDto } from '@api-rest/api-model';
import { Observable } from 'rxjs';

@Injectable({
	providedIn: 'root'
})
export class PersonMasterDataService {

	private readonly URL_BASE: string;

	constructor(
		private http: HttpClient
	) {
		this.URL_BASE = `${environment.apiBase}/person/masterdata`;
	}

	public getGenders(): Observable<GenderDto[]> {
		const url = `${this.URL_BASE}/genders`;
		return this.http.get<GenderDto[]>(url);
	}

	public getIdentificationTypes(): Observable<IdentificationTypeDto[]> {
		const url = `${this.URL_BASE}/identificationTypes`;
		return this.http.get<IdentificationTypeDto[]>(url);
	}

	public getEthnicities(): Observable<EthnicityDto[]> {
		const url = `${this.URL_BASE}/ethnicities`;
		return this.http.get<EthnicityDto[]>(url);
	}

	public getOccupations(): Observable<PersonOccupationDto[]> {
		const url = `${this.URL_BASE}/occupations`;
		return this.http.get<PersonOccupationDto[]>(url);
	}

	public getEducationLevels(): Observable<EducationLevelDto[]> {
		const url = `${this.URL_BASE}/educationLevel`;
		return this.http.get<EducationLevelDto[]>(url);
	}

	public getSelfPerceivedGenders(): Observable<SelfPerceivedGenderDto[]> {
		const url = `${this.URL_BASE}/self-perceived-genders`;
		return this.http.get<SelfPerceivedGenderDto[]>(url);
	}
}
