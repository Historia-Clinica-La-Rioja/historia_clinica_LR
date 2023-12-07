import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { SnomedDto } from '@api-rest/api-model';
import { ContextService } from '@core/services/context.service';
import { environment } from '@environments/environment';
import { Observable } from 'rxjs';

@Injectable({
	providedIn: 'root'
})
export class CareLineInstitutionPracticeService {

	private readonly BASE_URL: string;

	constructor(
		private readonly http: HttpClient,
		private readonly contextService: ContextService,
	) {
		this.BASE_URL = `${environment.apiBase}/institution`;
	}

	getPracticesByCareLine(careLineId: number): Observable<SnomedDto[]> {
		const url = `${this.BASE_URL}/${this.contextService.institutionId}/careline-institution-practice/careLine/${careLineId}`;
		return this.http.get<SnomedDto[]>(url);
	}

}
