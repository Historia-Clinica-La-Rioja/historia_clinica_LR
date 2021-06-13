import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { AdministrativeDischargeDto } from '@api-rest/api-model';
import { ContextService } from '@core/services/context.service';
import { environment } from '@environments/environment';
import { Observable } from 'rxjs';

@Injectable({
	providedIn: 'root'
})
export class EmergencyCareEpisodeAdministrativeDischargeService {

	private URL_PREFIX = `${environment.apiBase}` + '/institution/' + `${this.contextService.institutionId}` + `/emergency-care/episodes/`;

	constructor(
		private http: HttpClient,
		private contextService: ContextService,
	) { }

	newAdministrativeDischarge(episodeId: number, administrativeDischargeDto: AdministrativeDischargeDto): Observable<boolean> {
		const url = this.URL_PREFIX + episodeId + `/administrative-discharge`;
		return this.http.post<boolean>(url, administrativeDischargeDto);
	}

	newAdministrativeDischargeByAbsence(episodeId: number): Observable<boolean> {
		const url = this.URL_PREFIX + episodeId + `/administrative-discharge/absence`;
		return this.http.post<boolean>(url, {});
	}
}
