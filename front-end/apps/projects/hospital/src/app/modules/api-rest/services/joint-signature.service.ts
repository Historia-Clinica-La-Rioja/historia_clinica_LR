

import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { DocumentElectronicSignatureProfessionalStatusDto, ElectronicSignatureInvolvedDocumentDto } from '@api-rest/api-model';
import { ContextService } from '@core/services/context.service';
import { environment } from '@environments/environment';
import { Observable } from 'rxjs';

@Injectable({
	providedIn: 'root'
})

export class JointSignatureService {

	private readonly BASE_URL: string = `${environment.apiBase}/institution/${this.contextService.institutionId}/`;

	constructor(private readonly http: HttpClient,
		private readonly contextService: ContextService) { }

	getProfessionalInvolvedDocumentListPort(): Observable<ElectronicSignatureInvolvedDocumentDto[]> {
		const url = `${environment.apiBase}/institution/${this.contextService.institutionId}/electronic-joint-signature/get-involved-document-list`;
        return this.http.get<ElectronicSignatureInvolvedDocumentDto[]>(url);
	}

	getDocumentElectronicSignatureProfessionalStatusController(documentId: number): Observable<DocumentElectronicSignatureProfessionalStatusDto[]> {
		const URL = this.BASE_URL + `document/${documentId}/electronic-joint-signature/get-professionals-status`
		return this.http.get<DocumentElectronicSignatureProfessionalStatusDto[]>(URL);
	}
}
