

import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ElectronicSignatureInvolvedDocumentDto } from '@api-rest/api-model';
import { ContextService } from '@core/services/context.service';
import { environment } from '@environments/environment';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})

export class JointSignatureService {

	constructor(private readonly http: HttpClient,
		        private readonly contextService: ContextService) { }

	getProfessionalInvolvedDocumentListPort(): Observable<ElectronicSignatureInvolvedDocumentDto[]> {
		const url = `${environment.apiBase}/institution/${this.contextService.institutionId}/electronic-joint-signature/get-involved-document-list`;
        return this.http.get<ElectronicSignatureInvolvedDocumentDto[]>(url);
	}
}
