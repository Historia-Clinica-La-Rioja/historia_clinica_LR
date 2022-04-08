import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { ProfessionalDto } from '@api-rest/api-model';
import { environment } from '@environments/environment';

const BASIC_URL = '/healthcareprofessional';

@Injectable({
	providedIn: 'root'
})
export class HealthcareProfessionalService {

	constructor(
		private http: HttpClient
	) {
	}

	getAll(): Observable<ProfessionalDto[]> {
		const url = `${environment.apiBase}` + BASIC_URL;
		return this.http.get<ProfessionalDto[]>(url);
	}

	getHealthcareProfessionalByUserId(): Observable<number> {
		const url = `${environment.apiBase}/healthcareprofessional/by-user-logged`;
		return this.http.get<number>(url);
	}
}
