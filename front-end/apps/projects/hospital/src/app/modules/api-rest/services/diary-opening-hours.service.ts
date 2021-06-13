import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { DiaryOpeningHoursDto, OccupationDto } from '@api-rest/api-model';
import { environment } from '@environments/environment';
import { ContextService } from '@core/services/context.service';

@Injectable({
	providedIn: 'root'
})
export class DiaryOpeningHoursService {

	constructor(
		private http: HttpClient,
		private contextService: ContextService,
	) { }

	getMany(diaryIds: number[]): Observable<DiaryOpeningHoursDto[]> {
		if (!diaryIds || diaryIds.length === 0) {
			return of([]);
		}
		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/medicalConsultations/diaryOpeningHours`;
		return this.http.get<DiaryOpeningHoursDto[]>(url, {
			params: { diaryIds: `${diaryIds.join(',')}` }
		});
	}

	getAllWeeklyDoctorsOfficeOcupation(doctorsOfficeId: number, diaryId: number, startDate, endDate): Observable<OccupationDto[]> {
		let queryParams: HttpParams = new HttpParams();
		queryParams = queryParams.append('startDate', startDate);
		queryParams = queryParams.append('endDate', endDate);
		queryParams = (diaryId) ? queryParams.append('diaryId', JSON.stringify(diaryId)) : queryParams;

		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/
					medicalConsultations/diaryOpeningHours/doctorsOffice/${doctorsOfficeId}`;
		return this.http.get<OccupationDto[]>(url,
			{
				params: queryParams
			});
	}

}
