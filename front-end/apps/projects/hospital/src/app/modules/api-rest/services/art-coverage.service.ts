import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '@environments/environment';
import { Observable } from 'rxjs';
import { ARTCoverageDto } from '@api-rest/api-model';

@Injectable({
	providedIn: 'root'
})
export class ArtCoverageService {

	constructor(
		private http: HttpClient
	) {
	}

	getAll(): Observable<ARTCoverageDto[]> {
		const url = `${environment.apiBase}/ART`;
		return this.http.get<ARTCoverageDto[]>(url);
	}

}
