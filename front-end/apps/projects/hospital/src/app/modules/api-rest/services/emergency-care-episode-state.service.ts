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

	changeState(episodeId: number, emergencyCareEpisodeStateId: number, doctorsOfficeId?: number, shockroomId?: number): Observable<boolean> {
		const params: HttpParams = this.buildChangeStateParams(emergencyCareEpisodeStateId, doctorsOfficeId, shockroomId);

		const url = `${environment.apiBase + BASIC_URL_PREFIX}/${this.contextService.institutionId +
		BASIC_URL_SUFIX}/${episodeId}/state`;

		return this.http.post<boolean>(url, {}, {params});
	}

	getState(episodeId: number): Observable<MasterDataDto> {
		const url = `${environment.apiBase + BASIC_URL_PREFIX}/${this.contextService.institutionId +
			BASIC_URL_SUFIX}/${episodeId}/state`;
		return this.http.get<MasterDataDto>(url);
	}

	private buildChangeStateParams(emergencyCareEpisodeStateId: number, doctorsOfficeId?: number, shockroomId?: number): HttpParams {
		let params: HttpParams = new HttpParams();
		params = params.append('emergencyCareStateId', JSON.stringify(emergencyCareEpisodeStateId));
		if (doctorsOfficeId) 
			params = params.append('doctorsOfficeId', JSON.stringify(doctorsOfficeId));
	
		if (shockroomId)
			params = params.append('shockroomId', JSON.stringify(shockroomId));

		return params;
	}

}
