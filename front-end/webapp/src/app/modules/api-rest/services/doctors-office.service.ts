import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {ContextService} from "@core/services/context.service";
import { Observable, of } from "rxjs";
import {environment} from "@environments/environment";
import {DoctorsOfficeDto} from "@api-rest/api-model";

const doctorsOffices = [
	{
		"id": 1,
		"description": "Consultorio 1",
		"openingTime": "10:00",
		"closingTime": "21:00"
	},
	{
		"id": 2,
		"description": "Consultorio 2",
		"openingTime": "10:00",
		"closingTime": "21:00"
	}
]

const BASIC_URL_PREFIX = '/institutions';
const BASIC_URL_SUFIX = '/doctorsOffice';

@Injectable({
	providedIn: 'root'
})
export class DoctorsOfficeService {

	constructor(private http: HttpClient,
	            private contextService: ContextService) {
	}

	getAll(sectorId: number): Observable<DoctorsOfficeDto[]>{
		let url = `${environment.apiBase}` + BASIC_URL_PREFIX + '/' + `${this.contextService.institutionId}` +
			BASIC_URL_SUFIX + `/sector/${sectorId}`;
		return this.http.get<DoctorsOfficeDto[]>(url);
	}

	/**
	 * @param sectorTypeId
	 *  = 1 Ambulatorio
	 *  = 2 Internación
	 */
	getBySectorType(sectorTypeId: number): Observable<DoctorsOfficeDto[]> {
		let url = `${environment.apiBase + BASIC_URL_PREFIX}/${this.contextService.institutionId +
					BASIC_URL_SUFIX}/sectorType/${sectorTypeId}`;
		return of(doctorsOffices);
	}
}
