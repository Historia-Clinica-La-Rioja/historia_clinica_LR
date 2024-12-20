import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { OdontologyConsultationDto, OdontologyConsultationIndicesDto } from '@api-rest/api-model';
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

	createConsultation(patientId: number, odontologyConsultationDto: OdontologyConsultationDto, selectedFiles?: File[]): Observable<any> {
		const formData = new FormData();
		if (selectedFiles)
			Array.from(selectedFiles).forEach(file => formData.append('serviceRequestFiles', file));
		formData.append('createConsultationDto', new Blob([JSON.stringify(odontologyConsultationDto)], {type: 'application/json'}))
		const url = `${this.BASE_URL}/patient/${patientId}/odontology/consultation`;
		return this.http.post<boolean>(url, formData);
	}

	getConsultationIndices(patientId: number): Observable<OdontologyConsultationIndicesDto[]> {
		const url = `${this.BASE_URL}/patient/${patientId}/odontology/consultation/indices`;
		return this.http.get<OdontologyConsultationIndicesDto[]>(url);
	}
}


