import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import {
	VaccineInformationDto
} from '@api-rest/api-model';
import { Observable } from 'rxjs';
import { environment } from '@environments/environment';

@Injectable({
	providedIn: 'root'
})
export class VaccineService {

	constructor(
		private readonly http: HttpClient
	) {
	}

	vaccineInformation(sctdId: string): Observable<VaccineInformationDto> {
		const url = `${environment.apiBase}/vaccines/${sctdId}`;
		return this.http.get<VaccineInformationDto>(url);
	}
}
