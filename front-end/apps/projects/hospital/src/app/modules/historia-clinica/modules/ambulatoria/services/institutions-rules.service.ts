import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '@environments/environment';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class InstitutionsRulesService {

  constructor(private http: HttpClient,) { }

  validateRegulation(institutionIdToValidate: number, clinicalSpecialtys: number[], practice: number): Observable<boolean> {
    const url = `${environment.apiBase}/institutions/${institutionIdToValidate}/rules/validate-regulation`;
    let queryParams = new HttpParams().append('institutionIdToValidate', JSON.stringify(institutionIdToValidate));

    if (practice) 
        queryParams = queryParams.append('practiceId', JSON.stringify(practice));

    if (clinicalSpecialtys && clinicalSpecialtys.length)
        clinicalSpecialtys.forEach(clinicalSpecialtyId => queryParams = queryParams.append('clinicalSpecialtyIds', JSON.stringify(clinicalSpecialtyId)));

    return this.http.get<boolean>(url, { params: queryParams });
  }
}
