import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { DocumentElectronicSignatureProfessionalStatusDto } from '@api-rest/api-model';
import { ContextService } from '@core/services/context.service';
import { environment } from '@environments/environment';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class DocumentElectronicSignatureProfessionalStatusService {
  private readonly BASE_URL: string = `${environment.apiBase}/institution/${this.contextService.institutionId}/`;
  constructor(private http: HttpClient,
    private contextService: ContextService,) { }

  getDocumentElectronicSignatureProfessionalStatusController(documentId: number): Observable<DocumentElectronicSignatureProfessionalStatusDto[]> {
    const URL = this.BASE_URL + `document/${documentId}/electronic-joint-signature/get-professionals-status`
    return this.http.get<DocumentElectronicSignatureProfessionalStatusDto[]>(URL);
  }
}
