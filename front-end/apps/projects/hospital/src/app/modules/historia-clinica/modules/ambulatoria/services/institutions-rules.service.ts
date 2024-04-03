import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '@environments/environment';
import { Observable } from 'rxjs';
import {ContextService} from "@core/services/context.service";

@Injectable({
  providedIn: 'root'
})
export class InstitutionsRulesService {

  constructor(private http: HttpClient, private contextService: ContextService) { }

  validateRegulation(clinicalSpecialties: number[], practice: number): Observable<boolean> {
    const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/rules/validate-regulation`;
    let queryParams = new HttpParams();

    if (practice)
        queryParams = queryParams.append('practiceId', JSON.stringify(practice));

    if (clinicalSpecialties && clinicalSpecialties.length)
		clinicalSpecialties.forEach(clinicalSpecialtyId => queryParams = queryParams.append('clinicalSpecialtyIds', JSON.stringify(clinicalSpecialtyId)));

    return this.http.get<boolean>(url, { params: queryParams });
  }
}
