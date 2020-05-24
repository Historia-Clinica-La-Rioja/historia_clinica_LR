import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { PublicInfoDto } from '@api-rest/api-model';
import { HttpClient } from '@angular/common/http';

import { environment } from '@environments/environment';
@Injectable({
	providedIn: 'root'
})
export class PublicService {

	constructor(
		private http: HttpClient,
	) { }

	public getInfo(): Observable<PublicInfoDto> {
		return this.http.get<PublicInfoDto>(`${environment.apiBase}/public/info`);
	}
}
