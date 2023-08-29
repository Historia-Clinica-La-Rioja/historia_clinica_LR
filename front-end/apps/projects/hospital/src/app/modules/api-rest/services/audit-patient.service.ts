import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { DuplicatePatientDto, MergedPatientSearchDto, PatientPersonalInfoDto, PatientRegistrationSearchDto, PatientType } from '@api-rest/api-model';
import { environment } from '@environments/environment';
import { Observable } from 'rxjs';
import { AuditPatientSearch } from '../../auditoria/routes/control-patient-duplicate/control-patient-duplicate.component';



@Injectable({
	providedIn: 'root'
})
export class AuditPatientService {

	constructor(private http: HttpClient) { }

	getDuplicatePatientSearchFilter(filtro: AuditPatientSearch): Observable<DuplicatePatientDto[]> {
		const url = `${environment.apiBase}/audit/duplicate-patients-by-filter`;
		return this.http.get<DuplicatePatientDto[]>(url, { params: { searchFilterStr: JSON.stringify(filtro) } });
	}

	getPatientPersonalInfo(duplicatePatientDto:DuplicatePatientDto): Observable<PatientPersonalInfoDto[]>{
		const url = `${environment.apiBase}/audit/patients-personal-info`;
		return this.http.get<PatientPersonalInfoDto[]>(url, { params: { searchPatientInfoStr: JSON.stringify(duplicatePatientDto) } });
	}

	getSearchRegistrationPatient(searchPatientInfo:any): Observable<PatientRegistrationSearchDto[]>{
		const url = `${environment.apiBase}/audit/search-registration-patients`;
		return this.http.get<PatientRegistrationSearchDto[]>(url, { params: { searchFilterStr: JSON.stringify(searchPatientInfo) } });
	}

	getTypesPatient() :Observable<PatientType[]> {
		const url = `${environment.apiBase}/audit/patient-types`;
		return this.http.get<PatientType[]>(url);
	}

	getSearchMergedPatient(searchPatientInfo:any): Observable<MergedPatientSearchDto[]>{
		const url = `${environment.apiBase}/audit/search-merged-patients`;
		return this.http.get<MergedPatientSearchDto[]>(url, { params: { searchFilterStr: JSON.stringify(searchPatientInfo) } });
	}

	getMergedPatientPersonalInfo(patientId:number): Observable<PatientPersonalInfoDto[]>{
		const url = `${environment.apiBase}/audit/patient/${patientId}/merged-patients-personal-info`;
		return this.http.get<PatientPersonalInfoDto[]>(url);
	}

	getFetchPatientsToAudit(): Observable <PatientRegistrationSearchDto[]>{
		const url = `${environment.apiBase}/audit/patients-to-audit`;
		return this.http.get<PatientRegistrationSearchDto[]>(url);
	}
}
