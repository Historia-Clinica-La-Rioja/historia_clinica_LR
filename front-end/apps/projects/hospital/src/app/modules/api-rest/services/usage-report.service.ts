import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';

import { environment } from '@environments/environment';

import {
	UsageReportStatusDto,
	SendUsageReportDto,
} from '@api-rest/api-model';


@Injectable({
  providedIn: 'root'
})
export class UsageReportService {

	constructor(
		private readonly http: HttpClient,
	) { }

	public getStatus(): Observable<UsageReportStatusDto> {
		return this.http.get<UsageReportStatusDto>(`${environment.apiBase}/usage-report`);
	}
	public sendReport(): Observable<SendUsageReportDto> {
		return this.http.post<SendUsageReportDto>(`${environment.apiBase}/usage-report`, undefined);
	}
}
