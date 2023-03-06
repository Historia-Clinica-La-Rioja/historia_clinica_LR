import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { OccupationDto } from '@api-rest/api-model';
import { ContextService } from '@core/services/context.service';
import { environment } from '@environments/environment';
import { Observable } from 'rxjs';

@Injectable({
	providedIn: 'root'
})
export class EquipmentDiaryOpeningHoursService {

	constructor(
		private readonly http: HttpClient,
		private readonly contextService: ContextService
	) { }

	getAllWeeklyEquipmentOcupation(equipmentId: number, diaryId: number, startDate: string, endDate: string): Observable<OccupationDto[]> {
		let queryParams: HttpParams = new HttpParams();
		queryParams = queryParams.append('startDate', startDate);
		queryParams = queryParams.append('endDate', endDate);
		queryParams = (diaryId) ? queryParams.append('equipmentDiaryId', JSON.stringify(diaryId)) : queryParams;

		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/medicalConsultations/equipmentDiaryOpeningHours/equipment/${equipmentId}`;
		return this.http.get<OccupationDto[]>(url,
			{
				params: queryParams
			});
	}
}
