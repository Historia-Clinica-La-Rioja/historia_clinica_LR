import { Injectable } from '@angular/core';
import { Observable, of } from "rxjs";

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

	constructor() {
	}

	quickGetPatient(): Observable<any>  {
		return of([]);
	}

	getPatient(): Observable<any>  {
		return of([]);
	}

	getAllPatients(): Observable<any>  {
		return of(PATIENTS_DATA);
	}
}
