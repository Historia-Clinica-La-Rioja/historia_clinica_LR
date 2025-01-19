import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ParameterCompleteDataDto } from '@api-rest/api-model';
import { ContextService } from '@core/services/context.service';
import { environment } from '@environments/environment';
import { Observable } from 'rxjs';

@Injectable({
	providedIn: 'root'
})
export class ParametersService {

	constructor(
		private readonly http: HttpClient,
		private readonly contextService: ContextService
	) { }

	getParametersByFormId(parameterizedFormId: number): Observable<ParameterCompleteDataDto[]> {
		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/parameters/by-form/${parameterizedFormId}`;
		return this.http.get<ParameterCompleteDataDto[]>(url);
	}
}
