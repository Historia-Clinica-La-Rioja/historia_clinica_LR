import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '@environments/environment';
import { DateDto, OdontogramQuadrantDto, SnomedDto, ToothSurfacesDto } from '@api-rest/api-model';
import { of } from 'rxjs';

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

	getOdontogramDrawings(patientId: string): Observable<ToothDrawingsDto[]> {
		return patientId == '3' ? of([
			{
				toothSctid: "245566003",
				drawings:
				{
					left: "4721000221105",
					right: "4721000221105"
				}
			},
			{
				toothSctid: "245567007",
				drawings:
				{
					whole: "399271000221103",
				}
			}])
			: of([]);

		/*
		* Uncomment to connect to BE and remove previous return code

		const url = `${environment.apiBase}${this.BASE_URL}/drawings/${patientId}`
		return this.http.get<ToothDrawingsDto[]>(url);
		*/
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
