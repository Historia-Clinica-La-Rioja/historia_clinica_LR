import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
	providedIn: 'root'
})
export class OdontologyService {

	private readonly ODONTOLOGY_BASE = 'api/odontology';

	constructor(
		private http: HttpClient
	) {
	}

	getInfo(): Observable<string> {
		const url = `${this.ODONTOLOGY_BASE}/info`;
		return this.http.get<string>(url, { responseType: 'text' as 'json' });
	}

}
