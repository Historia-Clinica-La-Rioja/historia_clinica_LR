import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { DigitalSignatureDocumentDto } from '@api-rest/api-model';
import { ContextService } from '@core/services/context.service';
import { environment } from '@environments/environment';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class DigitalSignatureService {

	URL: string = `${environment.apiBase}/institutions/${this.contextService.institutionId}/signature/documents`;

	constructor(private readonly http: HttpClient,
		        private readonly contextService: ContextService) { }

    getPendingDocumentsByUser(): Observable<DigitalSignatureDocumentDto[]> {
        return this.http.get<DigitalSignatureDocumentDto[]>(this.URL);
    }

    sign(documentsId: number[]): Observable<string> {
		return this.http.put<string>(this.URL, documentsId, { responseType: 'text' as 'json' });
    }
}
