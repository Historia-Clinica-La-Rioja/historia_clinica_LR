import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '@environments/environment';
import { ContextService } from '@core/services/context.service';
import { saveAs } from 'file-saver';
import {HCEDocumentDataDto} from "@api-rest/api-model";

@Injectable({
	providedIn: 'root'
})
export class DocumentService {

	constructor(
		private contextService: ContextService,
		private http: HttpClient
	) { }

	public downloadFile(document: HCEDocumentDataDto): void {
		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/documents/${document.id}/downloadFile`;
		this.http.get(url,
			{ responseType: 'blob' }
		).subscribe(blob =>
			saveAs(blob, document.filename));
	}
}
