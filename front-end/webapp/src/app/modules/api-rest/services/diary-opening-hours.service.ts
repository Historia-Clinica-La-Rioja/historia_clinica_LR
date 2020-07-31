import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { DiaryOpeningHoursDto } from '@api-rest/api-model';
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

	getMany(ids: number[]): Observable<DiaryOpeningHoursDto[]> {
		if (!ids || ids.length === 0) {
			return of([]);
		}
		let url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/medicalConsultations/diaryOpeningHours`;
		return this.http.get<DiaryOpeningHoursDto[]>(url,{
			params: { ids: `${ids.join(',')}` }
		});
	}


}
