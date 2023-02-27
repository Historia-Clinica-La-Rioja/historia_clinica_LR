import { Injectable } from '@angular/core';
import { environment } from '@environments/environment';
import { ContextService } from '@core/services/context.service';
import {HCEDocumentDataDto} from "@api-rest/api-model";
import { ViewPdfService } from '@presentation/dialogs/view-pdf/view-pdf.service';

@Injectable({
	providedIn: 'root'
})
export class DocumentService {

	constructor(
		private contextService: ContextService,
		private viewPdfService: ViewPdfService,
	) { }

	public downloadFile(document: HCEDocumentDataDto): void {
		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/documents/${document.id}/downloadFile`;
		this.viewPdfService.showDialog(
			url,
			document.filename,
		);
	}
}
