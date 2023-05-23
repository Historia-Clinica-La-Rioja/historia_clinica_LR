import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { PermissionsDto, LoggedUserDto, TerminologyQueueItemDto, TerminologyCSVDto } from '@api-rest/api-model';
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

	public addCsv(newCsv: TerminologyCSVDto): Observable<TerminologyQueueItemDto> {
		console.log('post ', newCsv);
		return this.http.post<TerminologyQueueItemDto>(`${environment.apiBase}/terminology/cache`, newCsv);
	}

}
