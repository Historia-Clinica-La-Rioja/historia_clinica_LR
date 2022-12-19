import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
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
    return this.http.post<any>(url, file);
  }
}
