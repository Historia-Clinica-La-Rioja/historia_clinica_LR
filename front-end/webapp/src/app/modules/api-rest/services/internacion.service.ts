import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { environment } from '@environments/environment';
import { InternmentSummaryDto, InternmentEpisodeDto } from '@api-rest/api-model';

const HARD_CODE_INS_ID = 10;

@Injectable({
	providedIn: 'root'
})
export class InternacionService {

	constructor(
		private http: HttpClient
	) {	}

	getAllPacientesInternados(): Observable<InternmentEpisodeDto[]> {
		let url = `${environment.apiBase}/institutions/${HARD_CODE_INS_ID}/internments/patients`;
		return this.http.get<InternmentEpisodeDto[]>(url);
	}

	getInternmentEpisodeSummary(internmentId: number): Observable<InternmentSummaryDto> {
		let url = `${environment.apiBase}/institutions/${HARD_CODE_INS_ID}/internments/${internmentId}/summary`;
		return this.http.get<InternmentSummaryDto>(url);
	}

}
