import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import { environment } from "@environments/environment";
import { HttpClient } from "@angular/common/http";
import { PatientSearchDto } from '@api-rest/api-model';

@Injectable({
  providedIn: 'root'
})
export class SearchPatientService {

  constructor(private http: HttpClient) { }

  getPatientByCMD(params): Observable<any> {
		let url = `${environment.apiBase}/patient/search`;
		return this.http.get<PatientSearchDto[]>(url, {params: {'searchFilterStr' :params}});
  }
}

