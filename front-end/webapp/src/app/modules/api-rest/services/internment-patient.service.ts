import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { InternmentPatientDto, InternmentEpisodeProcessDto } from '@api-rest/api-model';
import { environment } from '@environments/environment';
import { ContextService } from '@core/services/context.service';

const BASIC_URL_PREFIX = '/institutions';
const BASIC_URL_SUFIX = '/internments/patients';

@Injectable({
	providedIn: 'root'
})
export class InternmentPatientService {

	constructor(
		private http: HttpClient,
		private contextService: ContextService,
	) {
	}

	getAllInternmentPatientsBasicData(): Observable<InternmentPatientDto[]> {
		const url = `${environment.apiBase}` + BASIC_URL_PREFIX + `/${this.contextService.institutionId}` + BASIC_URL_SUFIX + `/basicdata`;
		return this.http.get<InternmentPatientDto[]>(url);
	}

	internmentEpisodeIdInProcess(patientId: number): Observable<InternmentEpisodeProcessDto> {
		const url = `${environment.apiBase}` + BASIC_URL_PREFIX + `/${this.contextService.institutionId}` + BASIC_URL_SUFIX  + `/${patientId}` + `/internmentEpisodeIdInProcess/`;
		return this.http.get<InternmentEpisodeProcessDto>(url);
	}
}
