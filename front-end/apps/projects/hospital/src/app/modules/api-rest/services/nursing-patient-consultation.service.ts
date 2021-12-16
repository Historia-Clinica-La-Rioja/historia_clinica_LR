import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { NursingConsultationDto } from '@api-rest/api-model';
import { ContextService } from '@core/services/context.service';
import { environment } from '@environments/environment';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class NursingPatientConsultationService {

  constructor(
		private readonly http: HttpClient,
		private readonly contextService: ContextService
	) {
	}

	createNursingPatientConsultation(nursingConsultationDto: NursingConsultationDto, patientId: number): Observable<boolean> {
		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/patient/${patientId}/nursing/consultation`;
		return this.http.post<boolean>(url, nursingConsultationDto);
	}

}
