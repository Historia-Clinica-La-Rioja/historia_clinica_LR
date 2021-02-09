import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {environment} from '@environments/environment';
import {ContextService} from '@core/services/context.service';
import {OutpatientImmunizationDto} from '@api-rest/api-model';

@Injectable({
	providedIn: 'root'
})
export class HceImmunizationService {

	constructor(private readonly http: HttpClient, private readonly contextService: ContextService) {
	}

	gettingVaccine(immunization: OutpatientImmunizationDto[], patientId: number): Observable<boolean> {
		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/patient/${patientId}/outpatient/consultations/gettingVaccine`;
		return this.http.post<boolean>(url, immunization);
	}

	updateImmunization(immunization: OutpatientImmunizationDto, patientId: number): Observable<boolean> {
		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/patient/${patientId}/outpatient/consultations/updateImmunization`;
		return this.http.post<boolean>(url, immunization);
	}

}
