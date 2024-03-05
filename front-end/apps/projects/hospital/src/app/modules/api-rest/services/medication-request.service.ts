import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { environment } from '@environments/environment';
import { Observable, of } from 'rxjs';
import {
	DocumentRequestDto,
	MedicationInfoDto,
	PrescriptionDto,
	ProfessionalLicenseNumberValidationResponseDto,
	SnomedDto
} from '@api-rest/api-model';
import { ContextService } from '@core/services/context.service';
import { DownloadService } from '@core/services/download.service';

@Injectable({
  providedIn: 'root'
})
export class MedicationRequestService {

	constructor(
		private http: HttpClient,
		private readonly contextService: ContextService,
		private readonly downloadService: DownloadService,
	  ) { }


	create(patientId: number, newMedicationRequestDto: PrescriptionDto): Observable<DocumentRequestDto[]> {
		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/patient/${patientId}/medication-requests`;
		return this.http.post<DocumentRequestDto[]>(url, newMedicationRequestDto);
	}

	suspend(patientId: number, dayQuantity: number, observations: string, medicationsIds: number[]): Observable<void> {
		if (!medicationsIds || medicationsIds.length === 0) {
			return of();
		}
		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/patient/${patientId}/medication-requests/suspend`;
		return this.http.put<void>(url,
			{
				medicationsIds,
				dayQuantity,
				observations,
			}
		);
	}

	finalize(patientId: number, medicationsIds: number[]): Observable<void> {
		if (!medicationsIds || medicationsIds.length === 0) {
			return of();
		}
		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/patient/${patientId}/medication-requests/finalize`;
		return this.http.put<void>(url, {medicationsIds});
	}

	reactivate(patientId: number, medicationsIds: number[]) {
		if (!medicationsIds || medicationsIds.length === 0) {
			return;
		}
		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/patient/${patientId}/medication-requests/reactivate`;
		return this.http.put<void>(url, {medicationsIds});
	}

	medicationRequestList(patientId: number, statusId: string, medicationStatement: string, healthCondition: string): Observable<MedicationInfoDto[]> {
		let queryParams: HttpParams = new HttpParams();

		queryParams = statusId ? queryParams.append('statusId', statusId) : queryParams;

		queryParams = medicationStatement ? queryParams.append('medicationStatement', medicationStatement) : queryParams;

		queryParams = healthCondition ? queryParams.append('healthCondition', healthCondition) : queryParams;

		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/patient/${patientId}/medication-requests/medicationRequestList`;
		return this.http.get<MedicationInfoDto[]>(url, {params : queryParams});
	}

	medicationRequestListByRoles(patientId: number, statusId: string, medicationStatement: string, healthCondition: string): Observable<MedicationInfoDto[]> {
		let queryParams: HttpParams = new HttpParams();

		queryParams = statusId ? queryParams.append('statusId', statusId) : queryParams;

		queryParams = medicationStatement ? queryParams.append('medicationStatement', medicationStatement) : queryParams;

		queryParams = healthCondition ? queryParams.append('healthCondition', healthCondition) : queryParams;

		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/patient/${patientId}/medication-requests/medicationRequestListByUser`;

		return this.http.get<MedicationInfoDto[]>(url, {params : queryParams});
	}

	download(patientId: number, medicationRequestId: number) {
		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/patient/${patientId}/medication-requests/${medicationRequestId}/download`;
		this.downloadService.fetchFile(url, 'Receta ' + medicationRequestId);
	}

	validateProfessional(patientId: number): Observable<ProfessionalLicenseNumberValidationResponseDto> {
		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/patient/${patientId}/medication-requests/validate`;
		return this.http.get<ProfessionalLicenseNumberValidationResponseDto>(url);
	}

	sendEmail(patientId: number, patientEmail: string, documentId: number): Observable<any> {
		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/patient/${patientId}/medication-requests/documentId/${documentId}/notify`;
		let queryParams: HttpParams = new HttpParams();
		queryParams = queryParams.append('patientEmail', patientEmail);
		return this.http.get<any>(url, {params: queryParams});
	}

	mostFrequentPharmacosPreinscription(patientId: number): Observable<SnomedDto[]> {
		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/patient/${patientId}/medication-requests/most-frequent-pharmacos`;
		return this.http.get<SnomedDto[]>(url);
	}

	cancelPrescriptionLineState(medicationStatementId: number, patientId: number): Observable<void> {
		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/patient/${patientId}/medication-requests/cancel-prescription-line-state`;
		let queryParams: HttpParams = new HttpParams();
		queryParams = queryParams.append('medicationStatementId', medicationStatementId);
		return this.http.put<void>(url, queryParams);
	}
}
