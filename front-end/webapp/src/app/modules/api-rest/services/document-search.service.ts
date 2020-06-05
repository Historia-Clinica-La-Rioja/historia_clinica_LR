import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '@environments/environment';
import { ContextService } from '@core/services/context.service';
import { Observable } from 'rxjs';
import { DocumentHistoricDto, DocumentSearchFilterDto } from '@api-rest/api-model';

@Injectable({
	providedIn: 'root'
})
export class DocumentSearchService {

	constructor(
		private contextService: ContextService,
		private http: HttpClient
	) { }

	public getHistoric(internmentEpisodeId: number, searchFilter?: DocumentSearchFilterDto): Observable<DocumentHistoricDto> {
		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/internments/${internmentEpisodeId}/documentSearch`;
		return this.http.get<DocumentHistoricDto>(url, { params: {searchFilter: JSON.stringify(searchFilter) } } );
	}
}
