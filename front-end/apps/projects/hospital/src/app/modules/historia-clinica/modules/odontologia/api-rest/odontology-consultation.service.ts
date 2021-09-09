import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { OdontologyConsultationDto } from '@api-rest/api-model';
import { ContextService } from '@core/services/context.service';
import { environment } from '@environments/environment';
import { Observable } from 'rxjs';

@Injectable({
	providedIn: 'root'
})
export class OdontologyConsultationService {

	private readonly BASE_URL = `${environment.apiBase}/institutions/${this.contextService.institutionId}`


	constructor(
		private contextService: ContextService,
		private http: HttpClient,
	) { }

	createConsultation(patientId: number, odontologyConsultationDto: OdontologyConsultationDto): Observable<Boolean> {
		const url = `${this.BASE_URL}/patient/${patientId}/odontology/consultation`;
		return this.http.post<Boolean>(url, odontologyConsultationDto);
	}
}


