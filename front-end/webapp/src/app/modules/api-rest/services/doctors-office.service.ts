import { Injectable } from '@angular/core';
import { HttpClient } from "@angular/common/http";
import { ContextService } from "@core/services/context.service";
import { Observable, of } from "rxjs";
import { environment } from "@environments/environment";
import { DoctorsOfficeDto, HealthcareProfessionalDto } from "@api-rest/api-model";

const BASIC_URL_PREFIX = '/institutions';
const BASIC_URL_SUFIX = '/doctorsOffice';

const DOCTOR_OFFICES = [
	{
		id: 1,
		description: "Consultorio 1 Planta Baja",
		opening_time: "06:00:00",
		closing_time: "17:00:00"
	},
	{
		id: 2,
		description: 'Consultorio 2 Primer piso',
		opening_time: "06:00:00",
		closing_time: "17:00:00"
	}
];

@Injectable({
	providedIn: 'root'
})
export class DoctorsOfficeService {

	constructor(private http: HttpClient,
	            private contextService: ContextService) {
	}

	getAll(sectorId: number, clinicalspecialtyId: number): Observable<DoctorsOfficeDto[]>{
		let url = `${environment.apiBase}` + BASIC_URL_PREFIX + '/' + `${this.contextService.institutionId}` +
			BASIC_URL_SUFIX + `/sector/${sectorId}/clinicalspecialty/${clinicalspecialtyId}`;
		return this.http.get<DoctorsOfficeDto[]>(url);
	}
}
