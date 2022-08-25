import { Injectable } from '@angular/core';
import { HttpClient } from "@angular/common/http";
import { ContextService } from "@core/services/context.service";
import { Observable } from "rxjs";
import { environment } from "@environments/environment";
import { DietDto, OtherIndicationDto, ParenteralPlanDto, PharmacoDto } from "@api-rest/api-model";


const BASIC_URL_PREFIX = '/institutions';
const BASIC_URL_SUFIX = '/internments';	

@Injectable({
	providedIn: 'root'
})

export class InternmentIndicationService {

	constructor(
		private readonly http: HttpClient,
		private readonly contextService: ContextService,
	) {}

	getInternmentEpisodeDiets(internmentEpisodeId: number): Observable<DietDto[]> {
		const url = `${environment.apiBase}` + BASIC_URL_PREFIX + `/${this.contextService.institutionId}` + BASIC_URL_SUFIX + `/${internmentEpisodeId}/diets`;
		return this.http.get<DietDto[]>(url);
	}
	getInternmentEpisodeOtherIndications(internmentEpisodeId: number): Observable<OtherIndicationDto[]> {
		const url = `${environment.apiBase}` + BASIC_URL_PREFIX + `/${this.contextService.institutionId}` + BASIC_URL_SUFIX + `/${internmentEpisodeId}/other-indications`;
		return this.http.get<OtherIndicationDto[]>(url);
	}
	addDiet(indication: DietDto, internmentEpisodeId: number): Observable<DietDto> {
		const url = `${environment.apiBase}` + BASIC_URL_PREFIX + `/${this.contextService.institutionId}` + BASIC_URL_SUFIX + `/${internmentEpisodeId}/diet`;
		return this.http.post<DietDto>(url, indication);
	}

	addOtherIndication(otherIndication: OtherIndicationDto, internmentEpisodeId: number): Observable<OtherIndicationDto> {
		const url = `${environment.apiBase}` + BASIC_URL_PREFIX + `/${this.contextService.institutionId}` + BASIC_URL_SUFIX + `/${internmentEpisodeId}/other-indication`;
		return this.http.post<OtherIndicationDto>(url, otherIndication);
	}

	getOtherIndicationTypes(): Observable<OtherIndicationTypeDto[]> {
		const url = `${environment.apiBase}` + BASIC_URL_SUFIX +`/masterdata/other-indication-type`;
		return this.http.get<OtherIndicationTypeDto[]>(url);
	}

	getInternmentEpisodeParenteralPlans(internmentEpisodeId: number): Observable<ParenteralPlanDto[]> {
		const url = `${environment.apiBase}` + BASIC_URL_PREFIX + `/${this.contextService.institutionId}` + BASIC_URL_SUFIX + `/${internmentEpisodeId}/parenteral-plans`;
		return this.http.get<ParenteralPlanDto[]>(url);
	}

	getInternmentEpisodePharmacos(internmentEpisodeId: number): Observable<PharmacoDto[]> {
		const url = `${environment.apiBase}` + BASIC_URL_PREFIX + `/${this.contextService.institutionId}` + BASIC_URL_SUFIX + `/${internmentEpisodeId}/pharmacos`;
		return this.http.get<PharmacoDto[]>(url);
	}

	getInternmentEpisodeDiet(internmentEpisodeId: number, dietId: number): Observable<DietDto> {
		const url = `${environment.apiBase}` + BASIC_URL_PREFIX + `/${this.contextService.institutionId}` + BASIC_URL_SUFIX + `/${internmentEpisodeId}/diets/${dietId}`;
		return this.http.get<DietDto>(url);
	}
	
	getInternmentEpisodeOtherIndication(internmentEpisodeId: number, otherIndicationId: number): Observable<OtherIndicationDto> {
		const url = `${environment.apiBase}` + BASIC_URL_PREFIX + `/${this.contextService.institutionId}` + BASIC_URL_SUFIX + `/${internmentEpisodeId}/other-indications/${otherIndicationId}`;
		return this.http.get<OtherIndicationDto>(url);
	}

	getInternmentEpisodeParenteralPlan(internmentEpisodeId: number, parenteralPlanid: number): Observable<ParenteralPlanDto> {
		const url = `${environment.apiBase}` + BASIC_URL_PREFIX + `/${this.contextService.institutionId}` + BASIC_URL_SUFIX + `/${internmentEpisodeId}/parenteral-plans/${parenteralPlanid}`;
		return this.http.get<ParenteralPlanDto>(url);
	}

	getInternmentEpisodePharmaco(internmentEpisodeId: number, pharmacoId: number): Observable<PharmacoDto> {
		const url = `${environment.apiBase}` + BASIC_URL_PREFIX + `/${this.contextService.institutionId}` + BASIC_URL_SUFIX + `/${internmentEpisodeId}/pharmacos/${pharmacoId}`;
		return this.http.get<PharmacoDto>(url);
	}

	addParenteralPlan(indication: ParenteralPlanDto, internmentEpisodeId: number): Observable<number> {
		const url = `${environment.apiBase}` + BASIC_URL_PREFIX + `/${this.contextService.institutionId}` + BASIC_URL_SUFIX + `/${internmentEpisodeId}/parenteral-plan`;
		return this.http.post<number>(url, indication);
	}

	addPharamaco(pharmaco: PharmacoDto, internmentEpisodeId: number): Observable<number> {
		const url = `${environment.apiBase}` + BASIC_URL_PREFIX + `/${this.contextService.institutionId}` + BASIC_URL_SUFIX + `/${internmentEpisodeId}/pharmaco`;
		return this.http.post<number>(url, pharmaco);
	}
}

export interface OtherIndicationTypeDto {
	id: number,
	description: string
}
