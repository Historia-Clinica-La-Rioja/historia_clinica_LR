import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { DiaryAvailableProtectedAppointmentsDto } from '@api-rest/api-model';
import { environment } from '@environments/environment';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class DiaryAvailableAppointmentsSearchService {

  constructor(
    private http: HttpClient,
  ) { }

  getAvailableProtectedAppointments(institutionId: number, filters: ProtectedAppointmentsFilter): Observable<DiaryAvailableProtectedAppointmentsDto[]> {
    const url = `${environment.apiBase}/institutions/${institutionId}/medicalConsultations/available-appointments/protected`;
    return this.http.post<DiaryAvailableProtectedAppointmentsDto[]>(url, filters);
  }
}

export interface ProtectedAppointmentsFilter {
  careLineId?: number,
  clinicalSpecialtyId: number,
  departmentId: number,
  initialSearchDate: {
    year: number,
    month: number,
    day: number
  },
  endSearchDate: {
    year: number,
    month: number,
    day: number
  }
}
