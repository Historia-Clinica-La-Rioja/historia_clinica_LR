import { Injectable } from '@angular/core';
import { Observable, of } from "rxjs";
import { AllergyConditionDto, HealthHistoryConditionDto } from "@api-rest/api-model";
import { environment } from "@environments/environment";
import { HttpClient } from "@angular/common/http";
import { ContextService } from "@core/services/context.service";

const FAMILY_H_DATA: any[] = [
	{
		date: "2020-07-08",
		id: 31,
		snomed: {id: "429961000", pt: "antecedente familiar de demencia"},
		statusId: "55561003",
		verificationId: "59156000",
	}
];

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

	constructor(private http: HttpClient,
	            private contextService: ContextService) {
	}

	getAllergies(patientId: number): Observable<AllergyConditionDto[]> {
		return of(ALERGY_DATA);
	}

	getFamilyHistories(patientId: number): Observable<HealthHistoryConditionDto[]> {
		return of(FAMILY_H_DATA);
	}

}
