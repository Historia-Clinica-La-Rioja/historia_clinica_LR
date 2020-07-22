import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import {
	AllergyConditionDto,
	HealthHistoryConditionDto,
	ImmunizationDto,
	MedicationDto,
	Last2VitalSignsDto,
	AnthropometricDataDto
} from '@api-rest/api-model';
import { HttpClient } from '@angular/common/http';
import { ContextService } from '@core/services/context.service';
import { environment } from '@environments/environment';

// todo borrar cuando esten los endpoints

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
		let url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/patient/${patientId}/hce/general-state/familyHistory`;
		return this.http.get<HealthHistoryConditionDto[]>(url);
	}

	getPersonalHistories(patientId: number): Observable<HealthHistoryConditionDto[]> {
		let url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/patient/${patientId}/hce/general-state/personalHistory`;
		return this.http.get<HealthHistoryConditionDto[]>(url);
	}

	getMedications(patientId: number): Observable<MedicationDto[]> {
		let url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/patient/${patientId}/hce/general-state/medication`;
		return this.http.get<MedicationDto[]>(url);
	}

	getImmunizations(patientId: number): Observable<ImmunizationDto[]> {
		return of(INMUNIZATIONS_DATA);
	}

	getVitalSigns(patientId: number): Observable<Last2VitalSignsDto> {
		let url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/patient/${patientId}/hce/general-state/vitalSigns`;
		return this.http.get<Last2VitalSignsDto>(url);
	}

	getAnthropometricData(patientId: number): Observable<AnthropometricDataDto> {
		let url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/patient/${patientId}/hce/general-state/anthropometricData`;
		return this.http.get<AnthropometricDataDto>(url);
	}

}
