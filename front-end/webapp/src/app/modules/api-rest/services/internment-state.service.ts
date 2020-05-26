import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '@environments/environment';
import { AnthropometricDataDto, HealthConditionDto, HealthHistoryConditionDto, Last2VitalSignsDto } from '@api-rest/api-model';
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

	getDiagnosis(internmentId: number): Observable<HealthConditionDto[]> {
		let url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/internments-state/${internmentId}/general/diagnosis`;
		return this.http.get<HealthConditionDto[]>(url);
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
}
