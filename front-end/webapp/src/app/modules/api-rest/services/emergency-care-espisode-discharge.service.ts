import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { AdministrativeDischargeDto, MedicalDischargeDto } from '@api-rest/api-model';
import { ContextService } from '@core/services/context.service';
import { Observable } from 'rxjs';
import { environment } from '@environments/environment';



@Injectable({
	providedIn: 'root'
})
export class EmergencyCareEspisodeDischargeService {

	private URL_PREFIX = `${environment.apiBase}` + '/institution/' + `${this.contextService.institutionId}` + `/emergency-care/episodes/`;

	constructor(
		private http: HttpClient,
		private contextService: ContextService,
	) { }


	newMedicalDischarge(episodeId: number, medicalDischargeDto: MedicalDischargeDto): Observable<boolean> {
		const url = this.URL_PREFIX + episodeId + `/discharge`;
		return this.http.post<boolean>(url, medicalDischargeDto);
	}

	newAdministrativeDischarge(episodeId: number, administrativeDischargeDto: AdministrativeDischargeDto): Observable<boolean> {
		const url = this.URL_PREFIX + episodeId + `/discharge/administrativeDischarge`;
		return this.http.put<boolean>(url, administrativeDischargeDto);
	}

	newAdministrativeDischargeByAbsence(episodeId: number): Observable<boolean> {
		const url = this.URL_PREFIX + episodeId + `/discharge/administrativeDischarge/absence`;
		return this.http.post<boolean>(url, {});
	}
}
