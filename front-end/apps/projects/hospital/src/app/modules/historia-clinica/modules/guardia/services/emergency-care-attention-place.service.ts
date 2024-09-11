import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { EmergencyCareAttentionPlaceDto, EmergencyCareBedDetailDto, EmergencyCareDoctorsOfficeDetailDto, EmergencyCareShockRoomDetailDto } from '@api-rest/api-model';
import { ContextService } from '@core/services/context.service';
import { environment } from '@environments/environment';
import { Observable } from 'rxjs';

@Injectable({
	providedIn: 'root'
})
export class EmergencyCareAttentionPlaceService {

	private BASE_URL: string;

	constructor(
		private http: HttpClient,
		private contextService: ContextService,
	) {
		this.contextService.institutionId$.subscribe(institutionId =>
			this.BASE_URL = `${environment.apiBase}/institution/${institutionId}/emergency-care/attention-places`
		)
	}

	getAttentionPlaces(): Observable<EmergencyCareAttentionPlaceDto[]> {
		const url = `${this.BASE_URL}`;
		return this.http.get<EmergencyCareAttentionPlaceDto[]>(url);
	}

	getBedDetails(bedId: number): Observable<EmergencyCareBedDetailDto> {
		const url = `${this.BASE_URL}/bed/${bedId}`;
		return this.http.get<EmergencyCareBedDetailDto>(url);
	}

	getShockRoomDetails(shockroomId: number): Observable<EmergencyCareShockRoomDetailDto> {
		const url = `${this.BASE_URL}/shockroom/${shockroomId}`;
		return this.http.get<EmergencyCareShockRoomDetailDto>(url);
	}

	getDoctorOfficeDetails(doctorOfficeId: number): Observable<EmergencyCareDoctorsOfficeDetailDto> {
		const url = `${this.BASE_URL}/doctors-office/${doctorOfficeId}`;
		return this.http.get<EmergencyCareDoctorsOfficeDetailDto>(url);
	}
}

