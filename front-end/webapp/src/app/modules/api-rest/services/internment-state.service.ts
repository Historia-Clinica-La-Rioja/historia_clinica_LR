import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '@environments/environment';
import {
	AllergyConditionDto,
	AnthropometricDataDto,
	DiagnosesGeneralStateDto,
	HealthConditionDto,
	HealthHistoryConditionDto,
	ImmunizationDto,
	Last2VitalSignsDto,
	MedicationDto
} from '@api-rest/api-model';
import { ContextService } from '@core/services/context.service';

@Injectable({
	providedIn: 'root'
})
export class InternmentStateService {

	constructor(
		private http: HttpClient,
		private contextService: ContextService,
	) { }

	getMainDiagnosis(internmentId: number): Observable<HealthConditionDto> {
		let url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/internments-state/${internmentId}/general/maindiagnosis`;
		return this.http.get<HealthConditionDto>(url);
	}

	getAlternativeDiagnosesGeneralState(internmentId: number): Observable<HealthConditionDto[]> {
		let url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/internments-state/${internmentId}/general/alternativeDiagnoses`;
		return this.http.get<HealthConditionDto[]>(url);
	}


	getActiveAlternativeDiagnosesGeneralState(internmentId: number): Observable<HealthConditionDto[]> {
		let url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/internments-state/${internmentId}/general/alternativeDiagnoses/active`;
		return this.http.get<HealthConditionDto[]>(url);
	}
	
	getDiagnosesGeneralState(internmentId: number): Observable<DiagnosesGeneralStateDto[]> {
		let url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/internments-state/${internmentId}/general/diagnoses`;
		return this.http.get<DiagnosesGeneralStateDto[]>(url);
	}

	getVitalSigns(internmentId: number): Observable<Last2VitalSignsDto> {
		let url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/internments-state/${internmentId}/general/vitalSigns`;
		return this.http.get<Last2VitalSignsDto>(url);
	}

	getAnthropometricData(internmentId: number): Observable<AnthropometricDataDto> {
		let url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/internments-state/${internmentId}/general/anthropometricData`;
		return this.http.get<AnthropometricDataDto>(url);
	}

	getPersonalHistories(internmentId: number): Observable<HealthHistoryConditionDto[]> {
		let url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/internments-state/${internmentId}/general/personalHistories`;
		return this.http.get<HealthHistoryConditionDto[]>(url);
	}

	getFamilyHistories(internmentId: number): Observable<HealthHistoryConditionDto[]> {
		let url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/internments-state/${internmentId}/general/familyHistories`;
		return this.http.get<HealthHistoryConditionDto[]>(url);
	}

	getMedications(internmentId: number): Observable<MedicationDto[]> {
		let url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/internments-state/${internmentId}/general/medications`;
		return this.http.get<MedicationDto[]>(url);
	}

	getAllergies(internmentId: number): Observable<AllergyConditionDto[]> {
		let url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/internments-state/${internmentId}/general/allergies`;
		return this.http.get<AllergyConditionDto[]>(url);
	}

	getImmunizations(internmentId: number): Observable<ImmunizationDto[]> {
		let url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/internments-state/${internmentId}/general/immunizations`;
		return this.http.get<ImmunizationDto[]>(url);
	}
}
