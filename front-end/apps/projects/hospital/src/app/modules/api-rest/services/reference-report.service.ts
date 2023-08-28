import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ReferenceReportDto } from '@api-rest/api-model';
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

	constructor(
		private readonly http: HttpClient,
		private readonly contextService: ContextService,
	) {
		this.BASE_URL = `${environment.apiBase}/institutions`;
	}

	getAllReceivedReferences(from: Date, to: Date): Observable<ReferenceReportDto[]> {
		const url = `${this.BASE_URL}/${this.contextService.institutionId}/references-report/received`;
		let params: HttpParams = new HttpParams();
		
		params = params.append('from', format(from, DateFormat.API_DATE));
		params = params.append('to', format(to, DateFormat.API_DATE));
		return this.http.get<ReferenceReportDto[]>(url, { params });
	}

}
