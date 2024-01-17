import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { FrailSummary, FrailAnswers } from '@api-rest/api-model';
import { environment } from '@environments/environment';
import { ContextService } from '@core/services/context.service';

@Injectable({
  providedIn: 'root'
})
export class FrailService {

  constructor(
    private contextService: ContextService,
    private readonly http: HttpClient) { }

  createFrail(patientId: number, frailData: any): Observable<boolean> {
    const url = `${environment.apiBase}/institution/${this.contextService.institutionId}/patient/${patientId}/hce/general-state/frail`;
    return this.http.post<boolean>(url, frailData);
  }

  getFrailSummary(institutionId: number, patientId: number, questionnaireId: number): Observable<FrailSummary> {
    const url = `${environment.apiBase}/institution/${institutionId}/patient/${patientId}/hce/general-state/summary/frail/${questionnaireId}`;
    return this.http.get<FrailSummary>(url);
  }

  getAllByPatientId(patientId: number): Observable<FrailAnswers[]> {
    const url = `${environment.apiBase}/institution/${this.contextService.institutionId}/patient/${patientId}/all-questionnaire-responses`;
    return this.http.get<FrailAnswers[]>(url);
  }

  getPdf(questionnaireResponseId: number): Observable<Blob> {
    const url = `${environment.apiBase}/institution/${this.contextService.institutionId}/questionnaire/${questionnaireResponseId}/get-pdf`;
    return this.http.get(url, { responseType: 'blob' });

  }

}
