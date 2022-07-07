import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { MasterDataInterface } from '@api-rest/api-model';
import { Observable } from 'rxjs';
import { environment } from '@environments/environment';
import { ContextService } from '@core/services/context.service';

@Injectable({
	providedIn: 'root'
})
export class MedicalConsultationMasterdataService {

	constructor(
		private http: HttpClient,
		private contextService: ContextService,
	) { }

	getMedicalAttention(): Observable<MasterDataInterface<number>[]> {
		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/medicalConsultations/masterdata/medicalAttention`;
		return this.http.get<MasterDataInterface<number>[]>(url);
	}

	getAppointmentBlockMotives(): Observable<MasterDataInterface<number>[]> {
		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/medicalConsultations/masterdata/appointment-block-motive`;
		return this.http.get<MasterDataInterface<number>[]>(url);
	}

}
