import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { DocumentTypeDto, EpisodeDocumentResponseDto, EpisodeDocumentTypeDto} from '@api-rest/api-model';
import { ContextService } from '@core/services/context.service';
import { environment } from '@environments/environment';
import { ViewPdfService } from '@presentation/dialogs/view-pdf/view-pdf.service';
import { Observable } from 'rxjs';

@Injectable({
	providedIn: 'root'
})
export class InternmentEpisodeDocumentService {

	url: string = `${environment.apiBase}/institutions/${this.contextService.institutionId}`;

	constructor(
		private http: HttpClient,
		private contextService: ContextService,
		private viewPdfService: ViewPdfService,
	) { }

	saveInternmentEpisodeDocument(file, internmentEpisodeId: number, episodeDocumentTypeId: number, consentId: number): Observable<number> {
		const url = `${this.url}/internments/${internmentEpisodeId}/episodedocuments/${episodeDocumentTypeId}/consent/${consentId}`;
		return this.http.post<number>(url, file);
	}

	getInternmentEpisodeDocuments(internmentEpisodeId: number): Observable<EpisodeDocumentResponseDto[]> {
		const url = `${this.url}/internments/${internmentEpisodeId}/episodedocuments`;
		return this.http.get<EpisodeDocumentResponseDto[]>(url);
	}

	getConsentDocumentTypes(): Observable<EpisodeDocumentTypeDto[]> {
		const url = `${this.url}/episodedocumenttypes`;
		return this.http.get<EpisodeDocumentTypeDto[]>(url);
	}

	getDocumentTypes(): Observable<DocumentTypeDto[]> {
		const url = `${this.url}/internments/documentstypes`;
		return this.http.get<DocumentTypeDto[]>(url);
	}

	generateConsentDocument(internmentEpisodeId: number, consentId: number) {
		const url = `${this.url}/internments/${internmentEpisodeId}/episode-document-type/${consentId}`;
		const fileName = `Consentimiento_${consentId}.pdf`;

		this.viewPdfService.showDialog(
			url,
			fileName,
		);
	}

	deleteDocument(episodeDocumentId: number): Observable<boolean> {
		const url = `${this.url}/internments/episodedocuments/${episodeDocumentId}`;
		return this.http.delete<boolean>(url);
	}

	download(episodeDocumentId: number, fileName: string) {
		const url = `${this.url}/internments/episodedocuments/download/${episodeDocumentId}`;
		this.viewPdfService.showDialog(
			url,
			fileName,
		);
	}
}
