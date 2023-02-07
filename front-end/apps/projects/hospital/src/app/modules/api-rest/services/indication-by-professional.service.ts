import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ParenteralPlanDto, PharmacoSummaryDto } from '@api-rest/api-model';
import { ContextService } from '@core/services/context.service';
import { environment } from '@environments/environment';
import { Observable } from 'rxjs';

const BASIC_URL_PREFIX = '/institutions';
const BASIC_URL_SUFIX = '/indication';

@Injectable({
	providedIn: 'root'
})
export class IndicationByProfessionalService {

	constructor(
		private readonly http: HttpClient,
		private readonly contextService: ContextService,
	) { }
	getMostFrequentPharmacos(): Observable<PharmacoSummaryDto[]> {
		const url = `${environment.apiBase}` + BASIC_URL_PREFIX + `/${this.contextService.institutionId}` + BASIC_URL_SUFIX + `/pharmacos/most-frequent`;
		return this.http.get<PharmacoSummaryDto[]>(url);
	}

	getMostFrequentParenteralPlan(): Observable<ParenteralPlanDto[]> {
		const url = `${environment.apiBase}` + BASIC_URL_PREFIX + `/${this.contextService.institutionId}` + BASIC_URL_SUFIX + `/parenteralplans/most-frequent`;
		return this.http.get<ParenteralPlanDto[]>(url);
	}
}
