import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { CareLineDto } from '@api-rest/api-model';
import { ContextService } from '@core/services/context.service';
import { environment } from '@environments/environment';
import { Observable } from 'rxjs';

@Injectable({
	providedIn: 'root'
})
export class CareLineService {

	constructor(
		private readonly http: HttpClient,
		private readonly contextService: ContextService,
	) { }

	getCareLines(): Observable<CareLineDto[]> {
		const url = `${environment.apiBase}/institution/${this.contextService.institutionId}/carelines`;
		return this.http.get<CareLineDto[]>(url);
	}
}
