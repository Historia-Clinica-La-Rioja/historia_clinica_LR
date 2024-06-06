import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { PageDto, ViolenceReportDto, ViolenceReportFilterOptionDto, ViolenceReportSituationDto, ViolenceReportSituationEvolutionDto } from '@api-rest/api-model';
import { ContextService } from '@core/services/context.service';
import { DownloadService } from '@core/services/download.service';
import { environment } from '@environments/environment';
import { Observable } from 'rxjs';

@Injectable({
  	providedIn: 'root'
})
export class ViolenceReportService {

	private readonly BASE_URL: string;

	constructor(
		private http: HttpClient, private readonly contextService: ContextService,
		private downloadService: DownloadService
	) {
		this.BASE_URL = `${environment.apiBase}/institution/${this.contextService.institutionId}/violence-report/patient/`;
	}

	saveNewViolenceReport(newViolenceReport: ViolenceReportDto, patientId: number) {
		const url = this.BASE_URL + patientId;
		return this.http.post<ViolenceReportDto>(url, newViolenceReport);
	}

	getLimitedPatientViolenceSituations(patientId: number, mustBeLimited: boolean): Observable<PageDto<ViolenceReportSituationDto>> {
		const url = this.BASE_URL + patientId + '/get-situations';
		let queryParams: HttpParams = new HttpParams();
		queryParams = queryParams.append('mustBeLimited', JSON.stringify(mustBeLimited));
		return this.http.get<PageDto<ViolenceReportSituationDto>>(url, {params: queryParams});
	}

	getViolenceReport(situationId: number, patientId: number): Observable<ViolenceReportDto> {
		const url = this.BASE_URL + patientId + `/situation/${situationId}`;
		return this.http.get<ViolenceReportDto>(url);
	}

	evolveViolenceReport(violenceReport: ViolenceReportDto, patientId: number, situationId: number) {
		const url = this.BASE_URL + patientId + `/situation/${situationId}/evolve`;
		return this.http.post<ViolenceReportDto>(url, violenceReport);
	}

	getEvolutions = (patientId: number, filterData: string): Observable<ViolenceReportSituationEvolutionDto[]> => {
		const url = this.BASE_URL + patientId + '/historic';
		let queryParams: HttpParams = new HttpParams();
		queryParams = queryParams.append('filterData', filterData);
		return this.http.get<ViolenceReportSituationEvolutionDto[]>(url, {params: queryParams});
	}

	getEvolutionData = (patientId: number, situationId: number, evolutionId: number): Observable<ViolenceReportDto> => {
		const url = this.BASE_URL + patientId + `/situation/${situationId}/evolution/${evolutionId}`;
		return this.http.get<ViolenceReportDto>(url);
	}

	getPatientFilters = (patientId: number): Observable<ViolenceReportFilterOptionDto> => {
		const url = this.BASE_URL + patientId + "/get-filter";
		return this.http.get<ViolenceReportFilterOptionDto>(url);
	}

	download(patientId: number, situationId: number, evolutionId: number) {
		const url = this.BASE_URL + patientId + `/situation/${situationId}/evolution/${evolutionId}/download`;
		this.downloadService.downloadPdf(
			url,
			'Documento',
		).subscribe();
	}
}
