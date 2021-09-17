import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '@environments/environment';
import { OdontogramQuadrantDto, ToothDrawingsDto, ToothSurfacesDto } from '@api-rest/api-model';

@Injectable({
	providedIn: 'root'
})
export class OdontogramService {

	private readonly BASE_URL = '/odontology/odontogram';

	constructor(
		private readonly http: HttpClient,
	) { }

	getOdontogram(): Observable<OdontogramQuadrantDto[]> {
		const url = `${environment.apiBase}${this.BASE_URL}`;
		return this.http.get<OdontogramQuadrantDto[]>(url);

	}

	getToothSurfaces(sctid: string): Observable<ToothSurfacesDto> {
		const url = `${environment.apiBase}${this.BASE_URL}/tooth/${sctid}/surfaces`;
		return this.http.get<ToothSurfacesDto>(url);
	}

	getOdontogramDrawings(patientId: number): Observable<ToothDrawingsDto[]> {
		const url = `${environment.apiBase}${this.BASE_URL}/drawings/${patientId}`
		return this.http.get<ToothDrawingsDto[]>(url);
	}
}
