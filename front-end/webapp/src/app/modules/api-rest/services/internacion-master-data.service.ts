import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '@environments/environment';
import { MasterDataInterface } from '@api-rest/api-model';

@Injectable({
	providedIn: 'root'
})
export class InternacionMasterDataService {

	constructor(private http: HttpClient) {
	}

	getAllergyClinical(): Observable<any[]> {
		let url = `${environment.apiBase}/internments/masterdata/allergy/clinical`;
		return this.http.get<any[]>(url);
	}

	getAllergyVerifications(): Observable<MasterDataInterface<string>[]> {
		let url = `${environment.apiBase}/internments/masterdata/allergy/verification`;
		return this.http.get<[]>(url);
	}

	getAllergyCategories(): Observable<MasterDataInterface<string>[]> {
		let url = `${environment.apiBase}/internments/masterdata/allergy/category`;
		return this.http.get<[]>(url);
	}

	getHealthClinical(): Observable<any[]> {
		let url = `${environment.apiBase}/internments/masterdata/health/clinical`;
		return this.http.get<any[]>(url);
	}

	getHealthVerification(): Observable<MasterDataInterface<string>[]> {
		let url = `${environment.apiBase}/internments/masterdata/health/verification`;
		return this.http.get<[]>(url);
	}

	getInmunizationClinical(): Observable<MasterDataInterface<string>[]> {
		let url = `${environment.apiBase}/internments/masterdata/inmunization`;
		return this.http.get<[]>(url);
	}

	getMedicationClinical(): Observable<MasterDataInterface<string>[]> {
		let url = `${environment.apiBase}/internments/masterdata/medication`;
		return this.http.get<[]>(url);
	}

	getBloodTypes(): Observable<MasterDataInterface<string>[]> {
		let url = `${environment.apiBase}/internments/masterdata/bloodtypes`;
		return this.http.get<[]>(url);
	}

	getClinicalSpecialty(): Observable<MasterDataInterface<string>[]> {
		let url = `${environment.apiBase}/internments/masterdata/clinical/specialty`;
		return this.http.get<any[]>(url);
	}

}
