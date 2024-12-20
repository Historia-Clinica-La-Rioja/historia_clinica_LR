import { Injectable } from '@angular/core';
import { ContextService } from '@core/services/context.service';
import { HttpClient } from '@angular/common/http';
import {
	CreateOutpatientDto,
	HealthConditionNewConsultationDto,
	ProblemInfoDto
} from '@api-rest/api-model';
import { Observable } from 'rxjs';
import { environment } from '@environments/environment';

@Injectable({
	providedIn: 'root'
})
export class OutpatientConsultationService {

	constructor(
		private readonly http: HttpClient,
		private readonly contextService: ContextService
	) {
	}

	createOutpatientConsultation(createOutpatientDto: CreateOutpatientDto, patientId: number, selectedFiles?: File[]): Observable<any> {
		const formData = new FormData();
		if (selectedFiles)
			Array.from(selectedFiles).forEach(file => formData.append('serviceRequestFiles', file));
		formData.append('createOutpatientDto', new Blob([JSON.stringify(createOutpatientDto)], {type: 'application/json'}))
		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/patient/${patientId}/outpatient/consultations/billable`;
		return this.http.post<boolean>(url, formData);
	}

	solveProblem(solvedProblem: HealthConditionNewConsultationDto, patientId: number): Observable<boolean> {
		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/patient/${patientId}/outpatient/consultations/solveProblem`;
		return this.http.post<boolean>(url, solvedProblem);
	}

	validateProblemAsError(patientId: number, healthConditionId: number): Observable<ProblemInfoDto[]> {
		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/patient/${patientId}/outpatient/consultations/validateProblemAsError/${healthConditionId}`
		return this.http.get<ProblemInfoDto[]>(url);
	}
}
