import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { DocumentTypeDto, EpisodeDocumentResponseDto } from '@api-rest/api-model';
import { ContextService } from '@core/services/context.service';
import { environment } from '@environments/environment';
import { ViewPdfService } from '@presentation/dialogs/view-pdf/view-pdf.service';
import { Observable } from 'rxjs';

@Injectable({
	providedIn: 'root'
})
export class InternmentEpisodeDocumentService {

	url: string = `${environment.apiBase}/institutions/${this.contextService.institutionId}/internments`;

	constructor(
		private http: HttpClient,
		private contextService: ContextService,
		private viewPdfService: ViewPdfService,
	) { }

	saveInternmentEpisodeDocument(file, internmentEpisodeId: number, episodeDocumentTypeId: number): Observable<number> {
		const url = `${this.url}/${internmentEpisodeId}/episodedocuments/${episodeDocumentTypeId}`;
		return this.http.post<number>(url, file);
	}

	getInternmentEpisodeDocuments(internmentEpisodeId: number): Observable<EpisodeDocumentResponseDto[]> {
		const url = `${this.url}/${internmentEpisodeId}/episodedocuments`;
		return this.http.get<EpisodeDocumentResponseDto[]>(url);
	}

	getDocumentTypes(): Observable<DocumentTypeDto[]> {
		const url = `${this.url}/documentstypes`;
		return this.http.get<DocumentTypeDto[]>(url);
	}

	deleteDocument(episodeDocumentId: number): Observable<boolean> {
		const url = `${this.url}/episodedocuments/${episodeDocumentId}`;
		return this.http.delete<boolean>(url);
	}

	download(episodeDocumentId: number, fileName: string) {
		const url = `${this.url}/episodedocuments/download/${episodeDocumentId}`;
		this.viewPdfService.showDialog(
			url,
			fileName,
		);
	}
}
