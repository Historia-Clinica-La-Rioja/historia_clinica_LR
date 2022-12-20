import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { DocumentTypeDto, EpisodeDocumentResponseDto, SavedEpisodeDocumentResponseDto } from '@api-rest/api-model';
import { ContextService } from '@core/services/context.service';
import { environment } from '@environments/environment';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class InternmentEpisodeDocumentService {

  constructor(private http: HttpClient,
              private contextService: ContextService) { }

  saveInternmentEpisodeDocument(file, internmentEpisodeId: number, episodeDocumentTypeId: number): Observable<SavedEpisodeDocumentResponseDto> {
    const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/internments/${internmentEpisodeId}/episodedocuments/${episodeDocumentTypeId}`;
    return this.http.post<SavedEpisodeDocumentResponseDto>(url, file);
  }

  getInternmentEpisodeDocuments(internmentEpisodeId: number): Observable<EpisodeDocumentResponseDto[]> {
    const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/internments/${internmentEpisodeId}/episodedocuments`;
    return this.http.get<EpisodeDocumentResponseDto[]>(url);
  }

  getDocumentTypes() : Observable<DocumentTypeDto[]> {
    const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/internments/documentstypes`;
    return this.http.get<DocumentTypeDto[]>(url);
  } 
}
