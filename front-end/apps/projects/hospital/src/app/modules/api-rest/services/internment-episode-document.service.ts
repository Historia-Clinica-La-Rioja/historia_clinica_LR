import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { EpisodeDocumentResponseDto } from '@api-rest/api-model';
import { ContextService } from '@core/services/context.service';
import { environment } from '@environments/environment';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class InternmentEpisodeDocumentService {

  constructor(private http: HttpClient,
              private contextService: ContextService) { }

  saveInternmentEpisodeDocument(file, internmentEpisodeId: number, episodeDocumentTypeId: number): Observable<any> {
    const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/internments/${internmentEpisodeId}/episodedocuments/${episodeDocumentTypeId}`;
    return this.http.post<EpisodeDocumentResponseDto>(url, file);
  }

  getInternmentEpisodeDocuments(internmentEpisodeId: number): Observable<any> {
    const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/internments/${internmentEpisodeId}/episodedocuments`;
    return this.http.get<EpisodeDocumentResponseDto[]>(url);
  }
}
