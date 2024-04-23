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

	constructor(
		private readonly http: HttpClient,
		private readonly contextService: ContextService
	) { }

	saveEmergencyCareEvolutionNote(episodeId: number, dto: EmergencyCareEvolutionNoteDto): Observable<boolean> {
		const url = `${environment.apiBase}/institution/${this.contextService.institutionId}/emergency-care/episodes/${episodeId}/evolution-note`;
		return this.http.post<boolean>(url, dto);
	}
}
