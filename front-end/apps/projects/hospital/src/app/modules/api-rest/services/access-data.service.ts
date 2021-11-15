import { Injectable } from '@angular/core';
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";
import { environment } from "@environments/environment";
import { AccessDataDto } from "@api-rest/api-model";

@Injectable({
	providedIn: 'root'
})
export class AccessDataService {

	constructor(private http: HttpClient) {
	}

	updateAccessData(accessDataDto: AccessDataDto): Observable<void> {
		const url = `${environment.apiBase}/auth/access-data`;
		return this.http.put<void>(url, accessDataDto);
	}
}
