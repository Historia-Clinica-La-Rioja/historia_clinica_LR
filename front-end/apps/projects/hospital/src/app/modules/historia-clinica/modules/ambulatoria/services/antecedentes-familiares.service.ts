import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
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

  // getEdMonton(institutionId: number, patientId: number): Observable<EdMontonAnswers> {
  //   const url = `${environment.apiBase}/institution/${institutionId}/patient/${patientId}/hce/general-state/edmonton `;
  //   return this.http.get<EdMontonAnswers>(url);
  // }

  // getEdMontonSummary(institutionId: number, patientId: number, edmontonId: number): Observable<EdMontonSummary> {
  //   console.log('institution: ', institutionId)
  //   const url = `${environment.apiBase}/institution/${institutionId}/patient/${patientId}/hce/general-state/summary/edmonton/${edmontonId}`;
  //   return this.http.get<EdMontonSummary>(url);
}
