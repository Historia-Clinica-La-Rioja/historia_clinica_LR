import { Injectable } from '@angular/core';
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";
import { InternmentPatientDto } from "@api-rest/api-model";
import { environment } from "@environments/environment";

const BASIC_URL_PREFIX = '/institutions'
const BASIC_URL_SUFIX = '/internments/patients'

@Injectable({
	providedIn: 'root'
})
export class InternmentPatientService {

	private institutionId = 10; //TODO - completar cuando dispongamos del id de institucion cargandolo dinamicamente

	constructor(private http: HttpClient) {
	}

	getAllInternmentPatientsBasicData(): Observable<InternmentPatientDto[]>{
		let url = `${environment.apiBase}` + BASIC_URL_PREFIX + `/${this.institutionId}` + BASIC_URL_SUFIX + `/basicdata`;
		return this.http.get<InternmentPatientDto[]>(url);
	}
}
