import { Injectable } from '@angular/core';
import { PatientMedicalCoverageDto } from '@api-rest/api-model';
import { Observable } from 'rxjs';
import { environment } from '@environments/environment';
import { HttpClient } from '@angular/common/http';

@Injectable({
	providedIn: 'root'
})
export class PatientMedicalCoverageService {

	constructor(private http: HttpClient) { }

	getActivePatientMedicalCoverages(patientId: number): Observable<PatientMedicalCoverageDto[]> {
		const url = `${environment.apiBase}/patientMedicalCoverage/${patientId}/coverages`;
		return this.http.get<PatientMedicalCoverageDto[]>(url);
	}

	addPatientMedicalCoverages(patientId: number, patientMedicalCoverageDto: PatientMedicalCoverageDto[]): Observable<number[]> {
		const url = `${environment.apiBase}/patientMedicalCoverage/${patientId}/coverages`;
		return this.http.post<number[]>(url, patientMedicalCoverageDto);
	}

	getPatientMedicalCoverage(patientMedicalCoverageId: number): Observable<PatientMedicalCoverageDto> {
		const url = `${environment.apiBase}/patientMedicalCoverage/${patientMedicalCoverageId}`;
		return this.http.get<PatientMedicalCoverageDto>(url);
	}
}
