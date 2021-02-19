import { Injectable } from '@angular/core';
import { environment } from '@environments/environment';
import { Observable, of } from 'rxjs';
import { MasterDataDto } from '@api-rest/api-model';
import { HttpClient } from '@angular/common/http';

const TRIAGE_CATEGORIES = [
	{
		id: 1,
		description: 'Nivel I',
		colour: {
			name: 'red',
			code: 'some'
		}
	},
	{
		id: 2,
		description: 'Nivel II',
		colour: {
			name: 'orange',
			code: 'some'
		}
	},
	{
		id: 3,
		description: 'Nivel III',
		colour: {
			name: 'yellow',
			code: 'some'
		}
	},
	{
		id: 4,
		description: 'Nivel IV',
		colour: {
			name: 'green',
			code: 'some'
		}
	},
	{
		id: 5,
		description: 'Nivel V',
		colour: {
			name: 'blue',
			code: 'some'
		}
	}
];

const BASIC_URL_PREFIX = '/emergency-care/triage/masterdata';

@Injectable({
	providedIn: 'root'
})
export class TriageMasterDataService {

	constructor(private readonly http: HttpClient) {
	}

	getCategories(): Observable<TriageCategoryDto[]> {
		return of(TRIAGE_CATEGORIES);
	}

	getBodyTemperature(): Observable<MasterDataDto[]> {
		const url = `${environment.apiBase + BASIC_URL_PREFIX}/bodyTemperature`;
		return this.http.get<MasterDataDto[]>(url);
	}

	getMuscleHypertonia(): Observable<MasterDataDto[]> {
		const url = `${environment.apiBase + BASIC_URL_PREFIX}/muscleHypertonia`;
		return this.http.get<MasterDataDto[]>(url);
	}

	getRespiratoryRetraction(): Observable<MasterDataDto[]> {
		const url = `${environment.apiBase + BASIC_URL_PREFIX}/respiratoryRetraction`;
		return this.http.get<MasterDataDto[]>(url);
	}

	getPerfusion(): Observable<MasterDataDto[]> {
		const url = `${environment.apiBase + BASIC_URL_PREFIX}/perfusion`;
		return this.http.get<MasterDataDto[]>(url);
	}
}

export interface TriageCategoryDto {
	id: number;
	description: string;
	colour: {
		name: string,
		code: string
	};
}
