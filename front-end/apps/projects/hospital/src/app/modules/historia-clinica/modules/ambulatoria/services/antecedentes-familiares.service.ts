import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, map } from 'rxjs';
import { environment } from '@environments/environment.prod';
import { ContextService } from '@core/services/context.service';
import { CreateQuestionnaireDTO} from '@api-rest/api-model';

@Injectable({
  providedIn: 'root'
})
export class FamilyRecordService {
  
  constructor(
    private contextService: ContextService,
    private readonly http: HttpClient,
  ) { }

  enviarFamilyRecord(patientId: number, familyRecord: CreateQuestionnaireDTO): Observable<any> {
    const url = `${environment.apiBase}/institution/${this.contextService.institutionId}/patient/${patientId}/hce/general-state/familybg`;
    //const url = `${environment.apiBase}/institution/2/patient/1/hce/general-state/familybg`;
    return this.http.post(url, familyRecord);
  }
  // getPdf(questionnaireResponseId: number): Observable<Blob> {
  //   const url = `${environment.apiBase}/institution/2/questionnaire/${questionnaireResponseId}/get-pdf`; 
  //   return this.http.get(url, { responseType: 'blob' });
  // }

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
