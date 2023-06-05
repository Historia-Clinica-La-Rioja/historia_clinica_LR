import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { ContextService } from '@core/services/context.service';
import { environment } from '@environments/environment';
import { Observable } from 'rxjs';
import { DownloadService } from '@core/services/download.service';
import { DateFormat, momentFormat } from '@core/utils/moment.utils';
import { UIComponentDto } from '@extensions/extensions-model';

@Injectable({
	providedIn: 'root'
})
export class ReportsService {

	constructor(
		private contextService: ContextService,
		private downloadService: DownloadService,
		private http: HttpClient
	) { }

	private getReport(params: any, fileName: string, url: any): Observable<any> {
		let requestParams: HttpParams = new HttpParams();
		requestParams = requestParams.append('fromDate', momentFormat(params.startDate, DateFormat.API_DATE));
		requestParams = requestParams.append('toDate', momentFormat(params.endDate, DateFormat.API_DATE));
		if (params.specialtyId) {
			requestParams = requestParams.append('clinicalSpecialtyId', params.specialtyId);
		}
		if (params.professionalId) {
			requestParams = requestParams.append('doctorId', params.professionalId);
		}
		return this.downloadService.downloadXlsWithRequestParams(url, fileName, requestParams);
	}

	getMonthlyReport(params: any, fileName: string): Observable<any> {
		const url = `${environment.apiBase}/reports/${this.contextService.institutionId}/monthly`;
		return this.getReport(params, fileName, url);
	}

	getOutpatientSummaryReport(params: any, fileName: string): Observable<any> {
		const url = `${environment.apiBase}/reports/${this.contextService.institutionId}/summary`;
		return this.getReport(params, fileName, url);
	}


	getDiabetesReport(): Observable<UIComponentDto> {
		const url = `${environment.apiBase}/reports/institution/${this.contextService.institutionId}/diabetes`;
		return this.http.get<UIComponentDto>(url);
	}

}
