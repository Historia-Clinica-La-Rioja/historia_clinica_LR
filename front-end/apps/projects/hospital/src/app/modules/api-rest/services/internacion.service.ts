import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { environment } from '@environments/environment';
import { InternmentSummaryDto, InternmentEpisodeDto } from '@api-rest/api-model';
import { ContextService } from '@core/services/context.service';

@Injectable({
	providedIn: 'root'
})
export class InternacionService {

	constructor(
		private http: HttpClient,
		private contextService: ContextService,
	) {	}

	getAllPacientesInternados(): Observable<InternmentEpisodeDto[]> {
		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/internments/patients`;
		return this.http.get<InternmentEpisodeDto[]>(url);
	}

	getInternmentEpisodeSummary(internmentId: number): Observable<InternmentSummaryDto> {
		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/internments/${internmentId}/summary`;
		return this.http.get<InternmentSummaryDto>(url);
	}

}
