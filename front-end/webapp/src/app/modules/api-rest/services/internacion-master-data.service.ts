import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '@environments/environment';
import { MasterDataInterface } from '@api-rest/api-model';

@Injectable({
	providedIn: 'root'
})
export class InternacionMasterDataService {

	constructor(private http: HttpClient) {
	}

	getHealthClinical(): Observable<any[]> {
		let url = `${environment.apiBase}/internments/masterdata/health/clinical`;
		console.log('url', url);
		return this.http.get<any[]>(url);
	}

	getHealthVerification(): Observable<MasterDataInterface<string>[]> {
		let url = `${environment.apiBase}/internments/masterdata/health/verification`;
		return this.http.get<[]>(url);
	}
}
