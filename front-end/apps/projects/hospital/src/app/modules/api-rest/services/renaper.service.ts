import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '@environments/environment';
import { Observable } from 'rxjs';
import { MedicalCoverageDto } from '@api-rest/api-model';

@Injectable({
  providedIn: 'root'
})
export class RenaperService {

  	constructor(
		private http: HttpClient
  	) { }

	getHealthInsurance(params): Observable<MedicalCoverageDto[]> {
		const url = `${environment.apiBase}/renaper/search-health-insurance`;
		return this.http.get<MedicalCoverageDto[]>(url, { params });
	}
}
