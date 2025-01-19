import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';

import { environment } from '@environments/environment';
import { NotificationTemplateRenderDto } from '@api-rest/api-model';

@Injectable({
  providedIn: 'root'
})
export class NotificationTemplateService {

	constructor(
		private readonly http: HttpClient,
	) { }

	public list(): Observable<NotificationTemplateRenderDto[]> {
		return this.http.get<any[]>(`${environment.apiBase}/notification/mails`);
	}

}
