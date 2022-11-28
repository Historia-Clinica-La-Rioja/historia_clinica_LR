import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { environment } from '@environments/environment';
import { ContextService } from '@core/services/context.service';
import { ClinicalSpecialtyDto, ProfessionalsByClinicalSpecialtyDto } from '@api-rest/api-model';

@Injectable({
	providedIn: 'root'
})
export class ClinicalSpecialtyService {

	constructor(private http: HttpClient, private readonly contextService: ContextService) {
	}

	getClinicalSpecialty(professionalId): Observable<any[]> {
		const url = `${environment.apiBase}/institution/${this.contextService.institutionId}/clinicalspecialty/professional/${professionalId}`;
		return this.http.get<any[]>(url);
	}

	getClinicalSpecialties(professionalsIds: number[]): Observable<ProfessionalsByClinicalSpecialtyDto[]> {
		if (professionalsIds?.length) {
			const url = `${environment.apiBase}/institution/${this.contextService.institutionId}/clinicalspecialty/professional`;
			return this.http.get<ProfessionalsByClinicalSpecialtyDto[]>(url, {
				params: {
					professionalsIds: professionalsIds.join(',')
				}
			});

		}
		return of([]);
	}

	getAppointmentClinicalSpecialty(patientId): Observable<any> {
		const url = `${environment.apiBase}/institution/${this.contextService.institutionId}/clinicalspecialty/patient/${patientId}`;
		return this.http.get<any[]>(url);
	}

	getLoggedInProfessionalClinicalSpecialties(): Observable<ClinicalSpecialtyDto[]> {
		const url = `${environment.apiBase}/institution/${this.contextService.institutionId}/clinicalspecialty/loggedProfessionalClinicalSpecialty`;
		return this.http.get<any[]>(url);
	}

	getActiveDiariesByProfessionalsClinicalSpecialties(professionalsIds: number[]): Observable<ProfessionalsByClinicalSpecialtyDto[]> {
		if (professionalsIds?.length) {
			const url = `${environment.apiBase}/institution/${this.contextService.institutionId}/clinicalspecialty/diary/professional`;
			return this.http.get<ProfessionalsByClinicalSpecialtyDto[]>(url, {
				params: {
					professionalsIds: professionalsIds.join(',')
				}
			});

		}
		return of([]);
	}

	getClinicalSpecialtyByInstitution(institutionId: number): Observable<ClinicalSpecialtyDto[]> {
		const url = `${environment.apiBase}/institution/${this.contextService.institutionId}/clinicalspecialty/by-destination-institution`;
		let params = new HttpParams();
		params = params.append('destinationInstitutionId', institutionId);
		return this.http.get<ClinicalSpecialtyDto[]>(url, { params });
	}

	getAllByDestinationInstitution(careLineId: number, destinationInstitutionId: number): Observable<ClinicalSpecialtyDto[]> {
		const url = `${environment.apiBase}/institution/${this.contextService.institutionId}/clinicalspecialty/careline/${careLineId}/destinationinstitution/${destinationInstitutionId}`;
		return this.http.get<ClinicalSpecialtyDto[]>(url);
	}

}
