import { Injectable } from '@angular/core';
import { Observable, of } from "rxjs";

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

}
