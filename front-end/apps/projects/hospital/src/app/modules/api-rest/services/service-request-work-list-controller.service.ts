import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { PageDto, StudyOrderWorkListDto } from '@api-rest/api-model';
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

	getList(categories: string[], pageNumber: number, pageSize: number): Observable<PageDto<StudyOrderWorkListDto[]>> {
		let params: HttpParams = new HttpParams()
			.set('pageNumber', pageNumber)
			.set('pageSize', pageSize)
			.set('categories', categories.join(','));
		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/service-request-work-list`;
		return this.http.get<PageDto<StudyOrderWorkListDto[]>>(url, { params });
	}
}
