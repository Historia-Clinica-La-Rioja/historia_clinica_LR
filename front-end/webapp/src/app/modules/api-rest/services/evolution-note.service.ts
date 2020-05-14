import { Injectable } from '@angular/core';
import { EvolutionNoteDto, ResponseEvolutionNoteDto } from '@api-rest/api-model';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '@environments/environment';
import { DownloadService } from '@core/services/download.service';
import { ContextService } from '@core/services/context.service';

@Injectable({
  providedIn: 'root'
})
export class EvolutionNoteService {

	constructor(
		private http: HttpClient,
		private downloadService: DownloadService,
		private contextService: ContextService,
	) { }

	createDocument(evolutionNote: EvolutionNoteDto, internmentEpisodeId: number): Observable<ResponseEvolutionNoteDto> {
		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/internments/${internmentEpisodeId}/evolutionNote`;
		return this.http.post<ResponseEvolutionNoteDto>(url, evolutionNote);
	}

	getPDF(evolutionNoteId: number, internmentEpisodeId: number): Observable<any> {
		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/internments/${internmentEpisodeId}/evolutionNote/${evolutionNoteId}/report`;
		const fileName = `${`Nota_evolucion_${evolutionNoteId}_internacion_${internmentEpisodeId}`}.pdf`;
		return this.downloadService.downloadPdf(url, fileName);
	}

}
