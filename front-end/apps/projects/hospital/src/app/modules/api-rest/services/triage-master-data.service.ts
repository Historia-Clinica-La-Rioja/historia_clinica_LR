import { Injectable } from '@angular/core';
import { environment } from '@environments/environment';
import { Observable, of } from 'rxjs';
import { MasterDataDto } from '@api-rest/api-model';
import { HttpClient } from '@angular/common/http';
import { TriageCategory } from '@historia-clinica/modules/guardia/components/triage-chip/triage-chip.component';
import { Triages } from '@historia-clinica/modules/guardia/constants/masterdata';

export const TRIAGE_CATEGORIES = [
	{
		id: Triages.ROJO_NIVEL_1,
		name: 'Nivel 1'
	},
	{
		id: Triages.NARANJA_NIVEL_2,
		name: 'Nivel 2'
	},
	{
		id: Triages.AMARILLO_NIVEL_3,
		name: 'Nivel 3'
	},
	{
		id: Triages.VERDE_NIVEL_4,
		name: 'Nivel 4'
	},
	{
		id: Triages.AZUL_NIVEL_5,
		name: 'Nivel 5'
	},
	{
		id: Triages.GRIS_SIN_TRIAGE,
		name: 'Triage pendiente'
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
