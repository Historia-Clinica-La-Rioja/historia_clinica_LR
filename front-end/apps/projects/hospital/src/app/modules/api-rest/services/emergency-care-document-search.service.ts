import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { EmergencyCareHistoricDocumentDto } from '@api-rest/api-model';
import { ContextService } from '@core/services/context.service';
import { environment } from '@environments/environment';
import { Observable, } from 'rxjs';

@Injectable({
	providedIn: 'root'
})
export class EmergencyCareDocumentSearchService {

	readonly url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/emergency-care/`

	constructor(
		private http: HttpClient,
		private readonly contextService: ContextService
	) { }

	get(emergencyCareEpisodeId: number): Observable<EmergencyCareHistoricDocumentDto> {
		const url = `${this.url}${emergencyCareEpisodeId}/documentSearch`
		return this.http.get<EmergencyCareHistoricDocumentDto>(url);
	}

}
