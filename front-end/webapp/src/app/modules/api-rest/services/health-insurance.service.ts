import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '@environments/environment';
import { Observable } from 'rxjs';
import { MedicalCoverageDto } from '@api-rest/api-model';

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
}
