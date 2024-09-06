import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { ContextService } from '@core/services/context.service';
import { environment } from '@environments/environment';
import { Observable, map } from 'rxjs';
import {
	AddInstitutionReportToQueueDto,
	PageDto,
	InstitutionReportQueryDto,
	InstitutionReportQueuedDto,
} from '@api-rest/api-model';
import { DownloadService } from '@core/services/download.service';

@Injectable({
	providedIn: 'root'
})
export class ReportInstitutionQueueService {

	constructor(
		private contextService: ContextService,
		private http: HttpClient,
		private downloadService: DownloadService,
	) { }

	download(
		reportId: number,
		fileName: string,
	): Observable<void> {
		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/reports/queue/${reportId}`;

		return this.downloadService.fetchFile(url, fileName);
	}

	addToQueue(
		reportType: InstitutionReportType,
		reportToQueue: AddInstitutionReportToQueueDto,
	): Observable<InstitutionReportQueuedDto> {
		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/reports/${reportType}/queue`;
		return this.http.post<InstitutionReportQueuedDto>(url, reportToQueue);
	}


	list(reportType, query: InstitutionReportQueryDto): Observable<InstitutionReportQueuedDto[]> {
		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/reports/${reportType}/queue`;
		let params = new HttpParams();
		for (const key in query) {
			if (query.hasOwnProperty(key) && query[key] !== null && query[key] !== undefined) {
			  params = params.set(key, query[key].toString());
			}
		}
		return this.http.get<PageDto<InstitutionReportQueuedDto>>(url, { params }).pipe(
			map(page => page.content),
		);
	}



}

export const enum InstitutionReportType {
    Monthly = "Monthly",
    AppointmentNominalDetail = "AppointmentNominalDetail",
	EmergencyCareNominalDetail = "EmergencyCareNominalDetail",
	ImageNetworkProductivity = "ImageNetworkProductivity"
}
