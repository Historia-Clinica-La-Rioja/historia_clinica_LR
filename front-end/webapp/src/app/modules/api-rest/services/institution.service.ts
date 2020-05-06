import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { InstitutionDto } from '@api-rest/api-model';
import { environment } from "@environments/environment";

@Injectable({
	providedIn: 'root'
})
export class InstitutionService {

	constructor(
		private http: HttpClient,
	) { }

	public getInstitutions(ids: number[]): Observable<InstitutionDto[]> {
		return this.http.get<InstitutionDto[]>(
			`${environment.apiBase}/institution`,
			{
				params: { ids: `${ids.join(',')}` }
			}
		);
	}
}
