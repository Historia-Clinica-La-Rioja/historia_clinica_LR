import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '@environments/environment';
import { DownloadService } from '@core/services/download.service';

const HARD_CODE_INSTITUTION = 10;

@Injectable({
	providedIn: 'root'
})
export class EpicrisisReportService {

	constructor(
		private downloadService: DownloadService,
	) { }

	getPDF(epicrisisId: number, internmentEpisodeId: number): Observable<any> {
		const url = `${environment.apiBase}/institutions/${HARD_CODE_INSTITUTION}/internments/${internmentEpisodeId}/epicrisis-report/${epicrisisId}`;
		const fileName = `Epicrisis_internacion_${internmentEpisodeId}`;
		return this.downloadService.downloadPdf(url, fileName);
	}

}
