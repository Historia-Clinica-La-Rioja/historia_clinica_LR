import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '@environments/environment';
import { DownloadService } from '@core/services/download.service';
import { ContextService } from '@core/services/context.service';

@Injectable({
  providedIn: 'root'
})
export class EvolutionNoteReportService {

	constructor(
		private downloadService: DownloadService,
		private contextService: ContextService,
	) { }

	getPDF(evolutionNoteId: number, internmentEpisodeId: number): Observable<any> {
		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/internments/${internmentEpisodeId}/evolutionNote-report/${evolutionNoteId}`;
		const fileName = `${`Nota_evolucion_${evolutionNoteId}_internacion_${internmentEpisodeId}`}.pdf`;
		return this.downloadService.downloadPdf(url, fileName);
	}

}
