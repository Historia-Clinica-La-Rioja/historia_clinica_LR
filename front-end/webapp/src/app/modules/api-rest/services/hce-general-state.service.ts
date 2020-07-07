import { Injectable } from '@angular/core';
import { Observable, of } from "rxjs";
import { AllergyConditionDto } from "@api-rest/api-model";
import { environment } from "@environments/environment";

const ALERGY_DATA: any[] = [
	{
		categoryId: '1',
		date: 'string',
		severity: 'string',
		snomed: {id: "300910009", pt: "alergia a polen"}
	},
	{
		categoryId: '2',
		date: 'string',
		severity: 'string',
		snomed: {id: "294238000", pt: "alergia a oro"}
	},
];

@Injectable({
	providedIn: 'root'
})
export class HceGeneralStateService {

	constructor() {
	}

	getAllergies(patientId: number): Observable<AllergyConditionDto[]> {
		return of(ALERGY_DATA);
	}

}
