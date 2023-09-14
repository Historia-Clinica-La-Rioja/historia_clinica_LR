import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { InformerObservationDto, HCEDocumentDataDto, StudyAppointmentDto } from '@api-rest/api-model';
import { ContextService } from '@core/services/context.service';
import { environment } from '@environments/environment';
import { Observable } from 'rxjs';

@Injectable({
    providedIn: 'root'
})
export class StudyAppointmentReportService {

    constructor(
        private readonly http: HttpClient,
        private readonly contextService: ContextService
    ) {
    }

    getStudyByAppointmentId(appointmentId: number): Observable<StudyAppointmentDto> {
        const URL_BASE = `${environment.apiBase}/institutions/${this.contextService.institutionId}/studyAppointmentReport`;
        const url = `${URL_BASE}/study/by-appointment/${appointmentId}`;
        return this.http.get<StudyAppointmentDto>(url);
    }

    createDraftReport(appointmentId: number, informerObservations: InformerObservationDto): Observable<number> {
        const URL_BASE = `${environment.apiBase}/institutions/${this.contextService.institutionId}/studyAppointmentReport`;
        const url = `${URL_BASE}/createDraftReport/${appointmentId}`;
        return this.http.post<number>(url, informerObservations);
    }

    updateDraftReport(appointmentId: number, informerObservations: InformerObservationDto): Observable<number> {
        const URL_BASE = `${environment.apiBase}/institutions/${this.contextService.institutionId}/studyAppointmentReport`;
        const url = `${URL_BASE}/updateDraftReport/${appointmentId}`;
        return this.http.put<number>(url, informerObservations);
    }

    closeDraftReport(appointmentId: number, informerObservations: InformerObservationDto): Observable<number> {
        const URL_BASE = `${environment.apiBase}/institutions/${this.contextService.institutionId}/studyAppointmentReport`;
        const url = `${URL_BASE}/closeDraftReport/${appointmentId}`;
        return this.http.put<number>(url, informerObservations);
    }

    saveReport(appointmentId: number, informerObservations: InformerObservationDto): Observable<number> {
        const URL_BASE = `${environment.apiBase}/institutions/${this.contextService.institutionId}/studyAppointmentReport`;
        const url = `${URL_BASE}/saveReport/${appointmentId}`;
        return this.http.post<number>(url, informerObservations);
    }

    getFileInfo(appointmentId: number): Observable<HCEDocumentDataDto> {
        const URL_BASE = `${environment.apiBase}/institutions/${this.contextService.institutionId}/studyAppointmentReport`;
        const url = `${URL_BASE}/by-appointment/${appointmentId}`;
        return this.http.get<HCEDocumentDataDto>(url);
    }
}
