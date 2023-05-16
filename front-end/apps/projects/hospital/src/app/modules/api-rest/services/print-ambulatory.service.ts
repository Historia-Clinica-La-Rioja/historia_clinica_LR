import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { CHDocumentSummaryDto, CHSearchFilterDto, Resource } from '@api-rest/api-model';
import { ContextService } from '@core/services/context.service';
import { DownloadService } from '@core/services/download.service';
import { environment } from '@environments/environment';
import { Observable} from 'rxjs';

@Injectable({
	providedIn: 'root'
})
export class PrintAmbulatoryService {

	private readonly BASE_URL: string;

	constructor(
		private readonly http: HttpClient,
		private readonly contextService: ContextService,
		private readonly downloadService: DownloadService
	) {
		this.BASE_URL = `${environment.apiBase}/institutions/${this.contextService.institutionId}/clinic-history`;
	}

	getPatientClinicHistory(patientId: number, startDate: string, endDate: string, searchFilterStr: CHSearchFilterDto): Observable<CHDocumentSummaryDto[]> {
		const url = `${this.BASE_URL}/${patientId}`;
		let queryParams: HttpParams = new HttpParams();
		queryParams = queryParams.append('startDate', startDate);
		queryParams = queryParams.append('endDate', endDate);
		queryParams = queryParams.append('searchFilterStr', JSON.stringify(searchFilterStr));
		return this.http.get<CHDocumentSummaryDto[]>(url, { params: queryParams });
	}

	downloadClinicHistory(patientDni: string, ids: number[]): Observable<any> {
		const url = `${this.BASE_URL}/download`;
		const fileName = `HCE_${patientDni}.pdf`;
		return this.downloadService.downloadPdfWithRequestParams(url, fileName, { ids: `${ids.join(',')}` });
	}
}
