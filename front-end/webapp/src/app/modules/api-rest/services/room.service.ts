import { Injectable } from '@angular/core';
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";
import { environment } from "@environments/environment";
import { BedDto } from "@api-rest/api-model";

@Injectable({
	providedIn: 'root'
})
export class RoomService {

	constructor(private http: HttpClient) {
	}

	getAllBedsByRoom(roomId): Observable<BedDto[]> {
		let url = `${environment.apiBase}/room/${roomId}/beds`;
		return this.http.get<BedDto[]>(url);
	}
}
