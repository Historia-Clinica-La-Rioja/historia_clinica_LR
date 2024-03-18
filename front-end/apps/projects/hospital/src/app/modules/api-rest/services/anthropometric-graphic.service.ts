import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ContextService } from '@core/services/context.service';
import { environment } from '@environments/environment';
import { Observable } from 'rxjs';

@Injectable({
	providedIn: 'root'
})
export class AnthropometricGraphicService {

	private readonly prefixUrl = `${environment.apiBase}/institution`;
	private readonly basicUrl = `/percentiles`;

	constructor(
		private readonly http: HttpClient,
		private readonly contextService: ContextService
	) { }

	canShowPercentilesGraphic(patientId: number): Observable<boolean> {
		const url = `${this.prefixUrl}/${this.contextService.institutionId}${this.basicUrl}/patient/${patientId}/can-show-graphic`;
		return this.http.get<boolean>(url);
	}
}
