import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { VirtualConsultationDto, VirtualConsultationFilterDto, VirtualConsultationNotificationDataDto, VirtualConsultationRequestDto, VirtualConsultationResponsibleDataDto, VirtualConsultationStatusDto } from '@api-rest/api-model';
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
		return this.http.get<VirtualConsultationNotificationDataDto>(url)
	}

	getDomainVirtualConsultation(institutionId: number, searchCriteria: VirtualConsultationFilterDto): Observable<VirtualConsultationDto[]> {
		const url = `${this.BASE_URL}/institution/${institutionId}/domain`;
		const params = { filter: JSON.stringify(searchCriteria) };
		return this.http.get<VirtualConsultationDto[]>(url,{params})
	}

	changeResponsibleAttentionState(institutionId: number, attentionValue: boolean): Observable<any> {
		const url = `${this.BASE_URL}/${institutionId}/change-responsible-state`;
		return this.http.post(url, attentionValue)
	}

	getVirtualConsultation(id: number): Observable<VirtualConsultationDto> {
		const url = `${this.BASE_URL}/${id}`;
		return this.http.get<VirtualConsultationDto>(url)
	}

	changeVirtualConsultationState(id: number, status: VirtualConsultationStatusDto) {
		const url = `${this.BASE_URL}/${id}/state`;
		return this.http.put(url, status)
	}

	saveVirtualConsultationRequest(institutionId: number, virtualConsultation: VirtualConsultationRequestDto) {
		const url = `${this.BASE_URL}/${institutionId}`;
		return this.http.post(`${url}`, virtualConsultation)
	}

	changeClinicalProfessionalAvailability(availability: boolean) {
		const url = `${this.BASE_URL}/change-clinical-professional-state`;
		return this.http.post(url, availability)
	}

	getProfessionalAvailability(): Observable<boolean> {
		const url = `${this.BASE_URL}/professional-availability`;
		return this.http.get<boolean>(url)
	}

	getResponsibleStatus(institutionId: number): Observable<boolean> {
		const url = `${this.BASE_URL}/institution/${institutionId}/responsible-professional-availability`;
		return this.http.get<boolean>(url)
	}

	getVirtualConsultationsByInstitution(institutionId: number, searchCriteria: VirtualConsultationFilterDto): Observable<VirtualConsultationDto[]> {
		const url = `${this.BASE_URL}/institution/${institutionId}`;
		const params = { filter: JSON.stringify(searchCriteria) };
		return this.http.get<VirtualConsultationDto[]>(url,{params})
	}

	notifyVirtualConsultationIncomingCall(virtualConsultation: number) {
		const url = `${this.BASE_URL}/notify-incoming-call/${virtualConsultation}`;
		return this.http.post(url, {})
	}

	notifyVirtualConsultationCancelledCall(virtualConsultation: number) {
		const url = `${this.BASE_URL}/notify-cancelled-call/${virtualConsultation}`;
		return this.http.post(url, {})
	}

	notifyVirtualConsultationRejectedCall(virtualConsultation: number) {
		const url = `${this.BASE_URL}/notify-rejected-call/${virtualConsultation}`;
		return this.http.post(url, {})
	}

	notifyVirtualConsultationAcceptedCall(virtualConsultation: number) {
		const url = `${this.BASE_URL}/notify-accepted-call/${virtualConsultation}`;
		return this.http.post(url, {})
	}

	transferResponsibleProfessionaltOfVirtualConsultation(virtualConsultationId : number,responsibleHealthcareProfessionalId:number){
		const url = `${this.BASE_URL}/${virtualConsultationId}/transfer`;
		return this.http.put(url,responsibleHealthcareProfessionalId);
	}

	getResponsibleProfessional(responsibleHealthcareProfessionalId:number, institutionId:number):Observable<VirtualConsultationResponsibleDataDto>{
		const url = `${this.BASE_URL}/institution/${institutionId}/get-responsible-healthcare-professional/${responsibleHealthcareProfessionalId}`;
		return this.http.get<VirtualConsultationResponsibleDataDto>(url);
	}
}

