import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { EquipmentDto } from '@api-rest/api-model';
import { ContextService } from '@core/services/context.service';
import { environment } from '@environments/environment';
import { Observable } from 'rxjs';

@Injectable({
	providedIn: 'root'
})
export class EquipmentService {

	constructor(
		private readonly http: HttpClient,
		private readonly contextService: ContextService
	) { }

	getBySectorId(sectorId: number): Observable<EquipmentDto[]> {
		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/equipment/sector/${sectorId}`;
		return this.http.get<EquipmentDto[]>(url);
	}
}
