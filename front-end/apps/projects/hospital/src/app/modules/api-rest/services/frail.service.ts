import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { FrailAnswers, FrailSummary } from '@api-rest/api-model'; 
import { environment } from '@environments/environment';
import { ContextService } from '@core/services/context.service';

@Injectable({
  providedIn: 'root'
})
export class FrailService {  

  constructor(
    private contextService: ContextService,
    private readonly http: HttpClient  ) {}

  createFrail(patientId: number, frailData: any): Observable<boolean> {
    console.log("frail", frailData )
    const url = `${environment.apiBase}/institution/${this.contextService.institutionId}/patient/${patientId}/hce/general-state/frail`; 
    return this.http.post<boolean>(url, frailData);
  }

  getFrailSummary(institutionId: number, patientId: number, frailId: number): Observable<FrailSummary> {
    const url = `${environment.apiBase}/institution/${institutionId}/patient/${patientId}/hce/general-state/summary/frail/${frailId}`;
    return this.http.get<FrailSummary>(url);
  }

  getAllByPatientId(institutionId: number, patientId: number): Observable<FrailAnswers[]> {
    const url = `${environment.apiBase}/institution/${institutionId}/patient/${patientId}/hce/general-state/frail`; 
    return this.http.get<FrailAnswers[]>(url);
  }

  getPdf(frailId: number) {
    const url = `${environment.apiBase}/institution/patient/outpatient/consultation/frail/${frailId}/pdf-download`; 
    return this.http.get<boolean>(url);
  
  }

}
