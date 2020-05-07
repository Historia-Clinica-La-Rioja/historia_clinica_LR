import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { EpicrisisGeneralStateDto, NewEpicrisisDto, ResponseAnamnesisDto, ResponseEpicrisisDto } from '@api-rest/api-model';
import { environment } from '@environments/environment';

const HARD_CODE_INSTITUTION = 10;

@Injectable({
	providedIn: 'root'
})
export class EpicrisisService {

	constructor(
		private http: HttpClient
	) { }

	getInternmentGeneralState(internmentEpisodeId: number): Observable<EpicrisisGeneralStateDto> {
		const url = `${environment.apiBase}/institutions/${HARD_CODE_INSTITUTION}/internments/${internmentEpisodeId}/epicrisis/general`;
		return this.http.get<EpicrisisGeneralStateDto>(url);
	}

	createDocument(epicrisis: NewEpicrisisDto, internmentEpisodeId: number): Observable<ResponseEpicrisisDto> {
		const url = `${environment.apiBase}/institutions/${HARD_CODE_INSTITUTION}/internments/${internmentEpisodeId}/epicrisis`;
		return this.http.post<ResponseEpicrisisDto>(url, epicrisis);
	}
}
