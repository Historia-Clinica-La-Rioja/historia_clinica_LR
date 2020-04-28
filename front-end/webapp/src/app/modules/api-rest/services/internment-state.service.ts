import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { environment } from '@environments/environment';

const HARD_CODE_INS_ID = 10;

@Injectable({
	providedIn: 'root'
})
export class InternmentStateService {

	constructor(
		private http: HttpClient
	) { }

	getDiagnosis<HealthConditionDto>(internmentId): Observable <HealthConditionDto[]> {
		let url = `${environment.apiBase}/institutions/${HARD_CODE_INS_ID}/internments-state/${HARD_CODE_INS_ID}/general/diagnosis`;
		return this.http.get<HealthConditionDto[]>(url);
	}

}
