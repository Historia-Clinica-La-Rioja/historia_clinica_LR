import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { environment } from '@environments/environment';

const HARD_CODE_INS_ID = 10;
const HARD_CODE_INT_EP_ID = 10;

@Injectable({
	providedIn: 'root'
})
export class InternacionService {

	constructor(
		private http: HttpClient
	) {	}

	getAllPacientesInternados<InternmentEpisodeDto>(): Observable<InternmentEpisodeDto[]> {
		let url = `${environment.apiBase}/institutions/${HARD_CODE_INS_ID}/internments/patients`;
		return this.http.get<InternmentEpisodeDto[]>(url);
	}

	getInternmentEpisodeSummary<InternmentSummaryDto>(internmentId): Observable<InternmentSummaryDto> {
		let url = `${environment.apiBase}/institutions/${HARD_CODE_INS_ID}/internments/${HARD_CODE_INT_EP_ID}/summary`;
		return this.http.get<InternmentSummaryDto>(url);
	}

}
