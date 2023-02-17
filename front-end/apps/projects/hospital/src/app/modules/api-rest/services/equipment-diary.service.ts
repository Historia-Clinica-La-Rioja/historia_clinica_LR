import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { CompleteEquipmentDiaryDto, EquipmentDiaryADto, EquipmentDiaryDto } from '@api-rest/api-model';
import { ContextService } from '@core/services/context.service';
import { environment } from '@environments/environment';
import { Observable } from 'rxjs';

@Injectable({
	providedIn: 'root'
})
export class EquipmentDiaryService {

	private readonly BASE_URL: string;

	constructor(
		private readonly http: HttpClient,
		private readonly contextService: ContextService
	) {
		this.BASE_URL =  `${environment.apiBase}/institutions/${this.contextService.institutionId}/medicalConsultations/equipmentDiary`;
	}

	addEquipmentDiary(equipmentDiary: EquipmentDiaryADto): Observable<number> {
		return this.http.post<number>(this.BASE_URL, equipmentDiary);
	}

	getDiariesBy(equipmentId: number): Observable<EquipmentDiaryDto[]> {
		const url = `${this.BASE_URL}/equipment/${equipmentId}`;
		return this.http.get<EquipmentDiaryDto[]>(url);
	}

	getBy(equipmentDiaryId: number): Observable<CompleteEquipmentDiaryDto> {
		const url = `${this.BASE_URL}/${equipmentDiaryId}`;
		return this.http.get<CompleteEquipmentDiaryDto>(url);
	}

	updateEquipmentDiary(equipmentDiary: EquipmentDiaryADto,equipmentDiaryId:number): Observable<number> {
		const url = `${this.BASE_URL}/${equipmentDiaryId}`;
			return this.http.put<number>(url, equipmentDiary);
	}
}
