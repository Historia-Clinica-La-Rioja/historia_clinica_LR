import { Injectable } from '@angular/core';
import { Observable } from "rxjs";
import { InternmentEpisodeDto, InternmentPatientDto } from "@api-rest/api-model";
import { environment } from "@environments/environment";
import { HttpClient } from "@angular/common/http";
import { ContextService } from "@core/services/context.service";

const BASIC_URL_PREFIX = '/institutions'
const BASIC_URL_SUFIX = '/internments'

@Injectable({
	providedIn: 'root'
})
export class InternmentEpisodeService {

	constructor(private http: HttpClient,
				private contextService: ContextService) {
	}

	setNewInternmentEpisode(intenmentEpisode): Observable<any>{
		let url = `${environment.apiBase}` + BASIC_URL_PREFIX + `/${this.contextService.institutionId}` + BASIC_URL_SUFIX;
		return this.http.post<any>(url, intenmentEpisode);
	}

	dischargeInternmentEpisode<PatientDischargeDto>(discharge: PatientDischargeDto,internmentEpisodeId: number): Observable<PatientDischargeDto> {
		let url = `${environment.apiBase}` + BASIC_URL_PREFIX + `/${this.contextService.institutionId}` + BASIC_URL_SUFIX + `/${internmentEpisodeId}/discharge`;
		return this.http.post<PatientDischargeDto>(url, discharge);
	}

}
