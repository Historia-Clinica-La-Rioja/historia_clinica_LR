import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { IsolationStatusMasterDataDto } from '@api-rest/api-model';
import { environment } from '@environments/environment';
import { Observable } from 'rxjs';

@Injectable({
	providedIn: 'root'
})
export class IsolationAlertMasterDataService {
	private readonly BASIC_URL = `${environment.apiBase}/isolation-alerts/master-data`;

	constructor(
		private http: HttpClient,
	) { }

	getStatus(): Observable<IsolationStatusMasterDataDto[]> {
		const url = `${this.BASIC_URL}/status`;
		return this.http.get<IsolationStatusMasterDataDto[]>(url);
	}
}
