import { Injectable } from '@angular/core';
import { environment } from '@environments/environment.prod';
import { HttpClient } from '@angular/common/http';
import { Observable, map } from 'rxjs';
import { EdMontonAnswers, EdMontonSummary } from '@api-rest/api-model';
import { CreateQuestionnaireDTO} from '@api-rest/api-model';
import { ContextService } from '@core/services/context.service';


@Injectable({
  providedIn: 'root'
})
export class EdmontonService {

  constructor(
    private contextService: ContextService,
    private readonly http: HttpClient,
  ) { }

  createEdmonton(patientId: number, edmontonData: CreateQuestionnaireDTO): Observable<boolean> {
    const url = `${environment.apiBase}/institution/${this.contextService.institutionId}/patient/${patientId}/hce/general-state/edmonton`; 
    return this.http.post<boolean>(url, edmontonData);
  }

  getEdMonton(institutionId: number, patientId: number): Observable<EdMontonAnswers> {
    const url = `${environment.apiBase}/institution/${institutionId}/patient/${patientId}/hce/general-state/edmonton `;
    return this.http.get<EdMontonAnswers>(url);
  }

  getEdMontonSummary(institutionId: number, patientId: number, edmontonId: number): Observable<EdMontonSummary> {
    console.log('institution: ', institutionId)
    const url = `${environment.apiBase}/institution/${institutionId}/patient/${patientId}/hce/general-state/summary/edmonton/${edmontonId}`;
    return this.http.get<EdMontonSummary>(url);
  }

  getAllQuestionnaireResponses(institutionId: string, patientId: string): Observable<any[]> {
    const url = `${environment.apiBase}/institution/${this.contextService.institutionId}/patient/${patientId}/all-questionnaire-responses`;
    return this.http.get<any[]>(url);
  }

  getPdf(institutionId: string, questionnaireResponseId: string): Observable<Blob> {
    const url = `${environment.apiBase}/institution/${this.contextService.institutionId}/questionnaire/${questionnaireResponseId}/get-pdf`;
    return this.http.get(url, { responseType: 'blob' });
  }

  getQuestionnaireId(institutionId: string, patientId: string): Observable<number> {
    return this.getAllQuestionnaireResponses(institutionId, patientId).pipe(
      map((responses) => {
        // Aquí asumimos que estamos tomando el primer questionnaireId de la respuesta
        // Ajusta esto según la lógica específica de tu aplicación
        const firstResponse = responses[0];
        return firstResponse ? firstResponse.id : null;
      })
    );
  }


}