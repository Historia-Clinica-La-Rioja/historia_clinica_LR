import { Injectable } from '@angular/core';
import { HttpClient } from "@angular/common/http";
import { ContextService } from "@core/services/context.service";
import { Observable } from "rxjs";
import { environment } from "@environments/environment";
import { DietDto, OtherIndicationDto, ParenteralPlanDto, PharmacoDto } from "@api-rest/api-model";

@Injectable({
	providedIn: 'root'
})

export class InternmentIndicationService {

	constructor(
		private readonly http: HttpClient,
		private readonly contextService: ContextService,
	) { }

	getInternmentEpisodeDiets(internmentEpisodeId: number): Observable<DietDto[]> {
		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/internments/${internmentEpisodeId}/diets`;
		return this.http.get<DietDto[]>(url);
	}
	getInternmentEpisodeOtherIndications(internmentEpisodeId: number): Observable<OtherIndicationDto[]> {
		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/internments/${internmentEpisodeId}/other-indication`;
		return this.http.get<OtherIndicationDto[]>(url);
	}
	addDiet(indication: DietDto, internmentEpisodeId: number): Observable<DietDto> {
		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/internments/${internmentEpisodeId}/diet`;
		return this.http.post<DietDto>(url, indication);
	}

	addOtherIndication(otherIndication: OtherIndicationDto, internmentEpisodeId: number): Observable<OtherIndicationDto> {
		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/internments/${internmentEpisodeId}/other-indication`;
		return this.http.post<OtherIndicationDto>(url, otherIndication);
	}

	getOtherIndicationTypes(): Observable<OtherIndicationTypeDto[]> {
		const url = `${environment.apiBase}/internments/masterdata/other-indication-type`;
		return this.http.get<OtherIndicationTypeDto[]>(url);
	}

	getInternmentEpisodeParenteralPlan(internmentEpisodeId: number): Observable<ParenteralPlanDto[]> {
		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/internments/${internmentEpisodeId}/parenteral-plans`;
		return this.http.get<ParenteralPlanDto[]>(url);
	}

	getInternmentEpisodePharmaco(internmentEpisodeId: number): Observable<PharmacoDto[]> {
		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/internments/${internmentEpisodeId}/pharmacos`;
		return this.http.get<PharmacoDto[]>(url);
	}

	addParenteralPlan(indication: ParenteralPlanDto, internmentEpisodeId: number): Observable<number> {
		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/internments/${internmentEpisodeId}/parenteral-plan`;
		return this.http.post<number>(url, indication);
	}

	addPharamaco(pharmaco: PharmacoDto, internmentEpisodeId: number): Observable<number> {
		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/internments/${internmentEpisodeId}/pharmaco`;
		return this.http.post<number>(url, pharmaco);
	}
}

export interface OtherIndicationTypeDto {
	id: number,
	description: string
}
