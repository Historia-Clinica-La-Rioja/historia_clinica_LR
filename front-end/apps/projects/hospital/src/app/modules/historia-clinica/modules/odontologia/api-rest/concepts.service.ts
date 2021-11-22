import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { OdontologyConceptDto } from '@api-rest/api-model';
import { environment } from '@environments/environment';
import { Observable } from 'rxjs';

@Injectable({
	providedIn: 'root'
})
export class ConceptsService {


	private readonly BASE_URL = '/odontology/concepts'
	constructor(
		private readonly http: HttpClient,
	) { }


	getDiagnostics(): Observable<OdontologyConceptDto[]> {
		const url = `${environment.apiBase}${this.BASE_URL}/diagnostics`;
		return this.http.get<OdontologyConceptDto[]>(url);
	}

	getProcedures(): Observable<OdontologyConceptDto[]> {
		const url = `${environment.apiBase}${this.BASE_URL}/procedures`;
		return this.http.get<OdontologyConceptDto[]>(url);
	}
}
