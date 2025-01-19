import { Injectable } from '@angular/core';
import { HttpParams } from '@angular/common/http';
import { ContextService } from '@core/services/context.service';
import { environment } from '@environments/environment';
import { Observable } from 'rxjs';
import { DownloadService } from '@core/services/download.service';
import { ReportFilters } from "../../reportes/routes/home/home.component";
import { toApiFormat } from '@api-rest/mapper/date.mapper';

@Injectable({
	providedIn: 'root'
})
export class ReportsService {

	constructor(
		private contextService: ContextService,
		private downloadService: DownloadService,
	) { }

	private getReport(params: ReportFilters, fileName: string, url: any): Observable<any> {
		let requestParams: HttpParams = new HttpParams();
		requestParams = requestParams.append('fromDate', toApiFormat(params.fromDate));
		requestParams = requestParams.append('toDate', toApiFormat(params.toDate));
		if (params.clinicalSpecialtyId) {
			requestParams = requestParams.append('clinicalSpecialtyId', params.clinicalSpecialtyId);
		}
		if (params.doctorId) {
			requestParams = requestParams.append('doctorId', params.doctorId);
		}
		if (params.hierarchicalUnitTypeId) {
			requestParams = requestParams.append('hierarchicalUnitTypeId', params.hierarchicalUnitTypeId);
		}
		if (params.hierarchicalUnitId) {
			requestParams = requestParams.append('hierarchicalUnitId', params.hierarchicalUnitId);
		}
		if (params.includeHierarchicalUnitDescendants) {
			requestParams = requestParams.append('includeHierarchicalUnitDescendants', params.includeHierarchicalUnitDescendants);
		}
		if (params.appointmentStateId) {
			requestParams = requestParams.append('appointmentStateId', params.appointmentStateId);
		}
		return this.downloadService.downloadXlsWithRequestParams(url, fileName, requestParams);
	}

	getMonthlyReport(params: ReportFilters, fileName: string): Observable<any> {
		const url = `${environment.apiBase}/reports/${this.contextService.institutionId}/monthly`;
		return this.getReport(params, fileName, url);
	}

	getOutpatientSummaryReport(params: ReportFilters, fileName: string): Observable<any> {
		const url = `${environment.apiBase}/reports/${this.contextService.institutionId}/summary`;
		return this.getReport(params, fileName, url);
	}

	getMonthlySummaryOfExternalClinicAppointmentsReport(searchFilter: ReportFilters, fileName: string): Observable<any> {
		const url = `${environment.apiBase}/reports/institution/${this.contextService.institutionId}/appointment-consultation-summary`;
		const filters = new HttpParams().append('searchFilter', JSON.stringify(searchFilter));
		return this.downloadService.downloadXlsWithRequestParams(url, fileName, filters);
	}

	getNominalAppointmentsDetail(params: ReportFilters, fileName: string): Observable<any> {
		const url = `${environment.apiBase}/reports/institution/${this.contextService.institutionId}/nominal-appointment-detail`;
		return this.getReport(params, fileName, url);
	}

	getNominalEmergencyCareEpisodeDetail(params: ReportFilters, fileName: string): Observable<any> {
		const url = `${environment.apiBase}/reports/institution/${this.contextService.institutionId}/nominal-emergency-care-episode-detail`;
		return this.getReport(params, fileName, url);
		}

	getImageNetworkProductivityReport(params: ReportFilters, fileName: string): Observable<any> {
		const url = `${environment.apiBase}/institution/${this.contextService.institutionId}/report/image-network-productivity`;
		return this.getReport(params, fileName, url);
	}

}
