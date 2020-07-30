import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import {
	HCEImmunizationDto,
	HCEMedicationDto,
	HCELast2VitalSignsDto,
	HCEAnthropometricDataDto,
	HCEPersonalHistoryDto,
	HCEAllergyDto
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

	getAllergies(patientId: number): Observable<HCEAllergyDto[]> {
		let url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/patient/${patientId}/hce/general-state/allergies`;
		return this.http.get<HCEAllergyDto[]>(url);
	}

	getFamilyHistories(patientId: number): Observable<HCEPersonalHistoryDto[]> {
		let url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/patient/${patientId}/hce/general-state/familyHistories`;
		return this.http.get<HCEPersonalHistoryDto[]>(url);
	}

	getPersonalHistories(patientId: number): Observable<HCEPersonalHistoryDto[]> {
		let url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/patient/${patientId}/hce/general-state/personalHistories`;
		return this.http.get<HCEPersonalHistoryDto[]>(url);
	}

	getMedications(patientId: number): Observable<HCEMedicationDto[]> {
		let url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/patient/${patientId}/hce/general-state/medications`;
		return this.http.get<HCEMedicationDto[]>(url);
	}

	getImmunizations(patientId: number): Observable<HCEImmunizationDto[]> {
		let url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/patient/${patientId}/hce/general-state/immunizations`;
		return this.http.get<HCEImmunizationDto[]>(url);
	}

	getVitalSigns(patientId: number): Observable<HCELast2VitalSignsDto> {
		let url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/patient/${patientId}/hce/general-state/vitalSigns`;
		return this.http.get<HCELast2VitalSignsDto>(url);
	}

	getAnthropometricData(patientId: number): Observable<HCEAnthropometricDataDto> {
		let url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/patient/${patientId}/hce/general-state/anthropometricData`;
		return this.http.get<HCEAnthropometricDataDto>(url);
	}

	getActiveProblems(patientId: number): Observable<HCEPersonalHistoryDto[]> {
		let url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/patient/${patientId}/hce/general-state/activeProblems`;
		return this.http.get<HCEPersonalHistoryDto[]>(url);
	}

}
