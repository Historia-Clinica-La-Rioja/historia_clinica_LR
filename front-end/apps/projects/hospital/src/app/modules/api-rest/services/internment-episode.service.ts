import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '@environments/environment';
import { HttpClient } from '@angular/common/http';
import { ContextService } from '@core/services/context.service';
import { DateDto, DateTimeDto, InternmentEpisodeBMDto, PatientDischargeDto, ProbableDischargeDateDto } from '@api-rest/api-model';

const BASIC_URL_PREFIX = '/institutions';
const BASIC_URL_SUFIX = '/internments';

@Injectable({
	providedIn: 'root'
})
export class InternmentEpisodeService {

	constructor(
		private http: HttpClient,
		private contextService: ContextService) {
	}

	setNewInternmentEpisode(intenmentEpisode): Observable<any> {
		const url = `${environment.apiBase}` + BASIC_URL_PREFIX + `/${this.contextService.institutionId}` + BASIC_URL_SUFIX;
		return this.http.post<any>(url, intenmentEpisode);
	}

	dischargeInternmentEpisode(discharge: PatientDischargeDto, internmentEpisodeId: number): Observable<PatientDischargeDto> {
		const url = `${environment.apiBase}` + BASIC_URL_PREFIX + `/${this.contextService.institutionId}` + BASIC_URL_SUFIX + `/${internmentEpisodeId}/administrativedischarge`;
		return this.http.post<PatientDischargeDto>(url, discharge);
	}

	getInternmentEpisode(internmentId: number): Observable<InternmentEpisodeBMDto> {
		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/internments/${internmentId}`;
		return this.http.get<InternmentEpisodeBMDto>(url);
	}

	getMinDischargeDate(internmentId: number): Observable<DateTimeDto> {
		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/internments/${internmentId}/minDischargeDate`;
		return this.http.get<DateTimeDto>(url);
	}

	medicalDischargeInternmentEpisode(discharge: PatientDischargeDto, internmentEpisodeId: number): Observable<PatientDischargeDto> {
		const url = `${environment.apiBase}` + BASIC_URL_PREFIX + `/${this.contextService.institutionId}` + BASIC_URL_SUFIX + `/${internmentEpisodeId}/medicaldischarge`;
		return this.http.post<PatientDischargeDto>(url, discharge);
	}

	getPatientDischarge(internmentEpisodeId: number): Observable<PatientDischargeDto> {
		const url = `${environment.apiBase}` + BASIC_URL_PREFIX + `/${this.contextService.institutionId}` + BASIC_URL_SUFIX + `/${internmentEpisodeId}/patientdischarge`;
		return this.http.get<PatientDischargeDto>(url);
	}

	getLastUpdateDateOfInternmentEpisode(internmentEpisodeId: number): Observable<DateTimeDto> {
		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/internments/${internmentEpisodeId}/lastupdatedate`;
		return this.http.get<DateTimeDto>(url);
	}

	updateProbableDischargeDate(probableDischargeDateDto: ProbableDischargeDateDto, internmentEpisodeId: number): Observable<DateTimeDto> {
		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/internments/${internmentEpisodeId}/probabledischargedate`;
		return this.http.put<DateTimeDto>(url, probableDischargeDateDto);
	}

	physicalDischargeInternmentEpisode(internmentEpisodeId: number): Observable<PatientDischargeDto> {
		const url = `${environment.apiBase}` + BASIC_URL_PREFIX + `/${this.contextService.institutionId}` + BASIC_URL_SUFIX + `/${internmentEpisodeId}/physicaldischarge`;
		return this.http.post<PatientDischargeDto>(url, {});
	}
}
