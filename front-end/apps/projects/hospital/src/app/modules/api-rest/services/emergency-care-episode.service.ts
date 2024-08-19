import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import {
	ECAdministrativeDto,
	ECPediatricDto,
	ECAdultGynecologicalDto,
	ResponseEmergencyCareDto,
	EmergencyCareListDto,
	DateTimeDto,
	RiskFactorDto,
	PageDto,
	EmergencyCareEpisodeFilterDto
} from '@api-rest/api-model';
import { environment } from '@environments/environment';
import { HttpClient, HttpParams } from '@angular/common/http';
import { ContextService } from '@core/services/context.service';


const BASIC_URL_PREFIX = '/institution';
const BASIC_URL_SUFIX = '/emergency-care';

@Injectable({
	providedIn: 'root'
})
export class EmergencyCareEpisodeService {

	constructor(
		private http: HttpClient,
		private contextService: ContextService,
	) {
	}

	getAll(pageSize: number, pageNumber: number, filter: EmergencyCareEpisodeFilterDto): Observable<PageDto<EmergencyCareListDto>> {
		const url = `${environment.apiBase + BASIC_URL_PREFIX}/${this.contextService.institutionId + BASIC_URL_SUFIX}/episodes`;
		let queryParam = new HttpParams().append('pageNumber', pageNumber).append('pageSize', pageSize).append('filter', JSON.stringify(filter));
		return this.http.get<PageDto<EmergencyCareListDto>>(url, { params: queryParam });
	}

	createAdministrative(newEpisode: ECAdministrativeDto): Observable<number> {
		const url = `${environment.apiBase + BASIC_URL_PREFIX}/${this.contextService.institutionId +
			BASIC_URL_SUFIX}/episodes`;
		return this.http.post<number>(url, newEpisode);
	}

	createAdult(newEpisode: ECAdultGynecologicalDto): Observable<number> {
		const url = `${environment.apiBase + BASIC_URL_PREFIX}/${this.contextService.institutionId +
			BASIC_URL_SUFIX}/episodes/adult-gynecological`;
		return this.http.post<number>(url, newEpisode);
	}

	createPediatric(newEpisode: ECPediatricDto): Observable<number> {
		const url = `${environment.apiBase + BASIC_URL_PREFIX}/${this.contextService.institutionId +
			BASIC_URL_SUFIX}/episodes/pediatric`;
		return this.http.post<number>(url, newEpisode);
	}

	setPatient(episodeId: number, patientId: number): Observable<boolean> {
		const url = `${environment.apiBase + BASIC_URL_PREFIX}/${this.contextService.institutionId +
			BASIC_URL_SUFIX}/episodes/${episodeId}/administrative`;
		return this.http.put<boolean>(url, patientId);
	}

	getAdministrative(episodeId: number): Observable<ResponseEmergencyCareDto> {
		const url = `${environment.apiBase + BASIC_URL_PREFIX}/${this.contextService.institutionId +
			BASIC_URL_SUFIX}/episodes/` + episodeId + '/administrative';
		return this.http.get<ResponseEmergencyCareDto>(url);
	}

	getCreationDate(episodeId: number): Observable<DateTimeDto> {
		const url = `${environment.apiBase + BASIC_URL_PREFIX}/${this.contextService.institutionId +
			BASIC_URL_SUFIX}/episodes/` + episodeId + '/creation-date';
		return this.http.get<DateTimeDto>(url);
	}

	updateAdministrative(episodeId: number, data): Observable<number> {
		const url = `${environment.apiBase + BASIC_URL_PREFIX}/${this.contextService.institutionId +
			BASIC_URL_SUFIX}/episodes/` + episodeId;
		return this.http.put<number>(url, data);
	}

	hasEvolutionNote(episodeId: number): Observable<boolean> {
		const url = `${environment.apiBase + BASIC_URL_PREFIX}/${this.contextService.institutionId +
			BASIC_URL_SUFIX}/episodes/` + episodeId + '/has-evolution-note';
		return this.http.get<boolean>(url);
	}

	getRiskFactorsGeneralState(episodeId: number): Observable<RiskFactorDto> {
		const url = `${environment.apiBase + BASIC_URL_PREFIX}/${this.contextService.institutionId + BASIC_URL_SUFIX}/episodes/${episodeId}/general/riskFactors`;
		return this.http.get<RiskFactorDto>(url);
	}

	updatePatientDescription(episodeId: number, patientDescription: string): Observable<boolean> {
		const url = `${environment.apiBase + BASIC_URL_PREFIX}/${this.contextService.institutionId +
			BASIC_URL_SUFIX}/episodes/${episodeId}/updatePatientDescription`;
		return this.http.put<boolean>(url, patientDescription);
	}
}
