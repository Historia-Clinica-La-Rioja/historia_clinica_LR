import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { EmergencyCareEvolutionNoteDto } from '@api-rest/api-model';
import { ContextService } from '@core/services/context.service';
import { environment } from '@environments/environment';
import { Observable } from 'rxjs';

@Injectable({
	providedIn: 'root'
})
export class EmergencyCareEvolutionNoteService {

	readonly PREFIX_URL = `${environment.apiBase}/institution/`;
	readonly BASIC_URL = `emergency-care/episodes/`;

	constructor(
		private readonly http: HttpClient,
		private readonly contextService: ContextService
	) { }

	saveEmergencyCareEvolutionNote(episodeId: number, dto: EmergencyCareEvolutionNoteDto): Observable<boolean> {
		const url = `${this.PREFIX_URL}${this.contextService.institutionId}/${this.BASIC_URL}${episodeId}/evolution-note`;
		return this.http.post<boolean>(url, dto);
	}

	updateEmergencyCareEvolutionNote(episodeId: number, oldDocumentId: number, dto: EmergencyCareEvolutionNoteDto): Observable<boolean> {
		const url = `${this.PREFIX_URL}${this.contextService.institutionId}/${this.BASIC_URL}${episodeId}/evolution-note/edit-by-document/${oldDocumentId}`;
		return this.http.post<boolean>(url, dto);
	}

	getByDocumentId(episodeId: number, documentId: number): Observable<EmergencyCareEvolutionNoteDto> {
		const url = `${environment.apiBase}/institution/${this.contextService.institutionId}/emergency-care/episodes/${episodeId}/evolution-note/by-document/${documentId}`;
		return this.http.get<EmergencyCareEvolutionNoteDto>(url);
	}
}
