import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AnamnesisDto, ResponseAnamnesisDto } from '@api-rest/api-model';
import { environment } from '@environments/environment';

const HARD_CODE_INSTITUTION = 1;
const HARD_CODE_INTERNMENT_EPISODE_ID = 1;

@Injectable({
	providedIn: 'root'
})
export class AnamnesisService {

	constructor(
		private http: HttpClient
	) { }

	createAnamnesis(anamnesis: AnamnesisDto): Observable<ResponseAnamnesisDto> {
		let url = `${environment.apiBase}/institutions/${HARD_CODE_INSTITUTION}/internments/${HARD_CODE_INTERNMENT_EPISODE_ID}/anamnesis`;
		return this.http.post<ResponseAnamnesisDto>(url, anamnesis);
	}
}
