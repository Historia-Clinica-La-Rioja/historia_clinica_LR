import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { TextTemplateDto } from '@api-rest/api-model';
import { ContextService } from '@core/services/context.service';
import { environment } from '@environments/environment';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ReportDetailsTemplateService {

  constructor(
    private contextService: ContextService,
    private http: HttpClient
  ) { }


  saveTemplate(templateBody: TextTemplateDto): Observable<boolean> {
		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/documents/report-details/templates`;
		return this.http.post<boolean>(url, templateBody);
	}

  getOneTemplate(templateId: number): Observable<TextTemplateDto> {
    const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/documents/report-details/templates/${templateId}`;
		return this.http.get<TextTemplateDto>(url);
  }
}
