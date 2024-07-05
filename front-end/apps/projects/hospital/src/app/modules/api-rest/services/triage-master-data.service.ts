import { Injectable } from '@angular/core';
import { environment } from '@environments/environment';
import { Observable, of } from 'rxjs';
import { MasterDataDto } from '@api-rest/api-model';
import { HttpClient } from '@angular/common/http';
import { TriageCategory } from '@historia-clinica/modules/guardia/components/triage-chip/triage-chip.component';

const TRIAGE_CATEGORIES = [
	{
		id: 1,
		name: 'Nivel 1',
		colorHex: 'FF0000'
	},
	{
		id: 2,
		name: 'Nivel 2',
		colorHex: 'FF5C02'
	},
	{
		id: 3,
		name: 'Nivel 3',
		colorHex: 'ECBE00'
	},
	{
		id: 4,
		name: 'Nivel 4',
		colorHex: '009B68'
	},
	{
		id: 5,
		name: 'Nivel 5',
		colorHex: '3F4B9D'
	},
	{
		id: 6,
		name: 'Triage pendiente',
		colorHex: 'D5D5D5'
	}
];

const BASIC_URL_PREFIX = '/emergency-care/triage/masterdata';

@Injectable({
	providedIn: 'root'
})
export class TriageMasterDataService {

	constructor(private readonly http: HttpClient) {
	}	

	getCategories(): Observable<TriageCategory[]> {
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
