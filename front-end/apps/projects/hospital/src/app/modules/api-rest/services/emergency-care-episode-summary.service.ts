import { Injectable } from '@angular/core';
import { HttpClient } from "@angular/common/http";
import { ContextService } from "@core/services/context.service";
import { Observable } from "rxjs/internal/Observable";
import { EmergencyCareEpisodeInProgressDto, EmergencyCareListDto } from "@api-rest/api-model";
import { environment } from "@environments/environment";

const BASIC_URL = environment.apiBase + '/institution/'
const PREFIX_URL = '/emergency-care/episode'
@Injectable({
	providedIn: 'root'
})
export class EmergencyCareEpisodeSummaryService {

	constructor(private http: HttpClient,
				private contextService: ContextService) {
	}


	getEmergencyCareEpisodeInProgress(patientId: number): Observable<EmergencyCareEpisodeInProgressDto> {
		const url = `${BASIC_URL + this.contextService.institutionId + PREFIX_URL}/in-progress/patient/${patientId}`;
		return this.http.get<EmergencyCareEpisodeInProgressDto>(url);
	}

	getEmergencyCareEpisodeSummary(episodeId: number): Observable<EmergencyCareListDto> {
		const url = `${BASIC_URL + this.contextService.institutionId + PREFIX_URL}/${episodeId}/summary`;
		return this.http.get<EmergencyCareListDto>(url);
	}

}
