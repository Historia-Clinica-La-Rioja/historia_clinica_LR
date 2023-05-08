import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { PrescriptionDto } from '@api-rest/api-model';
import { ContextService } from '@core/services/context.service';
import { environment } from '@environments/environment';
import { Observable } from 'rxjs';

@Injectable({
	providedIn: 'root'
})
export class EmergencyCareServiceRequestService {

	private URL_PREFIX = `${environment.apiBase}` + '/institutions/' + `${this.contextService.institutionId}` + `/emergency-care/episode/`;

	constructor(
		private http: HttpClient,
		private contextService: ContextService,
	) { }

	create(patientId: number, episodeId: number, prescriptionDto: PrescriptionDto): Observable<number[]> {
		const url = `${this.URL_PREFIX}${episodeId}/emergency-care-service-request/patient/${patientId}`;
		return this.http.post<number[]>(url, prescriptionDto);
	}
}
