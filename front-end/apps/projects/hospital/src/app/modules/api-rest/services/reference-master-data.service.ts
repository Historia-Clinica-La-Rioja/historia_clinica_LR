import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '@environments/environment';
import { MasterDataDto } from '@api-rest/api-model';

@Injectable({
	providedIn: 'root'
})
export class ReferenceMasterDataService {

	private readonly URL_BASE: string;

	constructor(private http: HttpClient) {
		this.URL_BASE = `${environment.apiBase}/reference/masterdata`
	}

	getClosureTypes(): Observable<MasterDataDto[]> {
		const url = `${this.URL_BASE}/closure-types`;
		return this.http.get<MasterDataDto[]>(url);
	}

	getPriorities(): Observable<MasterDataDto[]> {
		const url = `${this.URL_BASE}/priorities`;
		return this.http.get<MasterDataDto[]>(url);
	}
}
