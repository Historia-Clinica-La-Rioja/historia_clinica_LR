import { Injectable } from '@angular/core';
import { Observable, of } from "rxjs";
import { environment } from "@environments/environment";
import { HttpClient } from "@angular/common/http";
import { BMPersonDto, APatientDto, BMPatientDto, PatientSearchDto, BasicPersonalDataDto, } from "@api-rest/api-model";
import { DateFormat, momentFormat } from '@core/utils/moment.utils';
import { Moment } from 'moment';

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
		let url = `${environment.apiBase}/patient/minimalsearch`;
		return this.http.get<BMPersonDto[]>(url, { params: params });
	}

	addPatient(datosPersonales: APatientDto): Observable<number> {
		let url = `${environment.apiBase}/patient`;
		return this.http.post<number>(url,datosPersonales);
	  }


	getPatient(): Observable<any> {
		return of([]);
	}

	getPatientBasicData<BasicPatientDto>(patientId: number): Observable<BasicPatientDto> {
		let url = `${environment.apiBase}/patient/${patientId}/basicdata`;
		return this.http.get<BasicPatientDto>(url);
	}

	getPatientCompleteData<CompletePatientDto>(patientId: number): Observable<CompletePatientDto> {
		let url = `${environment.apiBase}/patient/${patientId}/completedata`;
		return this.http.get<CompletePatientDto>(url);
	}

	getAllPatients(): Observable<BMPatientDto[]> {
		let url = `${environment.apiBase}/patient/basicdata`;
		return this.http.get<BMPatientDto[]>(url);
	}

	getPatientByCMD(params): Observable<PatientSearchDto[]> {
		let url = `${environment.apiBase}/patient/search`;
		return this.http.get<PatientSearchDto[]>(url, { params: { 'searchFilterStr': params } });
	}

	editPatient(datosPersonales: APatientDto, patientId: number): Observable<BMPatientDto> {
		// cambiar a edit
		let url = `${environment.apiBase}/patient/${patientId}`;
		return this.http.put<BMPatientDto>(url, datosPersonales);
	}

	searchPatientOptionalFilters(person: PersonInformationRequest): Observable<PatientSearchDto[]> {

		this.mapToRequestParams(person);
		let url = `${environment.apiBase}/patient/optionalfilter`;
		return this.http.get<PatientSearchDto[]>(url, { params: { 'searchFilterStr': JSON.stringify(person) } });
	}

	private mapToRequestParams(person: PersonInformationRequest) {
		if (person.birthDate != null)
		person.birthDate = momentFormat(person.birthDate as Moment,DateFormat.API_DATE);

		for (const property of Object.keys(person) ) {
			person[property] = person[property] === '' ? null : person[property];
		}
	}

	getAppointmentPatientData(patientId: number): Observable<BasicPersonalDataDto> {
		const url = `${environment.apiBase}/patient/${patientId}/appointment-patient-data`;
		return this.http.get<BasicPersonalDataDto>(url);
	}

}

export class PersonInformationRequest {
	firstName?: string;
	middleNames?: string;
	lastName?: string;
	otherLastNames?: string;
	genderId?: number;
	identificationNumber?: string;
	identificationTypeId?: number;
	birthDate?: string | Moment;
}
