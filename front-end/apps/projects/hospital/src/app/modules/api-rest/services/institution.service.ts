import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import {InstitutionBasicInfoDto, InstitutionDto} from '@api-rest/api-model';
import { environment } from '@environments/environment';

@Injectable({
	providedIn: 'root'
})
export class InstitutionService {

	constructor(
		private http: HttpClient,
	) { }

	public getInstitutions(ids: number[]): Observable<InstitutionDto[]> {
		if (!ids || ids.length === 0) {
			return of([]);
		}
		return this.http.get<InstitutionDto[]>(
			`${environment.apiBase}/institution`,
			{
				params: { ids: `${ids.join(',')}` }
			}
		);
	}

	public getAllInstitutions(): Observable<InstitutionBasicInfoDto[]> {
		return this.http.get<InstitutionBasicInfoDto[]>(`${environment.apiBase}/institution/all`);
	}
}
