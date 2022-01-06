import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { HealthcareProfessionalCompleteDto, HealthcareProfessionalSpecialtyDto, ProfessionalDto, ProfessionalsByClinicalSpecialtyDto } from '@api-rest/api-model';
import { ContextService } from '@core/services/context.service';
import { environment } from '@environments/environment';
import { ProfessionDto } from '@pacientes/dialogs/edit-professions/edit-professions.component';
import { Observable } from 'rxjs';

@Injectable({
	providedIn: 'root'
})
export class ProfessionalService {

	constructor(private http: HttpClient, private contextService: ContextService) { }

	get(personId): Observable<ProfessionalDto> {
		const url = `${environment.apiBase}/healthcareprofessional/institution/${this.contextService.institutionId}/person/${personId}`;
		return this.http.get<ProfessionalDto>(url);
	}

	addProfessional(professional: HealthcareProfessionalCompleteDto): Observable<number> {
		const url = `${environment.apiBase}/healthcareprofessional/institution/${this.contextService.institutionId}`;
		return this.http.post<number>(url, professional);
	}

	editProfessional(healthcareProfessionalId: number, professional: HealthcareProfessionalCompleteDto): Observable<boolean> {
		const url = `${environment.apiBase}/healthcareprofessional/${healthcareProfessionalId}/institution/${this.contextService.institutionId}`;
		return this.http.put<boolean>(url, professional);
	}

	getProfessionsByProfessional(professionalId): Observable<HealthcareProfessionalSpecialtyDto[]> {
		const url = `${environment.apiBase}/healthcareprofessionalspecialties/institution/${this.contextService.institutionId}/professional/${professionalId}`;
		return this.http.get<HealthcareProfessionalSpecialtyDto[]>(url);
	}

	getList(): Observable<ProfessionDto[]> {
		const url = `${environment.apiBase}/professionalSpecialty`;
		return this.http.get<ProfessionDto[]>(url);
	}

}
