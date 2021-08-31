import { Injectable } from '@angular/core';
import { ContextService } from '@core/services/context.service';
import { HttpClient } from '@angular/common/http';
import {
	ImmunizePatientDto
} from '@api-rest/api-model';
import { Observable } from 'rxjs';
import { environment } from '@environments/environment';

@Injectable({
	providedIn: 'root'
})
export class ImmunizationService {

	constructor(
		private readonly http: HttpClient,
		private readonly contextService: ContextService
	) {
	}

	immunizePatient(immunizePatientDto: ImmunizePatientDto, patientId: number): Observable<boolean> {
		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/patient/${patientId}/immunize`;
		return this.http.post<boolean>(url, immunizePatientDto);
	}
}
