import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '@environments/environment';
import { ContextService } from '@core/services/context.service';

@Injectable({
	providedIn: 'root'
})
export class SectorService {

	constructor(private readonly http: HttpClient, private readonly contextService: ContextService) {
	}

	getAll(): Observable<any[]> {
		const url = `${environment.apiBase}/institution/${this.contextService.institutionId}/sector`;
		return this.http.get<any[]>(url);
	}

	getAllRoomsBySectorAndSpecialty(sectorId, specialtyId): Observable<any[]> {
		const url = `${environment.apiBase}/institution/${this.contextService.institutionId}/sector/${sectorId}/specialty/${specialtyId}/rooms`;
		return this.http.get<any[]>(url);
	}
}
