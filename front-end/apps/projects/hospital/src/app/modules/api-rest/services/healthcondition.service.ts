import {Injectable} from '@angular/core';
import {Observable, map} from 'rxjs';
import {HttpClient} from '@angular/common/http';
import {environment} from '@environments/environment';
import {HealthConditionNewConsultationDto} from '@api-rest/api-model';
import {ContextService} from '@core/services/context.service';
import { dateISOParseDate } from '@core/utils/moment.utils';

@Injectable({
	providedIn: 'root'
})
export class HealthConditionService {

	constructor(
		private http: HttpClient,
		private contextService: ContextService,
	) {	}

	getHealthCondition(healthConditionId: number): Observable<HealthConditionNewConsultationDto> {
		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/healthcondition/${healthConditionId}`;
		return this.http.get<HealthConditionNewConsultationDto>(url).pipe(map(hc => {return {...hc ,startDate: dateISOParseDate(hc.startDate.toString())}}));
	}
}
