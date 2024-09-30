import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { StudyOrderWorkListDto } from '@api-rest/api-model';
import { ContextService } from '@core/services/context.service';
import { environment } from '@environments/environment';
import { Observable } from 'rxjs';

@Injectable({
	providedIn: 'root'
})
export class ServiceRequestWorkListControllerService {

	constructor(
		private readonly http: HttpClient,
		private readonly contextService: ContextService

	) { }

	getList(categories: string[]): Observable<StudyOrderWorkListDto[]> {
		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/service-request-work-list`;
		const params = new HttpParams().append('categories', categories.join(','));
		return this.http.get<StudyOrderWorkListDto[]>(url, { params });
	}
}
