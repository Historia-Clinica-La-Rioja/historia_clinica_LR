import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '@environments/environment';
import { AnthropometricDataDto, VitalSignDto, HealthConditionDto } from '@api-rest/api-model';

const HARD_CODE_INS_ID = 10;

@Injectable({
	providedIn: 'root'
})
export class InternmentStateService {

	constructor(
		private http: HttpClient
	) { }

	getMainDiagnosis(internmentId: number): Observable<HealthConditionDto> {
		let url = `${environment.apiBase}/institutions/${HARD_CODE_INS_ID}/internments-state/${internmentId}/general/maindiagnosis`;
		return this.http.get<HealthConditionDto>(url);
	}

	getDiagnosis(internmentId: number): Observable<HealthConditionDto[]> {
		let url = `${environment.apiBase}/institutions/${HARD_CODE_INS_ID}/internments-state/${internmentId}/general/diagnosis`;
		return this.http.get<HealthConditionDto[]>(url);
	}

	getVitalSigns(internmentId: number): Observable<VitalSignDto[]> {
		let url = `${environment.apiBase}/institutions/${HARD_CODE_INS_ID}/internments-state/${internmentId}/general/vitalSigns`;
		return this.http.get<VitalSignDto[]>(url);
	}

	getAnthropometricData(internmentId: number): Observable<AnthropometricDataDto> {
		let url = `${environment.apiBase}/institutions/${HARD_CODE_INS_ID}/internments-state/${internmentId}/general/anthropometricData`;
		return this.http.get<AnthropometricDataDto>(url);
	}

}
