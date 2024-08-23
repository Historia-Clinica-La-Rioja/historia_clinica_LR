import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { EmergencyCareAttentionPlaceDto } from '@api-rest/api-model';
import { ContextService } from '@core/services/context.service';
import { environment } from '@environments/environment';
import { Observable } from 'rxjs';

@Injectable({
	providedIn: 'root'
})
export class EmergencyCareAttentionPlaceService {

	private BASE_URL: string;

	constructor(
		private http: HttpClient,
		private contextService: ContextService,
	) {
		this.contextService.institutionId$.subscribe(institutionId =>
			this.BASE_URL = `${environment.apiBase}/institution/${institutionId}/emergency-care/attention-places`
		)
	}

	getAttentionPlaces(): Observable<EmergencyCareAttentionPlaceDto[]> {
		const url = `${this.BASE_URL}`;
		return this.http.get<EmergencyCareAttentionPlaceDto[]>(url);
	}
}

