import { Injectable } from '@angular/core';
//import { ContextService } from '@core/services/context.service';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ErrataService {

  constructor(
    //private contextService: ContextService,
    private readonly http: HttpClient
  ) { }

  createErrata(institutionId: number, errataData: any): Observable<any> {
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
    });

    // Usando institutionId recibido por parámetro en la URL
    const url = `/api/institution/${institutionId}/errata/create/`;

    // Enviando la solicitud POST con los datos errataData
    return this.http.post(url, errataData, { headers });
  }
}
