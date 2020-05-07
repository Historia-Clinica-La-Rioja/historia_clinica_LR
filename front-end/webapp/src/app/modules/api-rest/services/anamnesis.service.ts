import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AnamnesisDto, ResponseAnamnesisDto } from '@api-rest/api-model';
import { environment } from '@environments/environment';

const HARD_CODE_INSTITUTION = 1;

@Injectable({
	providedIn: 'root'
})
export class AnamnesisService {

	constructor(
		private http: HttpClient
	) { }

	createAnamnesis(anamnesis: AnamnesisDto, internmentEpisodeId: number): Observable<ResponseAnamnesisDto> {
		const url = `${environment.apiBase}/institutions/${HARD_CODE_INSTITUTION}/internments/${internmentEpisodeId}/anamnesis`;
		return this.http.post<ResponseAnamnesisDto>(url, anamnesis);
	}

	updateAnamnesis(anamnesisId: number, anamnesis: AnamnesisDto, internmentEpisodeId: number): Observable<ResponseAnamnesisDto> {
		const url = `${environment.apiBase}/institutions/${HARD_CODE_INSTITUTION}/internments/${internmentEpisodeId}/anamnesis/${anamnesisId}`;
		return this.http.put<ResponseAnamnesisDto>(url, anamnesis);
	}

	getAnamnesis(anamnesisId: number, internmentEpisodeId: number): Observable<ResponseAnamnesisDto> {
		const url = `${environment.apiBase}/institutions/${HARD_CODE_INSTITUTION}/internments/${internmentEpisodeId}/anamnesis/${anamnesisId}`;
		return this.http.get<ResponseAnamnesisDto>(url);
	}
}
