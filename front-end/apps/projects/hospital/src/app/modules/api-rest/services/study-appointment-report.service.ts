import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { InformerObservationDto, StudyAppointmentDto } from '@api-rest/api-model';
import { ContextService } from '@core/services/context.service';
import { environment } from '@environments/environment';
import { Observable } from 'rxjs';

@Injectable({
	providedIn: 'root'
})
export class StudyAppointmentReportService {

	private readonly URL_BASE: string;

	constructor(
		private readonly http: HttpClient,
		private readonly contextService: ContextService
	) {
		this.URL_BASE = `${environment.apiBase}/institutions/${this.contextService.institutionId}/studyAppointmentReport`;
	}

	getStudyByAppointmentId(appointmentId: number): Observable<StudyAppointmentDto> {
		const url = `${this.URL_BASE}/study/by-appointment/${appointmentId}`;
		return this.http.get<StudyAppointmentDto>(url);
	}

	createDraftReport(appointmentId: number, informerObservations: InformerObservationDto): Observable<number> {
		const url = `${this.URL_BASE}/createDraftReport/${appointmentId}`;
		return this.http.post<number>(url, informerObservations);
	}

	updateDraftReport(appointmentId: number, informerObservations: InformerObservationDto): Observable<number> {
		const url = `${this.URL_BASE}/updateDraftReport/${appointmentId}`;
		return this.http.put<number>(url, informerObservations);
	}

	closeDraftReport(appointmentId: number, informerObservations: InformerObservationDto): Observable<number> {
		const url = `${this.URL_BASE}/closeDraftReport/${appointmentId}`;
		return this.http.put<number>(url, informerObservations);
	}

	saveReport(appointmentId: number, informerObservations: InformerObservationDto): Observable<number> {
		const url = `${this.URL_BASE}/saveReport/${appointmentId}`;
		return this.http.post<number>(url, informerObservations);
	}
}
