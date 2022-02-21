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
	Last2RiskFactorsDto,
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
		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/internments-state/${internmentId}/general/maindiagnosis`;
		return this.http.get<HealthConditionDto>(url);
	}

	getAlternativeDiagnosesGeneralState(internmentId: number): Observable<HealthConditionDto[]> {
		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/internments-state/${internmentId}/general/alternativeDiagnoses`;
		return this.http.get<HealthConditionDto[]>(url);
	}


	getActiveAlternativeDiagnosesGeneralState(internmentId: number): Observable<HealthConditionDto[]> {
		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/internments-state/${internmentId}/general/alternativeDiagnoses/active`;
		return this.http.get<HealthConditionDto[]>(url);
	}

	getDiagnosesGeneralState(internmentId: number): Observable<DiagnosesGeneralStateDto[]> {
		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/internments-state/${internmentId}/general/diagnoses`;
		return this.http.get<DiagnosesGeneralStateDto[]>(url);
	}

	getRiskFactors(internmentId: number): Observable<Last2RiskFactorsDto> {
		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/internments-state/${internmentId}/general/riskFactors`;
		return this.http.get<Last2RiskFactorsDto>(url);
	}

	getAnthropometricData(internmentId: number): Observable<AnthropometricDataDto> {
		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/internments-state/${internmentId}/general/anthropometricData`;
		return this.http.get<AnthropometricDataDto>(url);
	}

	getPersonalHistories(internmentId: number): Observable<HealthHistoryConditionDto[]> {
		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/internments-state/${internmentId}/general/personalHistories`;
		return this.http.get<HealthHistoryConditionDto[]>(url);
	}

	getFamilyHistories(internmentId: number): Observable<HealthHistoryConditionDto[]> {
		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/internments-state/${internmentId}/general/familyHistories`;
		return this.http.get<HealthHistoryConditionDto[]>(url);
	}

	getMedications(internmentId: number): Observable<MedicationDto[]> {
		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/internments-state/${internmentId}/general/medications`;
		return this.http.get<MedicationDto[]>(url);
	}

	getAllergies(internmentId: number): Observable<AllergyConditionDto[]> {
		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/internments-state/${internmentId}/general/allergies`;
		return this.http.get<AllergyConditionDto[]>(url);
	}

	getImmunizations(internmentId: number): Observable<ImmunizationDto[]> {
		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/internments-state/${internmentId}/general/immunizations`;
		return this.http.get<ImmunizationDto[]>(url);
	}
}
