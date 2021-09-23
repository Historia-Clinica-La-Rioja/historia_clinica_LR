import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ExternalClinicalHistoryDto } from '@api-rest/api-model';
import { ContextService } from '@core/services/context.service';
import { environment } from '@environments/environment';
import { Observable } from 'rxjs';

@Injectable({
	providedIn: 'root'
})
export class ExternalClinicalHistoryService {

	constructor(
		private readonly http: HttpClient,
		private readonly contextService: ContextService
	) { }

	public getExternalClinicalHistoryList(patientId: number): Observable<ExternalClinicalHistoryDto[]> {
		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/patient/${patientId}/outpatient/consultations/getExternalClinicalHistoryList`;
		return this.http.get<ExternalClinicalHistoryDto[]>(url);
	}

}
