import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ContextService } from '@core/services/context.service';
import { environment } from '@environments/environment';
import { Observable } from 'rxjs';

@Injectable({
	providedIn: 'root'
})
export class PresentationUnitsService {

	private BASE_URL = `${environment.apiBase}/institutions/${this.contextService.institutionId}`;

	constructor(
		private http: HttpClient,
		private readonly contextService: ContextService,
	) { }

	getMedicationPresentationUnits(medicationSctid: string): Observable<number[]> {
		const url = `${this.BASE_URL}/snomed-medication/${medicationSctid}/get-presentation-units`;
		return this.http.get<number[]>(url);
	}
}
