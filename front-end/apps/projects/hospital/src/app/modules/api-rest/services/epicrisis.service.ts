import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { EpicrisisGeneralStateDto, EpicrisisDto, ResponseEpicrisisDto } from '@api-rest/api-model';
import { environment } from '@environments/environment';
import { DownloadService } from '@core/services/download.service';
import { ContextService } from '@core/services/context.service';

@Injectable({
	providedIn: 'root'
})
export class EpicrisisService {

	constructor(
		private http: HttpClient,
		private downloadService: DownloadService,
		private contextService: ContextService,
	) { }

	private readonly  BASIC_URL=`${environment.apiBase}/institutions/${this.contextService.institutionId}/internments/`;

	getInternmentGeneralState(internmentEpisodeId: number): Observable<EpicrisisGeneralStateDto> {
		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/internments/${internmentEpisodeId}/epicrisis/general`;
		return this.http.get<EpicrisisGeneralStateDto>(url);
	}

	createDocument(epicrisis: EpicrisisDto, internmentEpisodeId: number): Observable<ResponseEpicrisisDto> {
		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/internments/${internmentEpisodeId}/epicrisis`;
		return this.http.post<ResponseEpicrisisDto>(url, epicrisis);
	}

	createDraftDocument(epicrisis: EpicrisisDto, internmentEpisodeId: number): Observable<ResponseEpicrisisDto> {
		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/internments/${internmentEpisodeId}/epicrisis/draft`;
		return this.http.post<ResponseEpicrisisDto>(url, epicrisis);
	}

	getPDF(epicrisisId: number, internmentEpisodeId: number): Observable<any> {
		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/internments/${internmentEpisodeId}/epicrisis/${epicrisisId}/report`;
		const fileName = `Epicrisis_internacion_${internmentEpisodeId}`;
		return this.downloadService.downloadPdf(url, fileName);
	}

	deleteEpicrisis(epicrisisId: number, internmentEpisodeId: number, reason: string): Observable<boolean> {
		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/internments/${internmentEpisodeId}/epicrisis/${epicrisisId}`;
		return this.http.delete<boolean>(url, {
			body: reason
		});
	}

	getEpicrisis(epicrisisId: number, internmentEpisodeId: number): Observable<ResponseEpicrisisDto>{
		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/internments/${internmentEpisodeId}/epicrisis/${epicrisisId}`;
		return this.http.get<ResponseEpicrisisDto>(url);
	}

	editEpicrsis(epicrisis: EpicrisisDto, epicrisisId: number, internmentEpisodeId: number): Observable<number> {
		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/internments/${internmentEpisodeId}/epicrisis/${epicrisisId}`;
		return this.http.put<number>(url, epicrisis);
	}

	updateDraft(epicrisis: EpicrisisDto, epicrisisId: number, internmentEpisodeId: number): Observable<number> {
		const url = `${this.BASIC_URL}${internmentEpisodeId}/epicrisis/draft/${epicrisisId}`;
		return this.http.put<number>(url, epicrisis);
	}

	existUpdatesAfterEpicrisis(internmentEpisodeId: number): Observable<boolean> {
		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/internments/${internmentEpisodeId}/epicrisis/existUpdates`;
		return this.http.get<boolean>(url);
	}

	getDraft(epicrisisId: number, internmentEpisodeId: number): Observable<ResponseEpicrisisDto>{
		const url = `${this.BASIC_URL}${internmentEpisodeId}/epicrisis/draft/${epicrisisId}`;
		return this.http.get<ResponseEpicrisisDto>(url);
	}

	closeDraft(internmentEpisodeId: number, epicrisisId: number, epicrisis: EpicrisisDto): Observable<ResponseEpicrisisDto> {
		const url = `${this.BASIC_URL}${internmentEpisodeId}/epicrisis/draft/final/${epicrisisId}`;
		return this.http.put<ResponseEpicrisisDto>(url, epicrisis);
	}
}
