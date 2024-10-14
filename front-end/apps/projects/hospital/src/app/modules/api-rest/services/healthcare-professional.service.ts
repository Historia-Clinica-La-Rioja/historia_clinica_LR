import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ProfessionalDto } from '@api-rest/api-model';
import { ContextService } from '@core/services/context.service';
import { environment } from '@environments/environment';
import { Observable } from 'rxjs';

const BASIC_URL = '/healthcareprofessional';

@Injectable({
	providedIn: 'root'
})
export class HealthcareProfessionalService {

	constructor(
		private http: HttpClient,
		private readonly contextService: ContextService
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

	getAllProfessionalsAndTechniciansByInstitution(): Observable<ProfessionalDto[]> {
		const url = `${environment.apiBase}/healthcareprofessional/institution/${this.contextService.institutionId}/get-all-professionals-and-technicians`;
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

	getAllUsingfilters(filters: ProfessionalFilters): Observable<ProfessionalDto[]> {
		let queryParams: HttpParams = new HttpParams();
		queryParams = queryParams.append('searchFilter', JSON.stringify(filters));

		const url = `${environment.apiBase}/healthcareprofessional/by-filter`;
		return this.http.get<ProfessionalDto[]>(url, { params: queryParams });
	}
}

export interface ProfessionalFilters {
	departmentId: number,
	institutionId?: number,
	clinicalSpecialtyId?: number,
	practiceId?: number,
}
