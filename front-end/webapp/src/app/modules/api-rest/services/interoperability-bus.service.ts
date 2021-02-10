import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Observable} from 'rxjs';
import {environment} from '@environments/environment';
import {OrganizationDto, PatientSummaryDto} from '@api-rest/api-model';

@Injectable({
	providedIn: 'root'
})
export class InteroperabilityBusService {

	constructor(
		private http: HttpClient,
	) { }

	getPatientLocation(patientId: string): Observable<OrganizationDto[]> {
		let queryParams: HttpParams = new HttpParams();
		queryParams = queryParams.append('identifier', patientId);
		const url = `${environment.apiBase}/masterfile-federacion-service/Patient/patient-location`;
		return this.http.get<OrganizationDto[]>(url, { params: queryParams });
	}

	getPatientInfo(subject: string, custodian: string): Observable<PatientSummaryDto> {
		let queryParams: HttpParams = new HttpParams();
		queryParams = queryParams.append('subject', subject);
		queryParams = queryParams.append('custodian', custodian);
		const url = `${environment.apiBase}/masterfile-federacion-service/Patient`;
		return this.http.get<PatientSummaryDto>(url, { params: queryParams });
	}

}
