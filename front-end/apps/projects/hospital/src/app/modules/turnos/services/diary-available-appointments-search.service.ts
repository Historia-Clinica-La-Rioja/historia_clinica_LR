import { HttpClient, HttpParams } from '@angular/common/http';
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
    let queryParams: HttpParams = new HttpParams();
    queryParams = queryParams.append('diaryProtectedAppointmentsSearch', JSON.stringify(filters));

    const url = `${environment.apiBase}/institutions/${institutionId}/medicalConsultations/available-appointments/protected`;
    return this.http.get<DiaryAvailableProtectedAppointmentsDto[]>(url, { params: queryParams });
  }
}

export interface ProtectedAppointmentsFilter {
  careLineId?: number,
  clinicalSpecialtyId: number,
  departmentId: number,
  endSearchDate: string,
  initialSearchDate: string,
  institutionId?: number,
}
