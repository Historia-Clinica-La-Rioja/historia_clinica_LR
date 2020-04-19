import { Injectable } from '@angular/core';
import { Observable, of } from "rxjs";
import { environment } from "@environments/environment";
import { HttpClient } from "@angular/common/http";
import { BMPersonDto } from "@api-rest/api-model";

const PATIENTS_DATA = [
	{
		id: 1,
		person: {
			id: 1,
			identificationNumber: '12345678',
			first_name: 'juan',
			last_name: 'perez',
			birthDate: '01/01/1990',
			gender: {
				id: 1,
				description: 'masculino'
			}
		},
	},
	{
		id: 2,
		person: {
			id: 2,
			identificationNumber: '12345678',
			first_name: 'maria',
			last_name: 'perez',
			birthDate: '01/01/1990',
			gender: {
				id: 1,
				description: 'femenino'
			}
		},
	},
];

@Injectable({
	providedIn: 'root'
})
export class PatientService {

	constructor(private http: HttpClient) {
	}

	quickGetPatient(params): Observable<BMPersonDto[]> {
		let url = `${environment.apiBase}/person/minimalsearch`;
		return this.http.get<BMPersonDto[]>(url, { params: params });
	}

	getPatient(): Observable<any> {
		return of([]);
	}

	getPatientBasicData<BasicPatientDto>(patientId: number): Observable<BasicPatientDto> {
		let url = `${environment.apiBase}/patient/${patientId}/basicdata`;
		return this.http.get<BasicPatientDto>(url);
	}

	getAllPatients(): Observable<any> {
		return of(PATIENTS_DATA);
	}
}
