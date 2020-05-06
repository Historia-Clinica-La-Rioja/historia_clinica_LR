import { Injectable } from '@angular/core';
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";
import { environment } from "@environments/environment";

@Injectable({
	providedIn: 'root'
})
export class SectorService {

	constructor(private http: HttpClient) {
	}

	getAll(): Observable<any[]> {
		let url = `${environment.apiBase}/sector`;
		return this.http.get<any[]>(url);
	}

	getAllRoomsBySectorAndSpecialty(sectorId, specialtyId): Observable<any[]> {
		let url = `${environment.apiBase}/sector/${sectorId}/specialty/${specialtyId}/rooms`;
		return this.http.get<any[]>(url);
	}
}
