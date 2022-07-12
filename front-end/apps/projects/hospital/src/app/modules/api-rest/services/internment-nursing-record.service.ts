import { NursingRecordDto } from './../api-model.d';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ContextService } from '@core/services/context.service';
import { environment } from '@environments/environment';
import { Observable } from 'rxjs';

@Injectable({
	providedIn: 'root'
})
export class InternmentNursingRecordService {

	constructor(
		private readonly http: HttpClient,
		private readonly contextService: ContextService,
	) { }

	getInternmentNursingRecords(internmentEpisodeId: number): Observable<NursingRecordDto[]> {
		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/internments/${internmentEpisodeId}/nursingRecords`;
		return this.http.get<NursingRecordDto[]>(url);
	}
}
