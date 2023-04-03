import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { DuplicatePatientDto, PatientPersonalInfoDto } from '@api-rest/api-model';
import { ContextService } from '@core/services/context.service';
import { environment } from '@environments/environment';
import { Observable } from 'rxjs';
import { AuditPatientSearch } from '../../auditoria/routes/control-patient-duplicate/control-patient-duplicate.component';



@Injectable({
	providedIn: 'root'
})
export class AuditPatientService {

	constructor(private http: HttpClient,
		private readonly contextService: ContextService) { }

	getDuplicatePatientSearchFilter(filtro: AuditPatientSearch): Observable<DuplicatePatientDto[]> {
		const url = `${environment.apiBase}/audit/institution/${this.contextService.institutionId}/duplicate-patients-by-filter`;
		return this.http.get<DuplicatePatientDto[]>(url, { params: { searchFilterStr: JSON.stringify(filtro) } });
	}

	getPatientPersonalInfo(duplicatePatientDto:DuplicatePatientDto): Observable<PatientPersonalInfoDto[]>{
		const url = `${environment.apiBase}/audit/institution/${this.contextService.institutionId}/patients-personal-info`;
		return this.http.get<PatientPersonalInfoDto[]>(url, { params: { searchPatientInfoStr: JSON.stringify(duplicatePatientDto) } });
	}
}
