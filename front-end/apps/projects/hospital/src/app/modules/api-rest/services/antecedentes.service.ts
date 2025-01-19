import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { QuestionnairesResponses, AntecedentesSummary } from '@api-rest/api-model';
import { environment } from '@environments/environment';
import { ContextService } from '@core/services/context.service';

@Injectable({
  providedIn: 'root'
})
export class AntecedentesServices {  

  constructor(
    private contextService: ContextService,
    private readonly http: HttpClient  ) {}

  createAntecedecentes(patientId: number, antecedentesData: any): Observable<any> {
    const url = `${environment.apiBase}/institution/${this.contextService.institutionId}/patient/${patientId}/questionnaire/create`;
    return this.http.post<any>(url, antecedentesData);
  }

  getAntecedentesSummary(questionnaireResponseId: any): Observable<AntecedentesSummary> {
    const url = `${environment.apiBase}/institution/${this.contextService.institutionId}/questionnaire/${questionnaireResponseId}/answers`;
    return this.http.get<AntecedentesSummary>(url);
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
