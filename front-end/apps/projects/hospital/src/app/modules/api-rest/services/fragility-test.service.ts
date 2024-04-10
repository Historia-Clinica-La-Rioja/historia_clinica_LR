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
    const url = `${environment.apiBase}/institution/${this.contextService.institutionId}/patient/${patientId}/questionnaire/create`;
    return this.http.post<boolean>(url, frailData);
  }

  getFrailSummary(institutionId: number, patientId: number, questionnaireId: number): Observable<FrailSummary> {
    const url = `${environment.apiBase}/institution/${institutionId}/patient/${patientId}/hce/general-state/summary/frail/${questionnaireId}`;
    return this.http.get<FrailSummary>(url);
  }

  getAllByPatientId(institutionId: number, patientId: number): Observable<FrailAnswers[]> {
    const url = `${environment.apiBase}/institution/${institutionId}/patient/${patientId}/hce/general-state/frail`; 
    return this.http.get<FrailAnswers[]>(url);
  }

  getPdf(questionnaireId: number): Observable<Blob> {
    const url = `${environment.apiBase}/institution/patient/outpatient/consultation/frail/${questionnaireId}/pdf-download`; 
    return this.http.get(url, { responseType: 'blob' });
    
  }

}
