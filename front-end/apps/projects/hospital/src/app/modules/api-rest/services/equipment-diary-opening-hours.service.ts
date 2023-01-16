import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';

const MOCK = [
	{
		description: "lunes",
		id: 1,
		timeRanges: [{
			from: "05:00:00",
			to: "08:40:00"
		}]
	}
];

@Injectable({
	providedIn: 'root'
})
export class EquipmentDiaryOpeningHoursService {

	constructor() { }


	getAllWeeklyEquipmentOcupation(equipmentId: number, diaryId: number, startDate, endDate): Observable<any[]> {
		/*let queryParams: HttpParams = new HttpParams();
		queryParams = queryParams.append('startDate', startDate);
		queryParams = queryParams.append('endDate', endDate);
		queryParams = (diaryId) ? queryParams.append('diaryId', JSON.stringify(diaryId)) : queryParams;

		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/medicalConsultations/diaryOpeningHours/equipment/${equipmentId}`;
		return this.http.get<OccupationDto[]>(url,
			{
				params: queryParams
			});*/
		return of(MOCK);
	}
}
