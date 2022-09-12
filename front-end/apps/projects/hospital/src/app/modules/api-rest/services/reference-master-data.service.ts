import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '@environments/environment';

@Injectable({
	providedIn: 'root'
})
export class ReferenceMasterDataService {

	constructor(private http: HttpClient) { }

	getClosureTypes(): Observable<any[]> {
		const url = `${environment.apiBase}/reference/masterdata/closure-types`;
		return this.http.get<any[]>(url);
	}
}
