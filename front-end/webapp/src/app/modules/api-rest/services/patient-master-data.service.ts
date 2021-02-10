import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '@environments/environment';
import { HttpClient } from '@angular/common/http';
import { IdentityVerificationStatus } from '../../pacientes/pacientes.model';

@Injectable({
	providedIn: 'root'
})
export class PatientMasterDataService {

	constructor(private http: HttpClient) {
	}

	getIdentityVerificationStatus(): Observable<IdentityVerificationStatus[]> {
		const url = `${environment.apiBase}/masterdata/patient/withoutIdentityReasons`;
		return this.http.get<IdentityVerificationStatus[]>(url);
	}
}
