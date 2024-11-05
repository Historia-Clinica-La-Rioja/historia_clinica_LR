import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { SnomedTemplateDto } from '@api-rest/api-model';
import { ContextService } from '@core/services/context.service';
import { environment } from '@environments/environment';
import { Observable } from 'rxjs';

@Injectable({
	providedIn: 'root'
})
export class MostFrequentConceptsService {

	constructor(
		private readonly http: HttpClient,
		private readonly contextService: ContextService,
		) { }

	getMostFrequentConceptsService(): Observable<any> {
		return this.http.get<any[]>(`${environment.apiBase}/institutions/${this.contextService.institutionId}/service-request/most-frequent/concepts`);
	}

	getMostFrequentTemplates = (): Observable<SnomedTemplateDto[]> => {
		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/service-request/most-frequent/get-templates`;
		return this.http.get<SnomedTemplateDto[]>(url);
	}
}
