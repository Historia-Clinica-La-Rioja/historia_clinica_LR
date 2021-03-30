import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { ContextService } from '@core/services/context.service';
import { Observable } from 'rxjs';
import { environment } from '@environments/environment';
import { DoctorsOfficeDto } from '@api-rest/api-model';


const BASIC_URL_PREFIX = '/institutions';
const BASIC_URL_SUFIX = '/doctorsOffice';

@Injectable({
	providedIn: 'root'
})
export class DoctorsOfficeService {

	constructor(
		private http: HttpClient,
		private contextService: ContextService,
	) {
	}

	getAll(sectorId: number): Observable<DoctorsOfficeDto[]> {
		const url = `${environment.apiBase}` + BASIC_URL_PREFIX + '/' + `${this.contextService.institutionId}` +
			BASIC_URL_SUFIX + `/sector/${sectorId}`;
		return this.http.get<DoctorsOfficeDto[]>(url);
	}

	/**
	 * @param sectorTypeId
	 *  = 1 Ambulatorio
	 *  = 2 Internación
	 */
	getBySectorType(sectorTypeId: number): Observable<DoctorsOfficeDto[]> {
		const url = `${environment.apiBase + BASIC_URL_PREFIX}/${this.contextService.institutionId +
			BASIC_URL_SUFIX}/sectorType/${sectorTypeId}`;
		return this.http.get<DoctorsOfficeDto[]>(url);
	}
}
