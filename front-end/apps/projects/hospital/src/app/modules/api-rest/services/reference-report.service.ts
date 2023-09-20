import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ReferenceCompleteDataDto, ReferenceReportDto } from '@api-rest/api-model';
import { ContextService } from '@core/services/context.service';
import { DateFormat } from '@core/utils/date.utils';
import { environment } from '@environments/environment';
import { format } from 'date-fns';
import { Observable } from 'rxjs';

@Injectable({
	providedIn: 'root'
})
export class ReferenceReportService {

	private readonly BASE_URL: string;
	private readonly PREFIX_URL: string;

	constructor(
		private readonly http: HttpClient,
		private readonly contextService: ContextService,
	) {
		this.BASE_URL = `${environment.apiBase}/institutions`;
		this.PREFIX_URL = `references-report`;
	}

	getAllReceivedReferences(from: Date, to: Date): Observable<ReferenceReportDto[]> {
		const url = `${this.BASE_URL}/${this.contextService.institutionId}/${this.PREFIX_URL}/received`;
		let params: HttpParams = new HttpParams();

		params = params.append('from', format(from, DateFormat.API_DATE));
		params = params.append('to', format(to, DateFormat.API_DATE));
		return this.http.get<ReferenceReportDto[]>(url, { params });
	}

	getAllRequestedReferences(from: Date, to: Date): Observable<ReferenceReportDto[]> {
		const url = `${this.BASE_URL}/${this.contextService.institutionId}/${this.PREFIX_URL}/requested`;
		let params: HttpParams = new HttpParams();

		params = params.append('from', format(from, DateFormat.API_DATE));
		params = params.append('to', format(to, DateFormat.API_DATE));
		return this.http.get<ReferenceReportDto[]>(url, { params });
	}

	getReferenceDetail(id: number): Observable<ReferenceCompleteDataDto> {
		const url = `${this.BASE_URL}/${this.contextService.institutionId}/${this.PREFIX_URL}/reference-detail/${id}`;
		return this.http.get<ReferenceCompleteDataDto>(url);
	}

}
