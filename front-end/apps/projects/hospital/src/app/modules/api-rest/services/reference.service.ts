import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ReferenceDto } from '@api-rest/api-model';
import { ContextService } from '@core/services/context.service';
import { environment } from '@environments/environment';
import { Observable } from 'rxjs';

@Injectable({
	providedIn: 'root'
})

export class ReferenceService {

	constructor(
		private readonly http: HttpClient,
		private readonly contextService: ContextService,
	) { }

	getReferences(patientId: number, clinicalSpecialtyIds: number[]): Observable<ReferenceDto[]> {
		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/reference/patient/${patientId}`;
		return this.http.get<ReferenceDto[]>(url, {
			params: {
				clinicalSpecialtyIds
			}
		});
	}
}
