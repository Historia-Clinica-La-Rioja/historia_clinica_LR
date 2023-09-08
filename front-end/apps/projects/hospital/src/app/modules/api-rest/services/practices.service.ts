import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { SharedSnomedDto } from '@api-rest/api-model';
import { ContextService } from '@core/services/context.service';
import { environment } from '@environments/environment';
import { Observable } from 'rxjs/internal/Observable';

@Injectable({
	providedIn: 'root'
})
export class PracticesService {

	constructor(
		private readonly http: HttpClient,
		private readonly contextService: ContextService,
	) { }

	get(): Observable<SharedSnomedDto[]> {
		const url = `${environment.apiBase}/institution/${this.contextService.institutionId}/practices/by-institution`;
		return this.http.get<SharedSnomedDto[]>(url);
	}
}
