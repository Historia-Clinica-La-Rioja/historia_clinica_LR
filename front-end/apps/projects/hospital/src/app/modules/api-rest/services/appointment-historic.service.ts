import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { PatientAppointmentHistoryDto } from '@api-rest/api-model';
import { ContextService } from '@core/services/context.service';
import { environment } from '@environments/environment';
import { PaginatorDocumentData } from '@pacientes/dialogs/appointment-historic/appointment-historic.component';
import { Observable } from 'rxjs';

@Injectable({
    providedIn: 'root'
})
export class AppointmentHistoricService {

    private readonly BASE_URL: string;

    constructor(
        private http: HttpClient,
        private contextService: ContextService,
    ) { 
        this.BASE_URL = `${environment.apiBase}/institution/${this.contextService.institutionId}/appointment-history`;
    }

    getAppointmentHistoric(pageNumber: number, patientId: number): Observable<PaginatorDocumentData<PatientAppointmentHistoryDto[]>> {
        const url = `${this.BASE_URL}/patient/${patientId}/by-professional-diaries`;
        let queryParam: HttpParams = new HttpParams();
        queryParam = queryParam.append('page', pageNumber);
        return this.http.get<PaginatorDocumentData<PatientAppointmentHistoryDto[]>>(url, { params: queryParam });
    }
}
