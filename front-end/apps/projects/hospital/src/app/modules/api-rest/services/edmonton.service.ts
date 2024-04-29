import { Injectable } from '@angular/core';
import { environment } from '@environments/environment.prod';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { EdmontonAnswers, EdmontonSummary } from '@api-rest/api-model';
import { ContextService } from '@core/services/context.service';
import { catchError } from 'rxjs/operators';
import { throwError } from 'rxjs';
import { HttpHeaders } from '@angular/common/http';



@Injectable({
  providedIn: 'root'
})
export class EdmontonService {

  constructor(
    private contextService: ContextService,
    private readonly http: HttpClient,
  ) { }

  edmontonGetPdf(questionnaireResponseId: any): Observable<Blob> {
    const url = `${environment.apiBase}/institution/${this.contextService.institutionId}/questionnaire/${questionnaireResponseId}/get-pdf`; 
    return this.http.get(url, { responseType: 'blob' });
    
  }
  getEdmonton( questionnaireResponseId: any): Observable<EdmontonAnswers> {
    const url = `${environment.apiBase}/institution/${this.contextService.institutionId}/questionnaire/${questionnaireResponseId}/answers `;
    return this.http.get<EdmontonAnswers>(url);
  }

  getEdmontonSummary(patientId: number): Observable<EdmontonSummary> {
    const url = `${environment.apiBase}/institution/${this.contextService.institutionId}/patient/${patientId}/all-questionnaire-responses`;
    return this.http.get<EdmontonSummary>(url);
  }

  createEdmonton(patientId: number, edmontonData: any): Observable<any> {
    const url = `${environment.apiBase}/institution/${this.contextService.institutionId}/patient/${patientId}/questionnaire/create`; 
  
    const headers = new HttpHeaders({
      'Content-Type': 'application/json' // Ajusta el tipo de contenido según lo que el servidor espera  

      //Sin esta variable "headers", salta un error 415 de formato no aceptado, este manifiesta que el contenido enviado es
      // un json y asi es aceptado por el servidor.
      
    });
    return this.http.post<any>(url, edmontonData, { headers }).pipe(
      catchError((error: any) => {
        console.error('Error en la solicitud:', error);
        return throwError('Error al crear Edmonton');
      })
    );
  }
}