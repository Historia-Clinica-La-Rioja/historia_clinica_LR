import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { DiaryAvailableProtectedAppointmentsDto, EAppointmentModality } from '@api-rest/api-model';
import { ContextService } from '@core/services/context.service';
import { environment } from '@environments/environment';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class DiaryAvailableAppointmentsSearchService {

  URL_PREFIX = 'medicalConsultations/available-appointments';

  constructor(
    private http: HttpClient,
	  private readonly contextService: ContextService,

  ) { }

  getAvailableProtectedAppointments(institutionId: number, filters: ProtectedAppointmentsFilter): Observable<DiaryAvailableProtectedAppointmentsDto[]> {
    let queryParams: HttpParams = new HttpParams();
    queryParams = queryParams.append('diaryProtectedAppointmentsSearch', JSON.stringify(filters));

    const url = `${environment.apiBase}/institutions/${institutionId}/medicalConsultations/available-appointments/protected`;
    return this.http.get<DiaryAvailableProtectedAppointmentsDto[]>(url, { params: queryParams });
  }

  getAvailableProtectedAppointmentsQuantity(institutionDestinationId: number, clinicalSpecialtyId: number, departmentId: number,careLineId: number): Observable<number> {
	const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/medicalConsultations/available-appointments/protected-quantity`;

	const queryParams = new HttpParams()
	  .append('institutionDestinationId', JSON.stringify(institutionDestinationId))
	  .append('clinicalSpecialtyId', JSON.stringify(clinicalSpecialtyId))
	  .append('careLineId', JSON.stringify(careLineId))
	  .append('departmentId', JSON.stringify(departmentId));

	return this.http.get<number>(url, { params: queryParams });
  }

  getAvailableAppiuntmentsQuantityByCarelineDiaries(institutionDestinationId: number, careLineId: number, practiceSnomedId: number, clinicalSpecialtyId: number): Observable<number> {
    console.log('ESTOY EN LA FUNCION')
    const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/${this.URL_PREFIX}/quantity/by-careline-diaries`;

    let queryParams = new HttpParams()
      .append('institutionDestinationId', JSON.stringify(institutionDestinationId))
      .append('careLineId', JSON.stringify(careLineId));

    if (practiceSnomedId)
      queryParams = queryParams.append('practiceSnomedId', JSON.stringify(practiceSnomedId));
      
    else 
      queryParams = queryParams.append('clinicalSpecialtyId', JSON.stringify(clinicalSpecialtyId));
    
    return this.http.get<number>(url, { params: queryParams });
  }

  getAvailableAppointmentsQuantity(institutionDestinationId: number, clinicalSpecialtyId: number): Observable<number> {
	const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/medicalConsultations/available-appointments/quantity/by-reference-filter`;

	const queryParams = new HttpParams()
	  .append('institutionDestinationId', JSON.stringify(institutionDestinationId))
	  .append('clinicalSpecialtyId', JSON.stringify(clinicalSpecialtyId));

	return this.http.get<number>(url, { params: queryParams });
  }
}

export interface ProtectedAppointmentsFilter {
  careLineId?: number,
  clinicalSpecialtyId: number,
  departmentId: number,
  endSearchDate: string,
  initialSearchDate: string,
  institutionId?: number,
  modality:EAppointmentModality,
  practiceId?: number,
}
