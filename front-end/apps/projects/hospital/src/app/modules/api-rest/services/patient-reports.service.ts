import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable, of} from "rxjs";
import {ConsultationsDto} from "@api-rest/api-model";

@Injectable({
  providedIn: 'root'
})
export class PatientReportsService {

  constructor(private http: HttpClient) { }

	getConsultations(patientId: number): Observable<ConsultationsDto[]> {
		return of([{
			id: 1,
			date: '06/12/2021',
			especiality: 'Odontológa',
			professional: 'Maria Peréz'
		},
			{id: 2, date: '06/12/2021', especiality: 'Médico clínico', professional: 'Juan Rodríguez'},
			{id: 2, date: '06/12/2021', especiality: 'Médico clínico', professional: 'Juan Rodríguez'},
			{id: 2, date: '06/12/2021', especiality: 'Médico clínico', professional: 'Juan Rodríguez'},
			{id: 2, date: '06/12/2021', especiality: 'Médico clínico', professional: 'Juan Rodríguez'},
			{id: 2, date: '06/12/2021', especiality: 'Médico clínico', professional: 'Juan Rodríguez'},
			{id: 2, date: '06/12/2021', especiality: 'Médico clínico', professional: 'Juan Rodríguez'},
			{id: 2, date: '06/12/2021', especiality: 'Médico clínico', professional: 'Juan Rodríguez'},
			{id: 2, date: '06/12/2021', especiality: 'Médico clínico', professional: 'Juan Rodríguez'},
			{id: 2, date: '06/12/2021', especiality: 'Médico clínico', professional: 'Juan Rodríguez'},
			{id: 2, date: '06/12/2021', especiality: 'Médico clínico', professional: 'Juan Rodríguez'},
			{id: 2, date: '06/12/2021', especiality: 'Médico clínico', professional: 'Juan Rodríguez'},
			{id: 2, date: '06/12/2021', especiality: 'Médico clínico', professional: 'Juan Rodríguez'},
			{id: 2, date: '06/12/2021', especiality: 'Médico clínico', professional: 'Juan Rodríguez'}]);
	}
}
