import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {HCEAllergyDto, HCEAnthropometricDataDto, HCELast2VitalSignsDto, HCEMedicationDto, HCEPersonalHistoryDto} from '@api-rest/api-model';
import {environment} from '@environments/environment';

@Injectable({
  providedIn: 'root'
})
export class PatientHceService {

  constructor(
  	private http: HttpClient,
  ) { }

	getAllergies(): Observable<HCEAllergyDto[]> {
		const patientId = 1;
		const institutionId = 1;
		const url = `${environment.apiBase}/institutions/${institutionId}/patient/${patientId}/hce/general-state/allergies`;
		return this.http.get<HCEAllergyDto[]>(url);
	}

	getFamilyHistories(): Observable<HCEPersonalHistoryDto[]> {
		const patientId = 1;
		const institutionId = 1;
		const url = `${environment.apiBase}/institutions/${institutionId}/patient/${patientId}/hce/general-state/familyHistories`;
		return this.http.get<HCEPersonalHistoryDto[]>(url);
	}

	getPersonalHistories(): Observable<HCEPersonalHistoryDto[]> {
		const patientId = 1;
		const institutionId = 1;
		const url = `${environment.apiBase}/institutions/${institutionId}/patient/${patientId}/hce/general-state/personalHistories`;
		return this.http.get<HCEPersonalHistoryDto[]>(url);
	}

	getMedications(): Observable<HCEMedicationDto[]> {
		const patientId = 1;
		const institutionId = 1;
		const url = `${environment.apiBase}/institutions/${institutionId}/patient/${patientId}/hce/general-state/medications`;
		return this.http.get<HCEMedicationDto[]>(url);
	}

	getVitalSigns(): Observable<HCELast2VitalSignsDto> {
		const patientId = 1;
		const institutionId = 1;
		const url = `${environment.apiBase}/institutions/${institutionId}/patient/${patientId}/hce/general-state/vitalSigns`;
		return this.http.get<HCELast2VitalSignsDto>(url);
	}

	getAnthropometricData(): Observable<HCEAnthropometricDataDto> {
		const patientId = 1;
		const institutionId = 1;
		const url = `${environment.apiBase}/institutions/${institutionId}/patient/${patientId}/hce/general-state/anthropometricData`;
		return this.http.get<HCEAnthropometricDataDto>(url);
	}
}
