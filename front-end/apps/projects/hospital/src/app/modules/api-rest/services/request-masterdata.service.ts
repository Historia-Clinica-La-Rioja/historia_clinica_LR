import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '@environments/environment';
import { Observable } from 'rxjs';
import { EProfessionType, GenericMasterDataDto, ServiceRequestCategoryDto } from '@api-rest/api-model';

@Injectable({
	providedIn: 'root'
})
export class RequestMasterDataService {

	constructor(
		private http: HttpClient
	) { }

	categories(): Observable<ServiceRequestCategoryDto[]> {
		const url = `${environment.apiBase}/requests/masterdata/categories`;
		return this.http.get<ServiceRequestCategoryDto[]>(url);
	}

	medicationStatus(): Observable<any[]> {
		const url = `${environment.apiBase}/requests/masterdata/medication-status`;
		return this.http.get<any[]>(url);
	}

	diagnosticReportStatus(): Observable<any[]> {
		const url = `${environment.apiBase}/requests/masterdata/diagnostic-report-status`;
		return this.http.get<any[]>(url);
	}

	getSurgicalReportProfessionTypes(): Observable<GenericMasterDataDto<EProfessionType>[]>{
		const url = `${environment.apiBase}/requests/masterdata//get-surgical-report-profession-types`;
		return this.http.get<GenericMasterDataDto<EProfessionType>[]>(url);
	}
}
