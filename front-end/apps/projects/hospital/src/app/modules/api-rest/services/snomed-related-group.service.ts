import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { SnomedRelatedGroupDto } from '@api-rest/api-model';
import { ContextService } from '@core/services/context.service';
import { environment } from '@environments/environment';
import { Observable } from 'rxjs/internal/Observable';

@Injectable({
	providedIn: 'root'
})
export class SnomedRelatedGroupService {

	constructor(
		private readonly http: HttpClient,
		private readonly contextService: ContextService,
	) { }

	getPractices(): Observable<SnomedRelatedGroupDto[]> {
		const url = `${environment.apiBase}/institution/${this.contextService.institutionId}/snomed-related-group/practices`;
		return this.http.get<SnomedRelatedGroupDto[]>(url);
	}
}
