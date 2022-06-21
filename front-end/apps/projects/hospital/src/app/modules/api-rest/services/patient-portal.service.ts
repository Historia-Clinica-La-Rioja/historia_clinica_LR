import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import {
	BasicPatientDto,
	CompletePatientDto,
	HCEAllergyDto,
	HCEAnthropometricDataDto,
	HCELast2RiskFactorsDto,
	HCEMedicationDto,
	HCEPersonalHistoryDto,
	PatientMedicalCoverageDto,
	PersonalInformationDto,
	PersonPhotoDto,
} from '@api-rest/api-model';
import { environment } from '@environments/environment';

@Injectable({
	providedIn: 'root'
})
export class PatientPortalService {

	private readonly URL_BASE = `${environment.apiBase}/patientportal/`;

	constructor(
		private readonly http: HttpClient,
	) { }

	getAllergies(): Observable<HCEAllergyDto[]> {
		const url = `${this.URL_BASE}allergies`;
		return this.http.get<HCEAllergyDto[]>(url);
	}

	getFamilyHistories(): Observable<HCEPersonalHistoryDto[]> {
		const url = `${this.URL_BASE}familyHistories`;
		return this.http.get<HCEPersonalHistoryDto[]>(url);
	}

	getPersonalHistories(): Observable<HCEPersonalHistoryDto[]> {
		const url = `${this.URL_BASE}personalHistories`;
		return this.http.get<HCEPersonalHistoryDto[]>(url);
	}

	getMedications(): Observable<HCEMedicationDto[]> {
		const url = `${this.URL_BASE}medications`;
		return this.http.get<HCEMedicationDto[]>(url);
	}

	getRiskFactors(): Observable<HCELast2RiskFactorsDto> {
		const url = `${this.URL_BASE}riskFactors`;
		return this.http.get<HCELast2RiskFactorsDto>(url);
	}

	getBasicDataPatient(): Observable<BasicPatientDto> {
		const url = `${this.URL_BASE}basicdata`;
		return this.http.get<BasicPatientDto>(url);
	}

	getPatientPhoto(): Observable<PersonPhotoDto> {
		const url = `${this.URL_BASE}photo`;
		return this.http.get<PersonPhotoDto>(url);
	}

	getCompleteDataPatient(): Observable<CompletePatientDto> {
		const url = `${this.URL_BASE}completedata`;
		return this.http.get<CompletePatientDto>(url);
	}

	getPersonalInformation(): Observable<PersonalInformationDto> {
		const url = `${this.URL_BASE}personalInformation`;
		return this.http.get<PersonalInformationDto>(url);
	}

	getActivePatientMedicalCoverages(): Observable<PatientMedicalCoverageDto[]> {
		const url = `${this.URL_BASE}coverages`;
		return this.http.get<PatientMedicalCoverageDto[]>(url);
	}

	getLast2AnthropometricData(): Observable<HCEAnthropometricDataDto[]> {
		const url = `${this.URL_BASE}last-2-anthropometric-data`;
		return this.http.get<HCEAnthropometricDataDto[]>(url);
	}
}
