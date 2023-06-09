import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { PacsUrlDto } from '@api-rest/api-model';
import { ContextService } from '@core/services/context.service';
import { environment } from '@environments/environment';
import { Observable } from 'rxjs';

@Injectable({
	providedIn: 'root'
})
export class StudyPACAssociationService {

	private readonly BASE_URL: string;

	constructor(
		private readonly contextService: ContextService,
		private http: HttpClient,
	) {
		this.BASE_URL = `${environment.apiBase}/institutions/${this.contextService.institutionId}/imagenetwork/pacs`
	 }

	getPacGlobalURL(studyInstanceUID: string): Observable<PacsUrlDto> {
		const url = `${this.BASE_URL}/${studyInstanceUID}`
		return this.http.get<PacsUrlDto>(url);
	}
}
