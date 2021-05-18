import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '@environments/environment';
import { OdontogramQuadrantDto } from '@api-rest/api-model';

@Injectable({
	providedIn: 'root'
})
export class OdontogramService {

	constructor(
		private readonly http: HttpClient,
	) { }

	getOdontogram(): Observable<OdontogramQuadrantDto[]> {
		const url = `${environment.apiBase}/odontology/odontogram`;
		return this.http.get<OdontogramQuadrantDto[]>(url);

	}
}
