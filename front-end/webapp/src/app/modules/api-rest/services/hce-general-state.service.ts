import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import {
	AllergyConditionDto,
	HealthHistoryConditionDto,
	MedicationDto,
	Last2VitalSignsDto,
	AnthropometricDataDto,
	HCEImmunizationDto
} from '@api-rest/api-model';
import { HttpClient } from '@angular/common/http';
import { ContextService } from '@core/services/context.service';
import { environment } from '@environments/environment';

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
		let url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/patient/${patientId}/hce/general-state/allergies`;
		return this.http.get<AllergyConditionDto[]>(url);
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

	getImmunizations(patientId: number): Observable<HCEImmunizationDto[]> {
		let url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/patient/${patientId}/hce/general-state/immunizations`;
		return this.http.get<HCEImmunizationDto[]>(url);
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
