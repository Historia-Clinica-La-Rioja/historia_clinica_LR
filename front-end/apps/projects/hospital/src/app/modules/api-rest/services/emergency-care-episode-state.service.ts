import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient, HttpParams } from '@angular/common/http';
import { ContextService } from '@core/services/context.service';
import { environment } from '@environments/environment';
import { MasterDataDto } from '@api-rest/api-model';

const BASIC_URL_PREFIX = '/institution';
const BASIC_URL_SUFIX = '/emergency-care/episodes';

@Injectable({
	providedIn: 'root'
})
export class EmergencyCareEpisodeStateService {

	constructor(
		private http: HttpClient,
		private contextService: ContextService,
	) {
	}

	changeToSpecificState(episodeId: number, emergencyCareEpisodeStateId: number, state: string): Observable<boolean> {
		let params: HttpParams = new HttpParams();
		params = params.append('emergencyCareStateId', JSON.stringify(emergencyCareEpisodeStateId));

		const url = `${environment.apiBase + BASIC_URL_PREFIX}/${this.contextService.institutionId +
		BASIC_URL_SUFIX}/${episodeId}/state/${state}`;

		return this.http.put<boolean>(url, {}, {params});
	}

	changeState(episodeId: number, emergencyCareEpisodeStateId: number, doctorsOfficeId?: number, shockroomId?: number, bedId?: number): Observable<boolean> {
		const params: HttpParams = this.buildChangeStateParams(emergencyCareEpisodeStateId, doctorsOfficeId, shockroomId, bedId);

		const url = `${environment.apiBase + BASIC_URL_PREFIX}/${this.contextService.institutionId +
		BASIC_URL_SUFIX}/${episodeId}/state`;

		return this.http.post<boolean>(url, {}, {params});
	}

	getState(episodeId: number): Observable<MasterDataDto> {
		const url = `${environment.apiBase + BASIC_URL_PREFIX}/${this.contextService.institutionId +
			BASIC_URL_SUFIX}/${episodeId}/state`;
		return this.http.get<MasterDataDto>(url);
	}

	private buildChangeStateParams(emergencyCareEpisodeStateId: number, doctorsOfficeId?: number, shockroomId?: number, bedId?: number): HttpParams {
		let params: HttpParams = new HttpParams();
		params = params.append('emergencyCareStateId', JSON.stringify(emergencyCareEpisodeStateId));
		if (doctorsOfficeId)
			params = params.append('doctorsOfficeId', JSON.stringify(doctorsOfficeId));

		if (shockroomId)
			params = params.append('shockroomId', JSON.stringify(shockroomId));

		if (bedId)
			params = params.append('bedId', JSON.stringify(bedId));

		return params;
	}

}
