import { Injectable } from '@angular/core';
import { HttpClient } from "@angular/common/http";
import { Observable, of } from 'rxjs';
import { environment } from "@environments/environment";
import { ContextService } from '@core/services/context.service';
import { ProfessionalsByClinicalSpecialtyDto } from '@api-rest/api-model';

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
}
