import { Injectable } from '@angular/core';
import { HttpClient } from "@angular/common/http";
import { ContextService } from "@core/services/context.service";
import { TriageAdministrativeDto, TriageDto } from "@api-rest/api-model";
import { environment } from "@environments/environment";
import { Observable } from "rxjs";

const BASIC_URL_PREFIX = '/emergency-care/episodes';
const BASIC_URL_SUFIX = '/triage';

@Injectable({
	providedIn: 'root'
})
export class TriageService {

	constructor(private http: HttpClient,
	            private contextService: ContextService) {
	}

	createAdministrative(episodeId: number, triage: TriageAdministrativeDto): Observable<number> {
		let url = `${environment.apiBase}/institutions/${this.contextService.institutionId +
		BASIC_URL_PREFIX}/${episodeId}/${BASIC_URL_SUFIX}`;
		return this.http.post<number>(url, triage);
	}

}
