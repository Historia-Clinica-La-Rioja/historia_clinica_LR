import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { DietDto, OtherIndicationDto, ParenteralPlanDto, PharmacoDto } from '@api-rest/api-model';
import { ContextService } from '@core/services/context.service';
import { environment } from '@environments/environment';
import { Observable } from 'rxjs';

const BASIC_URL_PREFIX = '/institutions';
const BASIC_URL_SUFIX = '/indication'


@Injectable({
  	providedIn: 'root'
})
export class IndicationService {

	constructor(
		private readonly http: HttpClient,
		private readonly contextService: ContextService,
	) { }

	getDiet(dietId: number): Observable<DietDto> {
		const url = `${environment.apiBase}` + BASIC_URL_PREFIX + `/${this.contextService.institutionId}` + BASIC_URL_SUFIX + `/diets/${dietId}`;
		return this.http.get<DietDto>(url);
	}

	getOtherIndication(otherIndicationId: number): Observable<OtherIndicationDto> {
		const url = `${environment.apiBase}` + BASIC_URL_PREFIX + `/${this.contextService.institutionId}` + BASIC_URL_SUFIX + `/other-indications/${otherIndicationId}`;
		return this.http.get<OtherIndicationDto>(url);
	}

	getParenteralPlan(parenteralPlanid: number): Observable<ParenteralPlanDto> {
		const url = `${environment.apiBase}` + BASIC_URL_PREFIX + `/${this.contextService.institutionId}` + BASIC_URL_SUFIX + `/parenteral-plans/${parenteralPlanid}`;
		return this.http.get<ParenteralPlanDto>(url);
	}

	getPharmaco(pharmacoId: number): Observable<PharmacoDto> {
		const url = `${environment.apiBase}` + BASIC_URL_PREFIX + `/${this.contextService.institutionId}` + BASIC_URL_SUFIX + `/pharmacos/${pharmacoId}`;
		return this.http.get<PharmacoDto>(url);
	}

}
