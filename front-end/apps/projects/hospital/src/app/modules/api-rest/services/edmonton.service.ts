import { Injectable } from '@angular/core';
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
  
) 
  {

  }

crearEdMonton(institutionId: number, patientId  : number, datos: any[]): Observable<boolean> {
  console.log("en servicio ", patientId )
  console.log("array", datos)

  const url = `${environment.apiBase}/institution/${institutionId}/patient/${patientId}/hce/general-state/edmonton`;
  return this.http.post<boolean>(url, datos);
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
