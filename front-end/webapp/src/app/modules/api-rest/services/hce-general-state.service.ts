import { Injectable } from '@angular/core';
import { Observable, of } from "rxjs";
import { AllergyConditionDto, HealthHistoryConditionDto, MedicationDto, Last2VitalSignsDto, AnthropometricDataDto } from "@api-rest/api-model";
import { environment } from "@environments/environment";
import { HttpClient } from "@angular/common/http";
import { ContextService } from "@core/services/context.service";

//todo borrar cuando esten los endpoints
const FAMILY_H_DATA: any[] = [
	{
		date: "2020-07-08",
		id: 31,
		snomed: { id: "429961000", pt: "antecedente familiar de demencia" },
		statusId: "55561003",
		verificationId: "59156000",
	}
];

const ALERGY_DATA: any[] = [
	{
		categoryId: '1',
		date: 'string',
		severity: 'string',
		snomed: { id: "300910009", pt: "alergia a polen" }
	},
	{
		categoryId: '2',
		date: 'string',
		severity: 'string',
		snomed: { id: "294238000", pt: "alergia a oro" }
	},
];

const PERSONAL_H_DATA: any[] = [
	{
		date: "2020-07-08",
		id: 31,
		snomed: { id: "429961000", pt: "antecedente familiar de demencia" },
		statusId: "55561003",
		verificationId: "59156000",
	}
];

const MEDICATIONS: any[] = [
	{
		snomed: { id: "429961000", pt: "antecedente familiar de demencia" },
		statusId: "55561003",
	}
];

const VACIO: any = {};

const ANTHROPOMETRIC_DATA: any = {
	bloodType: { id: 3, value: "A−" },
	height: { id: 5, value: "222" },
	weight: { id: 4, value: "32" }
};

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

	getPersonalHistories(patientId: number): Observable<HealthHistoryConditionDto[]> {
		return of(PERSONAL_H_DATA);
	}

	getMedications(patientId: number): Observable<MedicationDto[]> {
		return of(MEDICATIONS);
	}

	getVitalSigns(patientId: number): Observable<Last2VitalSignsDto> {
		return of(VACIO);
	}

	getAnthropometricData(patientId: number): Observable<AnthropometricDataDto> {
		return of(ANTHROPOMETRIC_DATA);
	}

}
