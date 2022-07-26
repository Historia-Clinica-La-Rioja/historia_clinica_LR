import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { HealthcareProfessionalCompleteDto, ProfessionalDto, ProfessionalProfessionsDto, ProfessionalSpecialtyDto } from '@api-rest/api-model';
import { ContextService } from '@core/services/context.service';
import { environment } from '@environments/environment';
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


	getProfessionsByProfessional(professionalId): Observable<ProfessionalProfessionsDto[]> {
		const url = `${environment.apiBase}/healthcareprofessionalspecialties/institution/${this.contextService.institutionId}/professional/${professionalId}`;
		return this.http.get<ProfessionalProfessionsDto[]>(url);
	}

	getList(): Observable<ProfessionalSpecialtyDto[]> {
		const url = `${environment.apiBase}/professionalSpecialty`;
		return this.http.get<ProfessionalSpecialtyDto[]>(url);
	}

}
