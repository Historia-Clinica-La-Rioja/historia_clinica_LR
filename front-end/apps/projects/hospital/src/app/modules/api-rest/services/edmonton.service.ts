import { Injectable } from '@angular/core';
import { ContextService } from '@core/services/context.service';
import { environment } from '@environments/environment.prod';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { EdMontonAnswers, EdMontonSummary } from '@api-rest/api-model';

@Injectable({
providedIn: 'root'
})

export class EdmontonService {

constructor(
  private http: HttpClient,
  private contextService: ContextService,
  
  
) { }

crearEdMonton(patientId: number, institutionId: number, datos: any): Observable<boolean>{
  console.log("parametros en servicio")
  console.log(datos);
  console.log("la id del paciente",patientId)
  console.log("la id de la institucion",this.contextService.institutionId)
  const url = `${environment.apiBase}/institutions/${institutionId}/patient/${patientId}/hce/general-state/edmonton`;
  return this.http.post<any>(url, datos);
}

getEdMonton(institutionId: number, patientId: number): Observable<EdMontonAnswers[]> {
  const url = `${environment.apiBase}/institution/${institutionId}/patient/${patientId}/hce/general-state/edMonton `;
  return this.http.get<EdMontonAnswers[]>(url);
}

getEdMontonSummary(institutionId: number, patientId: number, edmontonId: number): Observable<EdMontonSummary> {
  console.log('institution: ', institutionId)
  const url = `${environment.apiBase}/institution/${institutionId}/patient/${patientId}/hce/general-state/summary/edMonton/${edmontonId}`;
  return this.http.get<EdMontonSummary>(url);
}



}
