import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { ContextService } from '@core/services/context.service';
import { environment } from '@environments/environment';
import { Observable } from 'rxjs';
import { DownloadService } from '@core/services/download.service';
import { UIComponentDto } from '@extensions/extensions-model';
import { ReportFilters } from "../../reportes/routes/home/home.component";
import { ImageNetworkProductivityFilterDto } from '@api-rest/api-model';
import { toApiFormat } from '@api-rest/mapper/date.mapper';

@Injectable({
	providedIn: 'root'
})
export class ReportsService {

	constructor(
		private contextService: ContextService,
		private downloadService: DownloadService,
		private http: HttpClient
	) { }

	private getReport(params: ReportFilters, fileName: string, url: any): Observable<any> {
		let requestParams: HttpParams = new HttpParams();
		requestParams = requestParams.append('fromDate', toApiFormat(params.startDate));
		requestParams = requestParams.append('toDate', toApiFormat(params.endDate));
		if (params.specialtyId) {
			requestParams = requestParams.append('clinicalSpecialtyId', params.specialtyId);
		}
		if (params.professionalId) {
			requestParams = requestParams.append('doctorId', params.professionalId);
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


	getDiabetesReport(): Observable<UIComponentDto> {
		const url = `${environment.apiBase}/reports/institution/${this.contextService.institutionId}/diabetes`;
		return this.http.get<UIComponentDto>(url);
	}

	getHypertensionReport(): Observable<UIComponentDto> {
		const url = `${environment.apiBase}/reports/institution/${this.contextService.institutionId}/hypertension`;
		return this.http.get<UIComponentDto>(url);
	}

	getEpidemiologicalWeekReport(): Observable<UIComponentDto> {
		const url = `${environment.apiBase}/reports/institution/${this.contextService.institutionId}/epidemiological_week`;
		return this.http.get<UIComponentDto>(url);
	}

	getNominalAppointmentsDetail(params: ReportFilters, fileName: string): Observable<any> {
		const url = `${environment.apiBase}/reports/institution/${this.contextService.institutionId}/nominal-appointment-detail`;
		return this.getReport(params, fileName, url);
	}

	getImageNetworkProductivityReport(searchCriteria: ImageNetworkProductivityFilterDto, fileName: string){
		const url = `${environment.apiBase}/institution/${this.contextService.institutionId}/report/image-network-productivity`;
		const params = { filter: JSON.stringify(searchCriteria) };
		return this.downloadService.downloadXlsWithRequestParams(url, fileName, params);
	}

}
