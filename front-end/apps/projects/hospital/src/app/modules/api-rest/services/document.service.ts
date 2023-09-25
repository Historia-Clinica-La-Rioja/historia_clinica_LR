import { Injectable } from '@angular/core';
import { environment } from '@environments/environment';
import { ContextService } from '@core/services/context.service';
import {HCEDocumentDataDto} from "@api-rest/api-model";
import { ViewPdfService } from '@presentation/dialogs/view-pdf/view-pdf.service';
import { HttpClient } from '@angular/common/http';
import { saveAs } from 'file-saver';

@Injectable({
	providedIn: 'root'
})
export class DocumentService {

	constructor(
		private contextService: ContextService,
		private viewPdfService: ViewPdfService,
		private http: HttpClient
	) { }

	public downloadFile(document: HCEDocumentDataDto): void {
		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/documents/${document.id}/downloadFile`;
		this.viewPdfService.showDialog(
			url,
			document.filename,
		);
	}

	public downloadTranscribedFile(document: HCEDocumentDataDto, patientId: number): void {
		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/patient/${patientId}/service-requests/${document.id}/downloadTranscribedFile`;
		this.viewPdfService.showDialog(
			url,
			document.filename,
		);
	}

	public downloadUnnamedFile(fileId: number): void {
		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/documents/${fileId}/downloadFile`;
		this.http.get(url,
			{ responseType: 'blob' }
		).subscribe(blob =>
			saveAs(blob, 'HSI_NEW_DOCUMENT'));
	}
}
