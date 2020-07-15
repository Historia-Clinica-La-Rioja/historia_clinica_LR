import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import {
	AllergyConditionDto,
	HealthHistoryConditionDto,
	InmunizationDto,
	MedicationDto,
	Last2VitalSignsDto,
	AnthropometricDataDto
} from '@api-rest/api-model';
import { HttpClient } from '@angular/common/http';
import { ContextService } from '@core/services/context.service';
import { environment } from '@environments/environment';

// todo borrar cuando esten los endpoints
const FAMILY_H_DATA: any[] = [
	{
		date: '2020-07-08',
		id: 31,
		snomed: { id: '429961000', pt: 'antecedente familiar de demencia' },
		statusId: '55561003',
		verificationId: '59156000',
	}
];

const ALERGY_DATA: any[] = [
	{
		categoryId: '1',
		date: 'string',
		severity: 'string',
		snomed: { id: '300910009', pt: 'alergia a polen' }
	},
	{
		categoryId: '2',
		date: 'string',
		severity: 'string',
		snomed: { id: '294238000', pt: 'alergia a oro' }
	},
];

const PERSONAL_H_DATA: any[] = [
	{
		date: '2020-07-08',
		id: 31,
		snomed: { id: '429961000', pt: 'antecedente familiar de demencia' },
		statusId: '55561003',
		verificationId: '59156000',
	}
];

const MEDICATIONS: any[] = [
	{
		snomed: { id: '429961000', pt: 'antecedente familiar de demencia' },
		statusId: '55561003',
	}
];

const INMUNIZATIONS_DATA: any[] = [
	{
		id: 14,
		snomed: { id: '41000221108', pt: 'vacuna hepatitis B' },
		statusId: '255594003'
	},

	{
		administrationDate: '2020-02-05',
		id: 13,
		snomed: { id: '991000221105', pt: 'vacuna cólera' },
		statusId: '255594003'
	}
];

const VACIO: any = {};

@Injectable({
	providedIn: 'root'
})
export class HceGeneralStateService {

	constructor(
		private http: HttpClient,
		private contextService: ContextService
	) {
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

	getInmunizations(patientId: number): Observable<InmunizationDto[]> {
		return of(INMUNIZATIONS_DATA);
	}

	getVitalSigns(patientId: number): Observable<Last2VitalSignsDto> {
		return of(VACIO);
	}

	getAnthropometricData(patientId: number): Observable<AnthropometricDataDto> {
		let url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/patient/${patientId}/hce/general-state/anthropometricData`;
		return this.http.get<AnthropometricDataDto>(url);
	}

}
