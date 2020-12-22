import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {environment} from '@environments/environment';
import {OrganizationDto} from "@api-rest/api-model";

@Injectable({
	providedIn: 'root'
})
export class InteroperabilityBusService {

	constructor(
		private http: HttpClient,
	) { }

	getPatientLocation(patientId: string): Observable<OrganizationDto[]> {
		let url = `${environment.apiBase}/masterfile-federacion-service/Patient/patient-location`;
		return this.http.get<OrganizationDto[]>(url, { params: { 'id': patientId } });
	}

}
