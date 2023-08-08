import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { TerminologyQueueItemDto, TerminologyCSVDto, TerminologyECLStatusDto } from '@api-rest/api-model';
import { HttpClient } from '@angular/common/http';

import { environment } from '@environments/environment';
@Injectable({
  providedIn: 'root'
})
export class TerminologyCacheService {

	constructor(
		private readonly http: HttpClient,
	) { }

	public getQueue(): Observable<TerminologyQueueItemDto[]> {
		return this.http.get<TerminologyQueueItemDto[]>(`${environment.apiBase}/terminology/cache`);
	}

	public getStatus(): Observable<TerminologyECLStatusDto[]> {
		return this.http.get<TerminologyECLStatusDto[]>(`${environment.apiBase}/terminology/cache/status`);
	}

	public addCsv(newCsv: TerminologyCSVDto): Observable<TerminologyQueueItemDto> {
		return this.http.post<TerminologyQueueItemDto>(`${environment.apiBase}/terminology/cache`, newCsv);
	}

	public delete(terminologyId: number): Observable<void> {
		return this.http.delete<void>(`${environment.apiBase}/terminology/cache/${terminologyId}`);
	}

}
