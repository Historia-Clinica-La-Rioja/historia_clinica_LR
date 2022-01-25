import { Injectable } from '@angular/core';
import { ContextService } from '@core/services/context.service';
import { HttpClient } from '@angular/common/http';
import {
	CreateOutpatientDto,
	OutpatientEvolutionSummaryDto,
	HealthConditionNewConsultationDto
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

	createOutpatientConsultation(createOutpatientDto: CreateOutpatientDto, patientId: number): Observable<boolean> {
		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/patient/${patientId}/outpatient/consultations/billable`;
		return this.http.post<boolean>(url, createOutpatientDto);
	}

	solveProblem(solvedProblem: HealthConditionNewConsultationDto, patientId: number): Observable<boolean> {
		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/patient/${patientId}/outpatient/consultations/solveProblem`;
		return this.http.post<boolean>(url, solvedProblem);
	}

	getEvolutionSummaryList(patientId: number): Observable<OutpatientEvolutionSummaryDto[]> {
		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/patient/${patientId}/outpatient/consultations/summary-list`;
		return this.http.get<OutpatientEvolutionSummaryDto[]>(url);
	}

}
