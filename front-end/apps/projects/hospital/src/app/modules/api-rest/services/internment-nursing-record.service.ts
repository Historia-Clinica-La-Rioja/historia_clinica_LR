import { NursingRecordDto } from './../api-model.d';
import { HttpClient, HttpParams } from '@angular/common/http';
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

	updateNursingRecord(internmentEpisodeId: number, nursingRecordId: number, data: any): Observable<boolean> {
		let queryParams: HttpParams = new HttpParams();
		queryParams = queryParams.append('status', data.status);
		const body = data.administrationTime ? data.administrationTime : null;
		queryParams = queryParams.append('reason', data.reason);
		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/internments/${internmentEpisodeId}/nursingRecords/${nursingRecordId}/change-status`;
		return this.http.put<boolean>(url, body, { params: queryParams });
	}
}
