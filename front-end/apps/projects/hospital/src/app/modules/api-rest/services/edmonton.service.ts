import { Injectable } from '@angular/core';
import { environment } from '@environments/environment.prod';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
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

}