import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ClinicalSpecialtyDto } from '@api-rest/api-model';
import { ContextService } from '@core/services/context.service';
import { environment } from '@environments/environment';
import { Observable } from 'rxjs';

@Injectable({
	providedIn: 'root'
})
export class ClinicalSpecialtyCareLineService {

	constructor(
		private readonly http: HttpClient,
		private readonly contextService: ContextService,
	) { }

	getSpecialtyCareLine(careLineId): Observable<ClinicalSpecialtyDto[]> {
		const url = `${environment.apiBase}/institution/${this.contextService.institutionId}/careline/${careLineId}/clinicalspecialties`;
		return this.http.get<ClinicalSpecialtyDto[]>(url);
	}

}
