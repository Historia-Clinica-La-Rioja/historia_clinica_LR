import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { QuestionnairesResponses, FrailSummary } from '@api-rest/api-model';
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
    const url = `${environment.apiBase}/institution/{institutionId}/questionnaire/{questionnaireResponseId}/answers`;
    return this.http.get<FrailSummary>(url);
  }

  getAllByPatientId(patientId: number): Observable<QuestionnairesResponses[]> {
    const url = `${environment.apiBase}/institution/${this.contextService.institutionId}/patient/${patientId}/all-questionnaire-responses`;
    return this.http.get<QuestionnairesResponses[]>(url);
  }

  getPdf(questionnaireId: number): Observable<Blob> {
    const url = `${environment.apiBase}/institution/${this.contextService.institutionId}/questionnaire/${questionnaireId}/get-pdf`;
    return this.http.get(url, { responseType: 'blob' });
  }

}
