import { Injectable } from '@angular/core';

@Injectable({
    providedIn: 'root'
})
export class DocumentsSummaryService {

    constructor(
        /* private http: HttpClient,
		private contextService: ContextService, */
    ) { }

    /* 
    getDocumentHeader(internmentEpisodeId: number): Observable<InternmentSummaryDto> {
		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/internments/${internmentEpisodeId}/summary`;
		return this.http.get<InternmentSummaryDto>(url);
	}
    */
}
