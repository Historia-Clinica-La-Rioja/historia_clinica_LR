import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import {
	TerminologyQueueItemDto,
	TerminologyCSVDto,
	TerminologyECLStatusDto,
	ETerminologyKind,
} from '@api-rest/api-model';
import { environment } from '@environments/environment';

@Injectable({
  providedIn: 'root'
})
export class TerminologyCacheService {

	constructor(
		private readonly http: HttpClient,
	) { }

	public getQueue(kind: ETerminologyKind): Observable<TerminologyQueueItemDto[]> {
		return this.http.get<TerminologyQueueItemDto[]>(`${environment.apiBase}/terminology/cache/${kind}`);
	}

	public getStatus(kind: ETerminologyKind): Observable<TerminologyECLStatusDto[]> {
		return this.http.get<TerminologyECLStatusDto[]>(`${environment.apiBase}/terminology/cache/${kind}/status`);
	}

	public addCsv(newCsv: TerminologyCSVDto): Observable<TerminologyQueueItemDto> {
		return this.http.post<TerminologyQueueItemDto>(`${environment.apiBase}/terminology/cache`, newCsv);
	}

	public delete(terminologyId: number): Observable<void> {
		return this.http.delete<void>(`${environment.apiBase}/terminology/cache/${terminologyId}`);
	}

}
