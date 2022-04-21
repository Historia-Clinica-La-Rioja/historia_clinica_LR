import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import {
	HCEImmunizationDto,
	HCEMedicationDto,
	HCELast2RiskFactorsDto,
	HCEAnthropometricDataDto,
	HCEPersonalHistoryDto,
	HCEAllergyDto,
	HCEHospitalizationHistoryDto,
	HCEToothRecordDto, HCEEvolutionSummaryDto,
	ExternalPatientCoverageDto
} from '@api-rest/api-model';
import { HttpClient } from '@angular/common/http';
import { ContextService } from '@core/services/context.service';
import { environment } from '@environments/environment';

@Injectable({
	providedIn: 'root'
})
export class HceGeneralStateService {

	private readonly URL_BASE = `${environment.apiBase}/institutions/${this.contextService.institutionId}/patient/`;
	private readonly URL_SUFFIX = '/hce/general-state/';

	constructor(
		private http: HttpClient,
		private contextService: ContextService
	) {
	}

	getAllergies(patientId: number): Observable<HCEAllergyDto[]> {
		const url = this.URL_BASE + patientId + this.URL_SUFFIX + `allergies`;
		return this.http.get<HCEAllergyDto[]>(url);
	}

	getCriticalAllergies(patientId: number): Observable<HCEAllergyDto[]> {
		const url = this.URL_BASE + patientId + this.URL_SUFFIX + `critical-allergies`;
		return this.http.get<HCEAllergyDto[]>(url);
	}

	getFamilyHistories(patientId: number): Observable<HCEPersonalHistoryDto[]> {
		const url = this.URL_BASE + patientId + this.URL_SUFFIX + `familyHistories`;
		return this.http.get<HCEPersonalHistoryDto[]>(url);
	}

	getPersonalHistories(patientId: number): Observable<HCEPersonalHistoryDto[]> {
		const url = this.URL_BASE + patientId + this.URL_SUFFIX + `personalHistories`;
		return this.http.get<HCEPersonalHistoryDto[]>(url);
	}

	getMedications(patientId: number): Observable<HCEMedicationDto[]> {
		const url = this.URL_BASE + patientId + this.URL_SUFFIX + `medications`;
		return this.http.get<HCEMedicationDto[]>(url);
	}

	getImmunizations(patientId: number): Observable<HCEImmunizationDto[]> {
		const url = this.URL_BASE + patientId + this.URL_SUFFIX + `immunizations`;
		return this.http.get<HCEImmunizationDto[]>(url);
	}

	getRiskFactors(patientId: number): Observable<HCELast2RiskFactorsDto> {
		const url = this.URL_BASE + patientId + this.URL_SUFFIX + `riskFactors`;
		return this.http.get<HCELast2RiskFactorsDto>(url);
	}

	getAnthropometricData(patientId: number): Observable<HCEAnthropometricDataDto> {
		const url = this.URL_BASE + patientId + this.URL_SUFFIX + `anthropometricData`;
		return this.http.get<HCEAnthropometricDataDto>(url);
	}

	getActiveProblems(patientId: number): Observable<HCEPersonalHistoryDto[]> {
		const url = this.URL_BASE + patientId + this.URL_SUFFIX + `activeProblems`;
		return this.http.get<HCEPersonalHistoryDto[]>(url);
	}

	getSolvedProblems(patientId: number): Observable<HCEPersonalHistoryDto[]> {
		const url = this.URL_BASE + patientId + this.URL_SUFFIX + `solvedProblems`;
		return this.http.get<HCEPersonalHistoryDto[]>(url);
	}

	getChronicConditions(patientId: number): Observable<HCEPersonalHistoryDto[]> {
		const url = this.URL_BASE + patientId + this.URL_SUFFIX + `chronic`;
		return this.http.get<HCEPersonalHistoryDto[]>(url);
	}

	getHospitalizationHistory(patientId: number): Observable<HCEHospitalizationHistoryDto[]> {
		const url = this.URL_BASE + patientId + this.URL_SUFFIX + `hospitalization`;
		return this.http.get<HCEHospitalizationHistoryDto[]>(url);
	}

	getToothRecords(patientId: number, toothSctid: string): Observable<HCEToothRecordDto[]> {
		const url = this.URL_BASE + patientId + this.URL_SUFFIX + `toothRecords/tooth/${toothSctid}`;
		return this.http.get<HCEToothRecordDto[]>(url);
	}

	getEvolutionSummaryList(patientId: number): Observable<HCEEvolutionSummaryDto[]> {
		const url = this.URL_BASE + patientId + this.URL_SUFFIX + `summary-list`;
		return this.http.get<HCEEvolutionSummaryDto[]>(url);
	}

	getInternmentEpisodeMedicalCoverage(patientId: number, internmentId: number): Observable<ExternalPatientCoverageDto> {
		const url = this.URL_BASE + patientId + this.URL_SUFFIX + `active-internment-episode/${internmentId}/medical-coverage`;
		return this.http.get<ExternalPatientCoverageDto>(url);
	}

	getLast2AnthropometricData(patientId: number): Observable<HCEAnthropometricDataDto[]> {
		const url = this.URL_BASE + patientId + this.URL_SUFFIX + `last-2-anthropometric-data`;
		return this.http.get<HCEAnthropometricDataDto[]>(url);
	}

}
