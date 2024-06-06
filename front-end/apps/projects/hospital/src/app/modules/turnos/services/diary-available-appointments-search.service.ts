import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { DiaryAvailableAppointmentsDto, EAppointmentModality } from '@api-rest/api-model';
import { ContextService } from '@core/services/context.service';
import { environment } from '@environments/environment';
import { Observable } from 'rxjs';

@Injectable({
	providedIn: 'root'
})
export class DiaryAvailableAppointmentsSearchService {

	URL_PREFIX = 'medicalConsultations/available-appointments'

	constructor(
		private http: HttpClient,
		private readonly contextService: ContextService,

	) { }

	getAvailableProtectedAppointments(filters: ProtectedAppointmentsFilter): Observable<DiaryAvailableAppointmentsDto[]> {
		let queryParams: HttpParams = new HttpParams();
		queryParams = queryParams.append('diaryProtectedAppointmentsSearch', JSON.stringify(filters));

		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/${this.URL_PREFIX}/protected`;
		return this.http.get<DiaryAvailableAppointmentsDto[]>(url, { params: queryParams });
	}

	getAvailableProtectedAppointmentsQuantity(institutionDestinationId: number, clinicalSpecialtyIds: number[], departmentId: number, careLineId: number, practiceSnomedId: number): Observable<number> {
		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/${this.URL_PREFIX}/protected-quantity`;

		let queryParams = new HttpParams()
			.append('institutionDestinationId', JSON.stringify(institutionDestinationId))
			.append('careLineId', JSON.stringify(careLineId))
			.append('departmentId', JSON.stringify(departmentId));

		if (practiceSnomedId)
			queryParams = queryParams.append('practiceSnomedId', practiceSnomedId);

		if (clinicalSpecialtyIds)
			clinicalSpecialtyIds.forEach(clinicalSpecialtyId => queryParams = queryParams.append('clinicalSpecialtyIds', JSON.stringify(clinicalSpecialtyId)));

		return this.http.get<number>(url, { params: queryParams });
	}

	getAvailableAppiuntmentsQuantityByCarelineDiaries(institutionDestinationId: number, careLineId: number, practiceSnomedId: number, clinicalSpecialtyIds: number[]): Observable<number> {
		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/${this.URL_PREFIX}/quantity/by-careline-diaries`;

		let queryParams = new HttpParams()
			.append('institutionDestinationId', JSON.stringify(institutionDestinationId))
			.append('careLineId', JSON.stringify(careLineId));

		if (practiceSnomedId)
			queryParams = queryParams.append('practiceSnomedId', JSON.stringify(practiceSnomedId));

		if (clinicalSpecialtyIds)
			clinicalSpecialtyIds.forEach(clinicalSpecialtyId => queryParams = queryParams.append('clinicalSpecialtyIds', JSON.stringify(clinicalSpecialtyId)));


		return this.http.get<number>(url, { params: queryParams });
	}

	getAvailableAppointmentsQuantity(institutionDestinationId: number, clinicalSpecialtyIds: number[], practiceSnomedId: number): Observable<number> {
		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/${this.URL_PREFIX}/quantity/by-reference-filter`;

		let queryParams = new HttpParams().append('institutionDestinationId', JSON.stringify(institutionDestinationId))

		if (clinicalSpecialtyIds)
			clinicalSpecialtyIds.forEach(clinicalSpecialtyId => queryParams = queryParams.append('clinicalSpecialtyIds', JSON.stringify(clinicalSpecialtyId)));

		if (practiceSnomedId)
			queryParams = queryParams.append('practiceSnomedId', JSON.stringify(practiceSnomedId));

		return this.http.get<number>(url, { params: queryParams });
	}

	getAvailableAppointmentsToThirdPartyBooking(filters: ThirdPartyAppointmentFilter): Observable<DiaryAvailableAppointmentsDto[]> {
		let queryParams: HttpParams = new HttpParams();
		queryParams = queryParams.append('searchFilter', JSON.stringify(filters));

		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/${this.URL_PREFIX}/third-party-booking`;
		return this.http.get<DiaryAvailableAppointmentsDto[]>(url, { params: queryParams });
	}
}

export interface ProtectedAppointmentsFilter {
	careLineId?: number,
	clinicalSpecialtyIds: number[],
	departmentId: number,
	endSearchDate: string,
	initialSearchDate: string,
	institutionId?: number,
	modality: EAppointmentModality,
	practiceId?: number,
}

export interface ThirdPartyAppointmentFilter {
	clinicalSpecialtyIds?: number[],
	departmentId: number,
	endSearchDate: string,
	initialSearchDate: string,
	institutionId?: number,
	practiceId?: number,
	healthcareProfessionalId?: number
}
