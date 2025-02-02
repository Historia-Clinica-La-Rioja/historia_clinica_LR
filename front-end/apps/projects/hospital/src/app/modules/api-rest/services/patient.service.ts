import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import { environment } from '@environments/environment';
import { HttpClient } from '@angular/common/http';
import {
	APatientDto,
	BMPatientDto,
	PatientSearchDto,
	ReducedPatientDto,
	PersonPhotoDto,
	PatientPhotoDto, PatientGenderAgeDto, PageDto
} from '@api-rest/api-model';
import { ContextService } from "@core/services/context.service";
import { toApiFormat } from '@api-rest/mapper/date.mapper';
import { dateISOParseDate } from '@core/utils/moment.utils';

@Injectable({
	providedIn: 'root'
})
export class PatientService {

	constructor(private http: HttpClient,
		private readonly contextService: ContextService) {
	}

	getPatientMinimal(params): Observable<number[]> {
		const url = `${environment.apiBase}/patient/minimalsearch`;
		return this.http.get<number[]>(url, { params });
	}

	addPatient(datosPersonales: APatientDto): Observable<number> {
		const url = `${environment.apiBase}/patient/institution/${this.contextService.institutionId}`;
		return this.http.post<number>(url, datosPersonales);
	}


	getPatient(): Observable<any> {
		return of([]);
	}

	getPatientBasicData<BasicPatientDto>(patientId: number): Observable<BasicPatientDto> {
		const url = `${environment.apiBase}/patient/${patientId}/basicdata`;
		return this.http.get<BasicPatientDto>(url);
	}

	getPatientCompleteData<CompletePatientDto>(patientId: number): Observable<CompletePatientDto> {
		const url = `${environment.apiBase}/patient/${patientId}/completedata`;
		return this.http.get<CompletePatientDto>(url);
	}

	getAllPatients(): Observable<BMPatientDto[]> {
		const url = `${environment.apiBase}/patient/basicdata`;
		return this.http.get<BMPatientDto[]>(url);
	}

	getPatientByCMD(params): Observable<PatientSearchDto[]> {
		const url = `${environment.apiBase}/patient/search`;
		return this.http.get<PatientSearchDto[]>(url, { params: { searchFilterStr: params } });
	}

	editPatient(datosPersonales: APatientDto, patientId: number): Observable<BMPatientDto> {
		const url = `${environment.apiBase}/patient/${patientId}/institution/${this.contextService.institutionId}`;
		return this.http.put<BMPatientDto>(url, datosPersonales);
	}

	searchPatientOptionalFilters(person: PersonInformationRequest, pageable: { pageNumber, pageSize }): Observable<PageDto<PatientSearchDto>> {
		this.mapToRequestParams(person);
		const url = `${environment.apiBase}/patient/optionalfilter`;
		return this.http.get<PageDto<PatientSearchDto>>(url, {
			params: {
				searchFilterStr: JSON.stringify(person),
				pageSize: pageable.pageSize,
				pageNumber: pageable.pageNumber
			}
		});
	}

	private mapToRequestParams(person: PersonInformationRequest) {
		if (person.birthDate != null) {
			person.birthDate = toApiFormat(dateISOParseDate(person.birthDate));
		}

		for (const property of Object.keys(person)) {
			person[property] = person[property] === '' ? null : person[property];
		}
	}

	getBasicPersonalData(patientId: number): Observable<ReducedPatientDto> {
		const url = `${environment.apiBase}/patient/${patientId}/appointment-patient-data`;
		return this.http.get<ReducedPatientDto>(url);
	}

	getPatientPhoto(patientId: number): Observable<PersonPhotoDto> {
		const url = `${environment.apiBase}/patient/${patientId}/photo`;
		return this.http.get<PersonPhotoDto>(url);
	}

	getPatientsPhotos(patientsIds: number[]): Observable<PatientPhotoDto[]> {
		const url = `${environment.apiBase}/patient/photos`;
		return this.http.get<any[]>(url, {
			params: { patientsIds: `${patientsIds.join(',')}` }
		});
	}

	addPatientPhoto(patientId: number, personPhoto: PersonPhotoDto): Observable<boolean> {
		const url = `${environment.apiBase}/patient/${patientId}/photo`;
		return this.http.post<boolean>(url, personPhoto);
	}

	getPatientGender(patientId: number): Observable<PatientGenderAgeDto> {
		const url = `${environment.apiBase}/patient/${patientId}/gender-age`;
		return this.http.get<PatientGenderAgeDto>(url);
	}
}

export class PersonInformationRequest {
	firstName?: string;
	middleNames?: string;
	lastName?: string;
	otherLastNames?: string;
	genderId?: number;
	identificationNumber?: string;
	identificationTypeId?: number;
	birthDate?: string;
}
