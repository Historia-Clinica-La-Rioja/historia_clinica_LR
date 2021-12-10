import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { SnvsEventManualClassificationsDto } from "@api-rest/api-model";
import { environment } from "@environments/environment";
import { HttpClient } from "@angular/common/http";

@Injectable({
	providedIn: 'root'
})
export class SnvsMasterDataService {

	constructor(
		private readonly http: HttpClient
	) { }

	fetchManualClassification(params): Observable<SnvsEventManualClassificationsDto[]> {
		const url = `${environment.apiBase}/snvs/manual-classifications`;
		return this.http.get<SnvsEventManualClassificationsDto[]>(url, { params });
	}

}
