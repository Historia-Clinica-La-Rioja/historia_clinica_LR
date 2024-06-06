import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ProfessionalDto } from '@api-rest/api-model';
import { environment } from '@environments/environment';
import { Observable } from 'rxjs';

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

	getAllProfessionalsAndTechnicians(): Observable<ProfessionalDto[]> {
		const url = `${environment.apiBase}/healthcareprofessional/get-all-professionals-and-technicians`;
		return this.http.get<ProfessionalDto[]>(url);
	}

	getHealthcareProfessionalByUserId(): Observable<number> {
		const url = `${environment.apiBase}/healthcareprofessional/by-user-logged`;
		return this.http.get<number>(url);
	}

	geUserIdByHealthcareProfessional(healthcareProfesionalId: number): Observable<number> {
		const url = `${environment.apiBase}/healthcareprofessional/${healthcareProfesionalId}`;
		return this.http.get<number>(url);
	}

	getAllByDepartment(departmentId: number): Observable<ProfessionalDto[]> {
		const url = `${environment.apiBase}/healthcareprofessional/department/${departmentId}`;
		return this.http.get<ProfessionalDto[]>(url);
	}
}
