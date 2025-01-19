import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ClinicHistoryAccessDto } from '@api-rest/api-model';
import { environment } from '@environments/environment';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ClinicHistoryAccessService {

  private readonly BASE_URL: string;

  constructor(
    private readonly http: HttpClient,
    ) {
    this.BASE_URL = `${environment.apiBase}/institutions`;
   }

  isValid(patientId: number, institutionId: number):Observable<boolean> {
    const url = `${this.BASE_URL}/${institutionId}/patient/${patientId}/clinic-history-access`;
		return this.http.get<boolean>(url);
  }

  saveAudit(patientId: number, clinicHistoryAccessDto: ClinicHistoryAccessDto, institutionId: number ):Observable<boolean> {
    const url = `${environment.apiBase}/institutions/${institutionId}/patient/${patientId}/clinic-history-access`;
    return  this.http.post<boolean>(url, clinicHistoryAccessDto);
  }
}
