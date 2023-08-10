import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { VirtualConsultationDto, VirtualConsultationNotificationDataDto, VirtualConsultationRequestDto } from '@api-rest/api-model';
import { Observable } from 'rxjs';
import { environment } from '@environments/environment';

@Injectable({
	providedIn: 'root'
})
export class VirtualConstultationService {


	private BASE_URL = `${environment.apiBase}/virtual-consultation`;

	constructor(
		private http: HttpClient,
	) {

	}

	notifyVirtualConsultationCall(virtualConsultationId: number): Observable<void> {
		return this.http.post<void>(`${this.BASE_URL}/notify/${virtualConsultationId}`, {});
	}

	getVirtualConsultationCall(virtualConsultationId: number): Observable<VirtualConsultationNotificationDataDto> {
		const url = `${this.BASE_URL}/notification/${virtualConsultationId}`
		return this.http.get<VirtualConsultationNotificationDataDto>(`${url}`)
	}

	getDomainVirtualConsultation(): Observable<VirtualConsultationDto[]> {
		const url = `${this.BASE_URL}/domain`;
		return this.http.get<VirtualConsultationDto[]>(`${url}`)
	}

	changeResponsibleAttentionState(institutionId: number, attentionValue: boolean): Observable<any> {
		const url = `${this.BASE_URL}/${institutionId}/change-responsible-state`;
		return this.http.post(`${url}`, attentionValue)
	}

	getVirtualConsultation(id:number): Observable<VirtualConsultationDto> {
		const url = `${this.BASE_URL}/${id}`;
		return this.http.get<VirtualConsultationDto>(`${url}`)
	}

	saveVirtualConsultationRequest(institutionId: number, virtualConsultation: VirtualConsultationRequestDto) {
		const url = `${this.BASE_URL}/${institutionId}`;
		return this.http.post(`${url}`, virtualConsultation)
	}
}

