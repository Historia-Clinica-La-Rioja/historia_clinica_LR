import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { TemplateNamesDto } from '@api-rest/api-model';
import { ContextService } from '@core/services/context.service';
import { environment } from '@environments/environment';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class DocumentTemplateService {

  constructor(
    private contextService: ContextService,
    private http: HttpClient
  ) { }

  getTemplatesByUserInformer(typeId: number): Observable<TemplateNamesDto[]> {
    const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/documents/templates/${typeId}/user`;
		return this.http.get<TemplateNamesDto[]>(url);
  }

  deleteTemplatesByUserInformer(typeId: number, id: number): Observable<boolean> {
    const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/documents/templates/${typeId}/${id}`;
		return this.http.delete<boolean>(url);
  }
}
