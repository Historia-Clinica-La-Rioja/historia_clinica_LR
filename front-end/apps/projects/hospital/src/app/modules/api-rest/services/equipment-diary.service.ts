import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { EquipmentDiaryADto } from '@api-rest/api-model';
import { ContextService } from '@core/services/context.service';
import { environment } from '@environments/environment';
import { Observable } from 'rxjs';

@Injectable({
	providedIn: 'root'
})
export class EquipmentDiaryService {

	constructor(
		private readonly http: HttpClient, 
		private readonly contextService: ContextService
	) { }
	
	addEquipmentDiary(equipmentDiary: EquipmentDiaryADto): Observable<number> {
		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/medicalConsultations/equipmentDiary`;
		return this.http.post<number>(url, equipmentDiary);
	}
}
