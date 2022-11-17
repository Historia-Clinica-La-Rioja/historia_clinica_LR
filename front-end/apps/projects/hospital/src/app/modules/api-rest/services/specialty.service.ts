import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ClinicalSpecialtyDto, ProfessionalsByClinicalSpecialtyDto } from '@api-rest/api-model';
import { environment } from '@environments/environment';
import { Observable } from 'rxjs/internal/Observable';

@Injectable({
	providedIn: 'root'
})
export class SpecialtyService {

	constructor(private http: HttpClient) { }

	getAll(): Observable<ClinicalSpecialtyDto[]> {
		const url = `${environment.apiBase}/clinicalSpecialty`;
		return this.http.get<ClinicalSpecialtyDto[]>(url);
	}

	getAllSpecialtyByProfessional(institutionId, professionalId): Observable<ProfessionalsByClinicalSpecialtyDto[]> {
		const url = `${environment.apiBase}/institution/${institutionId}/clinicalspecialty/professional/${professionalId}`;
		return this.http.get<ProfessionalsByClinicalSpecialtyDto[]>(url);
	}
}
