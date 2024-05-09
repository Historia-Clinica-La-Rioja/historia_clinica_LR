import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { HospitalizationDocumentHeaderDto } from '@api-rest/api-model';
import { ContextService } from '@core/services/context.service';
import { environment } from '@environments/environment';
import { Observable } from 'rxjs';

@Injectable({
    providedIn: 'root'
})
export class DocumentsSummaryService {

    constructor(
        private http: HttpClient,
		private contextService: ContextService,
    ) { }

    
    getDocumentHeader(documentId: number, internmentEpisodeId: number): Observable<HospitalizationDocumentHeaderDto> {
		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/internments/${internmentEpisodeId}/document-header/${documentId}`;
		return this.http.get<HospitalizationDocumentHeaderDto>(url);
	}
   
}
