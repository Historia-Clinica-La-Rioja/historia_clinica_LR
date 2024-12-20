import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { DiagnosesGeneralStateDto } from '@api-rest/api-model';
import { ContextService } from '@core/services/context.service';
import { environment } from '@environments/environment';
import { Observable } from 'rxjs';

@Injectable({
	providedIn: 'root'
})
export class EmergencyCareStateService {

	private URL_PREFIX = `${environment.apiBase}` + '/institution/' + `${this.contextService.institutionId}` + `/emergency-care/episode/`;

	constructor(
		private http: HttpClient,
		private contextService: ContextService,
	) { }


	getEmergencyCareEpisodeDiagnoses(episodeId: number): Observable<DiagnosesGeneralStateDto[]> {
		const url = this.URL_PREFIX + episodeId + `/diagnoses`;
		return this.http.get<DiagnosesGeneralStateDto[]>(url);
	}

	getEmergencyCareEpisodeDiagnosesWithoutNursingAttentionDiagnostic(episodeId: number): Observable<DiagnosesGeneralStateDto[]> {
		const url = `${this.URL_PREFIX}${episodeId}/diagnoses-without-nursing-attention-diagnostic`;
		return this.http.get<DiagnosesGeneralStateDto[]>(url);
	}

}
