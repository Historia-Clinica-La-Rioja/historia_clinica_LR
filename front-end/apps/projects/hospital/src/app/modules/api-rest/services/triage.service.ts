import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { ContextService } from '@core/services/context.service';
import {
	TriageAdministrativeDto,
	TriageAdultGynecologicalDto,
	TriageListDto,
	TriagePediatricDto
} from '@api-rest/api-model';
import { environment } from '@environments/environment';
import { Observable } from 'rxjs';

const URL_PREFIX = '/emergency-care/episodes';
const URL_SUFIX = '/triage';

@Injectable({
	providedIn: 'root'
})
export class TriageService {

	constructor(
		private http: HttpClient,
		private contextService: ContextService
	) { }

	createAdministrative(episodeId: number, triage: TriageAdministrativeDto): Observable<number> {
		const url = `${environment.apiBase}/institution/${this.contextService.institutionId +
		URL_PREFIX}/${episodeId}${URL_SUFIX}`;
		return this.http.post<number>(url, triage);
	}

	newAdultGynecological(episodeId: number, triage: TriageAdultGynecologicalDto): Observable<number> {
		const url = `${environment.apiBase}/institution/${this.contextService.institutionId +
		URL_PREFIX}/${episodeId}${URL_SUFIX}/adult-gynecological`;
		return this.http.post<number>(url, triage);
	}

	newPediatric(episodeId: number, triage: TriagePediatricDto): Observable<number> {
		const url = `${environment.apiBase}/institution/${this.contextService.institutionId +
		URL_PREFIX}/${episodeId}${URL_SUFIX}/pediatric`;
		return this.http.post<number>(url, triage);
	}

	getAll(episodeId: number): Observable<TriageListDto[]> {
		const url = `${environment.apiBase}/institution/${this.contextService.institutionId +
			URL_PREFIX}/${episodeId}${URL_SUFIX}`;
		return this.http.get<TriageListDto[]>(url);
	}

}
