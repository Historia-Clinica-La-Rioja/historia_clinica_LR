import { Injectable } from '@angular/core';
import { DiaryOpeningHoursDto } from '@api-rest/api-model';
import { Observable, of } from 'rxjs';

@Injectable({
	providedIn: 'root'
})
export class EquipmentDiaryService {

	constructor(	) { }

	addEquipmentDiary(equipmentDiary: EquipmentDiary): Observable<number> {
		//const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/medicalConsultations/equipmentDiary`;
		//return this.http.post<number>(url, equipmentDiary);
		return of(1);
	}

}

export interface EquipmentDiary {
	equipmentId: number;
	appointmentDuration: number;
	automaticRenewal?: boolean;
	equipmentDiaryOpeningHours: DiaryOpeningHoursDto[];
	endDate: string;
	includeHoliday?: boolean;
	startDate: string;
}