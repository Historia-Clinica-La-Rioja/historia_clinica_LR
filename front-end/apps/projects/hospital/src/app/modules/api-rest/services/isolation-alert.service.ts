import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { PatientCurrentIsolationAlertDto, UpdateIsolationAlertDto } from '@api-rest/api-model';
import { ContextService } from '@core/services/context.service';
import { environment } from '@environments/environment';
import { Observable } from 'rxjs';

@Injectable({
	providedIn: 'root'
})
export class IsolationAlertService {

	private readonly BASIC_URL = `${environment.apiBase}/institution`;
	private readonly URL_SUFFIX = 'isolation-alerts';

	constructor(
		private http: HttpClient,
		private readonly contextService: ContextService,
	) { }

	getIsolationAlerts(patientId: number, filterOngoing: boolean): Observable<PatientCurrentIsolationAlertDto[]> {
		const url = `${this.BASIC_URL}/${this.contextService.institutionId}/${this.URL_SUFFIX}/patient/${patientId}`;
		return this.http.get<PatientCurrentIsolationAlertDto[]>(url, { params: { filterOngoing } });
	}

	getAlertDetail(id: number): Observable<PatientCurrentIsolationAlertDto> {
		const url = `${this.BASIC_URL}/${this.contextService.institutionId}/${this.URL_SUFFIX}/${id}`;
		return this.http.get<PatientCurrentIsolationAlertDto>(url);
	}

	cancel(id: number): Observable<PatientCurrentIsolationAlertDto> {
		const url = `${this.BASIC_URL}/${this.contextService.institutionId}/${this.URL_SUFFIX}/${id}/cancel`;
		return this.http.put<PatientCurrentIsolationAlertDto>(url, {});
	}

	update(id: number, updateIsolationAlertDto: UpdateIsolationAlertDto): Observable<PatientCurrentIsolationAlertDto> {
		const url = `${this.BASIC_URL}/${this.contextService.institutionId}/${this.URL_SUFFIX}/${id}`;
		return this.http.put<PatientCurrentIsolationAlertDto>(url, updateIsolationAlertDto);
	}
}
