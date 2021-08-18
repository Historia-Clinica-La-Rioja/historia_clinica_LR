import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '@environments/environment';
import { OdontogramQuadrantDto, ToothSurfacesDto } from '@api-rest/api-model';
import { of } from 'rxjs/internal/observable/of';

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

	getOdontogramDrawings(): Observable<ToothDrawingsDto[]> {
		return of([]);
	}

}


export interface ToothDrawingsDto {
	toothSctid: string;
	drawings: {
		internal?: string;
		external?: string;
		left?: string;
		right?: string;
		central?: string;
		whole?: string
	}
}

