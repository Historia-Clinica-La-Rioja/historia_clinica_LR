import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { AMedicalDischargeDto, VMedicalDischargeDto } from '@api-rest/api-model';
import { ContextService } from '@core/services/context.service';
import { Observable } from 'rxjs';
import { environment } from '@environments/environment';



@Injectable({
	providedIn: 'root'
})
export class EmergencyCareEpisodeMedicalDischargeService {

	private URL_PREFIX = `${environment.apiBase}` + '/institution/' + `${this.contextService.institutionId}` + `/emergency-care/episodes/`;

	constructor(
		private http: HttpClient,
		private contextService: ContextService,
	) { }


	newMedicalDischarge(episodeId: number, medicalDischargeDto: AMedicalDischargeDto): Observable<boolean> {
		const url = this.URL_PREFIX + episodeId + `/medical-discharge`;
		return this.http.post<boolean>(url, medicalDischargeDto);
	}

	getMedicalDischarge(episodeId: number): Observable<VMedicalDischargeDto> {
		const url = this.URL_PREFIX + episodeId + `/medical-discharge`;
		return this.http.get<VMedicalDischargeDto>(url);
	}
}
