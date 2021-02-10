import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { HealthcareProfessionalDto, ProfessionalDto } from '@api-rest/api-model';
import { environment } from '@environments/environment';
import { ContextService } from '@core/services/context.service';

const BASIC_URL_PREFIX = '/institution';
const BASIC_URL_SUFIX = '/healthcareprofessional';

const PROFESSIONALS = [
	{
		id: 1,
		person: {
			firstName: 'juan',
			lastName: 'perez'
		},
		licenceNumber: '123123'
	},
	{
		id: 1,
		person: {
			firstName: 'jose',
			lastName: 'martinez'
		},
		licenceNumber: '111222'
	}
];

@Injectable({
	providedIn: 'root'
})
export class HealthcareProfessionalService {

	constructor(
		private http: HttpClient,
		private contextService: ContextService,
	) {
	}

	getAll(): Observable<ProfessionalDto[]> {
		let url = `${environment.apiBase}` + BASIC_URL_PREFIX + '/' + `${this.contextService.institutionId}` + BASIC_URL_SUFIX;
		return this.http.get<ProfessionalDto[]>(url);
	}

	getAllDoctors(): Observable<HealthcareProfessionalDto[]> {
		let url = `${environment.apiBase}` + BASIC_URL_PREFIX + '/' + `${this.contextService.institutionId}` +
			BASIC_URL_SUFIX + '/doctors';
		return this.http.get<HealthcareProfessionalDto[]>(url);
	}

	getAllDoctorsBySector(sectorId: number): Observable<any[]> {
		return of(PROFESSIONALS);
	}

	searchByName(name: string): Observable<ProfessionalDto[]> {
		let url = `${environment.apiBase}` + BASIC_URL_PREFIX + '/' + `${this.contextService.institutionId}` +
			BASIC_URL_SUFIX + '/search-by-name';
		return this.http.get<ProfessionalDto[]>(url, { params: { name } });
	}

	getOne(healthcareProfessionalId: number): Observable<ProfessionalDto> {
		let url = `${environment.apiBase}` + BASIC_URL_PREFIX + '/' + `${this.contextService.institutionId}` +
			BASIC_URL_SUFIX + '/' + healthcareProfessionalId;
		return this.http.get<ProfessionalDto>(url);
	}

}
