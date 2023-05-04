import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { DietDto, OtherIndicationDto, ParenteralPlanDto, PharmacoDto } from '@api-rest/api-model';
import { ContextService } from '@core/services/context.service';
import { environment } from '@environments/environment';
import { Observable } from 'rxjs';
import { OtherIndicationTypeDto } from './internment-indication.service';

const BASIC_URL_SUFIX = '/internments';

@Injectable({
	providedIn: 'root'
})
export class EmergencyCareIndicationService {

	private readonly url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/emergency-care/episode`
	constructor(
		private readonly http: HttpClient,
		private readonly contextService: ContextService,
	) { }

	getInternmentEpisodeDiets(emergencyCareEpisodeId: number): Observable<DietDto[]> {
		const url = `${this.url}/${emergencyCareEpisodeId}/diets`;
		return this.http.get<DietDto[]>(url);
	}

	getInternmentEpisodeOtherIndications(emergencyCareEpisodeId: number): Observable<OtherIndicationDto[]> {
		const url = `${this.url}/${emergencyCareEpisodeId}/other-indications`;
		return this.http.get<OtherIndicationDto[]>(url);
	}

	addDiet(indication: DietDto, emergencyCareEpisodeId: number): Observable<DietDto> {
		const url = `${this.url}/${emergencyCareEpisodeId}/diet`;
		return this.http.post<DietDto>(url, indication);
	}

	addOtherIndication(otherIndication: OtherIndicationDto, emergencyCareEpisodeId: number): Observable<OtherIndicationDto> {
		const url = `${this.url}/${emergencyCareEpisodeId}/other-indication`;
		return this.http.post<OtherIndicationDto>(url, otherIndication);
	}

	getOtherIndicationTypes(): Observable<OtherIndicationTypeDto[]> {
		const url = `${environment.apiBase}` + BASIC_URL_SUFIX + `/masterdata/other-indication-type`;
		return this.http.get<OtherIndicationTypeDto[]>(url);
	}

	getInternmentEpisodeParenteralPlans(emergencyCareEpisodeId: number): Observable<ParenteralPlanDto[]> {
		const url = `${this.url}/${emergencyCareEpisodeId}/parenteral-plans`;
		return this.http.get<ParenteralPlanDto[]>(url);
	}

	getInternmentEpisodePharmacos(emergencyCareEpisodeId: number): Observable<PharmacoDto[]> {
		const url = `${this.url}/${emergencyCareEpisodeId}/pharmacos`;
		return this.http.get<PharmacoDto[]>(url);
	}

	addParenteralPlan(indication: ParenteralPlanDto, emergencyCareEpisodeId: number): Observable<number> {
		const url = `${this.url}/${emergencyCareEpisodeId}/parenteral-plan`;
		return this.http.post<number>(url, indication);
	}

	addPharamaco(pharmaco: PharmacoDto, emergencyCareEpisodeId: number): Observable<number> {
		const url = `${this.url}/${emergencyCareEpisodeId}/pharmaco`;
		return this.http.post<number>(url, pharmaco);
	}
}
