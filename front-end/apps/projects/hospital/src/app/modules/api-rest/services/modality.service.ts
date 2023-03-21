import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ModalityDto } from '@api-rest/api-model';
import { ContextService } from '@core/services/context.service';
import { environment } from '@environments/environment';
import { Observable } from 'rxjs';

@Injectable({
	providedIn: 'root'
})
export class ModalityService {

	constructor(
		private readonly http: HttpClient,
		private readonly contextService: ContextService
	) { }

	getAll(): Observable<ModalityDto[]> {
		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/modality`;
		return this.http.get<ModalityDto[]>(url);
	}
}
