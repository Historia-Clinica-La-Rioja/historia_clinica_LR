import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import {PhysicalSummary, QuestionnairesResponses } from '@api-rest/api-model';
import { environment } from '@environments/environment';
import { ContextService } from '@core/services/context.service';

@Injectable({
  providedIn: 'root'
})
export class PhysicalPerformanceService {

  constructor(
    private contextService: ContextService,
    private readonly http: HttpClient) { }


  physicalPerformancePdf(questionnaireId: number): Observable<Blob> {
    const url = `${environment.apiBase}/institution/${this.contextService.institutionId}/questionnaire/${questionnaireId}/get-pdf`;
    return this.http.get(url, { responseType: 'blob' });

  }
  
  getPhysicalSummary( questionnaireResponseId: any): Observable<PhysicalSummary> {
    const url = `${environment.apiBase}/institution/${this.contextService.institutionId}/questionnaire/${questionnaireResponseId}/answers `;
    return this.http.get<PhysicalSummary>(url);
  }

  getAllByPatientId(patientId: number): Observable<QuestionnairesResponses[]> {
    const url = `${environment.apiBase}/institution/${this.contextService.institutionId}/patient/${patientId}/all-questionnaire-responses`;
    return this.http.get<QuestionnairesResponses[]>(url);
  }

  createPhysical(patientId: number, physicalData: any): Observable<boolean> {
    const url = `${environment.apiBase}/institution/${this.contextService.institutionId}/patient/${patientId}/questionnaire/create`;
    return this.http.post<boolean>(url, physicalData);

  }
}
