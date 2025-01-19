import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '@environments/environment';
import { ContextService } from '@core/services/context.service';
import { AttentionPlacesQuantityDto, MasterDataDto, SectorDto } from '@api-rest/api-model';

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

	getAllSectorByType(sectorType: number): Observable<SectorDto[]> {
		const url = `${environment.apiBase}/institution/${this.contextService.institutionId}/sector/sectoroftype/${sectorType}`;
		return this.http.get<SectorDto[]>(url);
	}

	getTypes(): Observable<MasterDataDto[]> {
		const url = `${environment.apiBase}/institution/${this.contextService.institutionId}/sector/sectortype`;
		return this.http.get<MasterDataDto[]>(url);
	}

	quantityAttentionPlacesBySectorType(sectorTypeId: number): Observable<AttentionPlacesQuantityDto> {
		const url = `${environment.apiBase}/institution/${this.contextService.institutionId}/sector/attentionPlaces/${sectorTypeId}`;
		return this.http.get<AttentionPlacesQuantityDto>(url);
	}
}

