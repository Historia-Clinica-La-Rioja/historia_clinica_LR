import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '@environments/environment';
import { ContextService } from '@core/services/context.service';
import { Observable } from 'rxjs';
import { ParameterizedFormDto } from '@api-rest/api-model';

@Injectable({
	providedIn: 'root'
})
export class ParameterizedFormService {

	constructor(
		private readonly http: HttpClient,
		private readonly contextService: ContextService
	) { }

	getList(): Observable<ParameterizedFormDto[]> {
		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/parameterized-form`;
		return this.http.get<ParameterizedFormDto[]>(url);
	}
}
