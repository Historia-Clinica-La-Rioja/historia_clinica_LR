import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {
	HCEAllergyDto,
	HCEAnthropometricDataDto,
	HCELast2VitalSignsDto,
	HCEMedicationDto,
	HCEPersonalHistoryDto,
} from '@api-rest/api-model';
import {environment} from '@environments/environment';

@Injectable({
  providedIn: 'root'
})
export class PatientPortalService {

  constructor(
  	private readonly http: HttpClient,
  ) { }

	getAllergies(): Observable<HCEAllergyDto[]> {
		const url = `${environment.apiBase}/patientportal/allergies`;
		return this.http.get<HCEAllergyDto[]>(url);
	}

	getFamilyHistories(): Observable<HCEPersonalHistoryDto[]> {
		const url = `${environment.apiBase}/patientportal/familyHistories`;
		return this.http.get<HCEPersonalHistoryDto[]>(url);
	}

	getPersonalHistories(): Observable<HCEPersonalHistoryDto[]> {
		const url = `${environment.apiBase}/patientportal/personalHistories`;
		return this.http.get<HCEPersonalHistoryDto[]>(url);
	}

	getMedications(): Observable<HCEMedicationDto[]> {
		const url = `${environment.apiBase}/patientportal/medications`;
		return this.http.get<HCEMedicationDto[]>(url);
	}

	getVitalSigns(): Observable<HCELast2VitalSignsDto> {
		const url = `${environment.apiBase}/patientportal/vitalSigns`;
		return this.http.get<HCELast2VitalSignsDto>(url);
	}

	getAnthropometricData(): Observable<HCEAnthropometricDataDto> {
		const url = `${environment.apiBase}/patientportal/anthropometricData`;
		return this.http.get<HCEAnthropometricDataDto>(url);
	}
}
