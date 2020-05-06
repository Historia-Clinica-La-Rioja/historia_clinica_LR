import { Injectable } from '@angular/core';
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";
import { environment } from "@environments/environment";

@Injectable({
	providedIn: 'root'
})
export class ClinicalSpecialtySectorService {

	constructor(private http: HttpClient) {
	}

	getClinicalSpecialty(sectorId): Observable<any[]> {
		let url = `${environment.apiBase}/sector/${sectorId}/clinicalspecialty`;
		return this.http.get<any[]>(url);
	}
}
