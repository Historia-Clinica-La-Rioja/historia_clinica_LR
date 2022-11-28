import { Injectable } from '@angular/core';
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";
import { environment } from "@environments/environment";
import { BedDto, RoomDto } from "@api-rest/api-model";
import { ContextService } from '@core/services/context.service';

@Injectable({
	providedIn: 'root'
})
export class RoomService {

	constructor(private readonly http: HttpClient, private readonly contextService: ContextService) {
	}

	getAll(): Observable<RoomDto[]> {
		const url = `${environment.apiBase}/institution/${this.contextService.institutionId}/room`;
		return this.http.get<RoomDto[]>(url);
	}

	getAllBedsByRoom(roomId): Observable<BedDto[]> {
		const url = `${environment.apiBase}/institution/${this.contextService.institutionId}/room/${roomId}/beds`;
		return this.http.get<BedDto[]>(url);
	}

	getAllFreeBedsByRoom(roomId): Observable<BedDto[]> {
		const url = `${environment.apiBase}/institution/${this.contextService.institutionId}/room/${roomId}/freebeds`;
		return this.http.get<BedDto[]>(url);
	}
}
