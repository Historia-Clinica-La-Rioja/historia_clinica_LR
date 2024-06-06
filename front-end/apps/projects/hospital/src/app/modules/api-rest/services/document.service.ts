import { Injectable } from '@angular/core';
import { environment } from '@environments/environment';
import { ContextService } from '@core/services/context.service';
import {DocumentDto, HCEDocumentDataDto} from "@api-rest/api-model";
import { HttpClient } from '@angular/common/http';
import { saveAs } from 'file-saver';
import { Observable } from 'rxjs';
import { DownloadService } from '@core/services/download.service';

@Injectable({
	providedIn: 'root'
})
export class DocumentService {

	constructor(
		private contextService: ContextService,
		private downloadService: DownloadService,
		private http: HttpClient
	) { }

	public downloadFile(document: HCEDocumentDataDto): void {
		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/documents/${document.id}/downloadFile`;
		this.downloadService.fetchFile(
			url,
			document.filename,
		);
	}

	public getTranscribedFileUrl(documentId: number, patientId: number): string {
		return `${environment.apiBase}/institutions/${this.contextService.institutionId}/patient/${patientId}/service-requests/${documentId}/downloadTranscribedFile`;
	}

	public downloadUnnamedFile(fileId: number): void {
		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/documents/${fileId}/downloadFile`;
		this.http.get(url,
			{ responseType: 'blob' }
		).subscribe(blob =>
			saveAs(blob, 'HSI_NEW_DOCUMENT'));
	}

	getDocumentInfo(id: number): Observable<DocumentDto> {
		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/documents/${id}/info`;
		return this.http.get<DocumentDto>(url);
	}
}
