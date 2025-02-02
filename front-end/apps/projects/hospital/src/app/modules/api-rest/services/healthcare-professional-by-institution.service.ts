import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { HealthcareProfessionalDto, ProfessionalDto } from '@api-rest/api-model';
import { ContextService } from '@core/services/context.service';
import { environment } from '@environments/environment';
import { Observable } from 'rxjs';

const BASIC_URL_PREFIX = '/institution';
const BASIC_URL_SUFIX = '/healthcareprofessional';

@Injectable({
	providedIn: 'root'
})
export class HealthcareProfessionalByInstitutionService {

	constructor(
		private http: HttpClient,
		private contextService: ContextService,
	) {
	}

	getAll(): Observable<ProfessionalDto[]> {
		const url = `${environment.apiBase}${BASIC_URL_PREFIX}/${this.contextService.institutionId}${BASIC_URL_SUFIX}`;
		return this.http.get<ProfessionalDto[]>(url);
	}

	getAllByDestinationInstitution(destinationInstitutionId: number): Observable<ProfessionalDto[]> {
		const url = `${environment.apiBase}${BASIC_URL_PREFIX}/${destinationInstitutionId}${BASIC_URL_SUFIX}`;
		return this.http.get<ProfessionalDto[]>(url);
	}

	getAllDoctors(): Observable<HealthcareProfessionalDto[]> {
		const url = `${environment.apiBase}` + BASIC_URL_PREFIX + '/' + `${this.contextService.institutionId}` +
			BASIC_URL_SUFIX + '/doctors';
		return this.http.get<HealthcareProfessionalDto[]>(url);
	}

	getOne(healthcareProfessionalId: number): Observable<ProfessionalDto> {
		const url = `${environment.apiBase}` + BASIC_URL_PREFIX + '/' + `${this.contextService.institutionId}` +
			BASIC_URL_SUFIX + '/' + healthcareProfessionalId;
		return this.http.get<ProfessionalDto>(url);
	}

	getAllAssociated(): Observable<ProfessionalDto[]> {
		const url = `${environment.apiBase}` + BASIC_URL_PREFIX + '/' + `${this.contextService.institutionId}` +
			BASIC_URL_SUFIX + '/associated-healthcare-professionals';
		return this.http.get<ProfessionalDto[]>(url);
	}

	getAllAssociatedWithActiveDiaries(): Observable<ProfessionalDto[]> {
		const url = `${environment.apiBase}` + BASIC_URL_PREFIX + '/' + `${this.contextService.institutionId}` +
			BASIC_URL_SUFIX + '/associated-healthcare-professionals-with-active-diaries';
		return this.http.get<ProfessionalDto[]>(url);
	}

	getVirtualConsultationHealthcareProfessionalsByInstitutionId(): Observable<ProfessionalDto[]> {
		const url = `${environment.apiBase}` + BASIC_URL_PREFIX + '/' + `${this.contextService.institutionId}` +
			BASIC_URL_SUFIX + '/virtual-consultation';
		return this.http.get<ProfessionalDto[]>(url);
	}

	getVirtualConsultationResponsiblesByInstitutionId(): Observable<ProfessionalDto[]> {
		const url = `${environment.apiBase}` + BASIC_URL_PREFIX + '/' + `${this.contextService.institutionId}` +
			BASIC_URL_SUFIX + '/virtual-consultation-responsibles';
		return this.http.get<ProfessionalDto[]>(url);
	}
}
