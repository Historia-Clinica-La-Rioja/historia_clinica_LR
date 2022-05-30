import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '@environments/environment';
import { Observable } from 'rxjs';
import { MedicalCoverageDto, MedicalCoveragePlanDto } from '@api-rest/api-model';

@Injectable({
  providedIn: 'root'
})
export class HealthInsuranceService {

	constructor(
		private http: HttpClient
  	) { }

	getAll(): Observable<MedicalCoverageDto[]> {
		const url = `${environment.apiBase}/health-insurance`;
		return this.http.get<MedicalCoverageDto[]>(url);
	}

	get(rnos: number): Observable<MedicalCoverageDto> {
		const url = `${environment.apiBase}/health-insurance/${rnos}`;
		return this.http.get<MedicalCoverageDto>(url);
	}

	getAllPlansById(healthInsuranceId : number): Observable<MedicalCoveragePlanDto[]> {
		const url = `${environment.apiBase}/health-insurance/${healthInsuranceId}/plans`;
		return this.http.get<MedicalCoveragePlanDto[]>(url);
	}

	getPlanById(healthInsuranceId : number, healthInsurancePlanId : number): Observable<MedicalCoveragePlanDto> {
		const url = `${environment.apiBase}/health-insurance/${healthInsuranceId}/health-insurance-plan/${healthInsurancePlanId}`;
		return this.http.get<MedicalCoveragePlanDto>(url);
	}
}
