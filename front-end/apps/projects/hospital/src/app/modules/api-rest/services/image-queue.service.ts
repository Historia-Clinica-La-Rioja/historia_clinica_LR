import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ImageQueueFilteringCriteriaDto, ImageQueueListDto, PageDto } from '@api-rest/api-model';
import { ContextService } from '@core/services/context.service';
import { environment } from '@environments/environment';
import { Observable } from 'rxjs';

@Injectable({
	providedIn: 'root'
})
export class ImageQueueService {

	constructor(
		private readonly http: HttpClient,
		private readonly contextService: ContextService
	) { }


	getImageQueueList(pageSize: number, pageNumber: number, filterValues?: ImageQueueFilteringCriteriaDto ):Observable<PageDto<ImageQueueListDto>> {
		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/image-queue`;
		let queryParam = new HttpParams().append('pageNumber', pageNumber).append('pageSize', pageSize);
		return this.http.post<PageDto<ImageQueueListDto>>(url, filterValues, {params: queryParam})
	}

	updateImageQueueRetry(idMoveImage:number): Observable<boolean> {
		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/image-queue/move-image/${idMoveImage}/retry`;
		return this.http.put<boolean>(url, {})
	}
}