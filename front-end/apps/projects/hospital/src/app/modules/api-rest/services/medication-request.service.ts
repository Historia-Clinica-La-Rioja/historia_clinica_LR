import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { environment } from '@environments/environment';
import { Observable } from 'rxjs';
import { MedicationInfoDto, PrescriptionDto } from '@api-rest/api-model';
import { ContextService } from '@core/services/context.service';
import { of } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class MedicationRequestService {

	constructor(
		private http: HttpClient,
		private readonly contextService: ContextService
	  ) { }


	create(patientId: number, newMedicationRequestDto: PrescriptionDto): Observable<number> {
		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/patient/${patientId}/medication-requests`;
		return this.http.post<number>(url, newMedicationRequestDto);
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

		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/patient/${patientId}/medication-requests`;
		return this.http.get<MedicationInfoDto[]>(url, {params : queryParams});
	}

	download(patientId: number, medicationRequestId: number): Observable<any> {
		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/patient/${patientId}/medication-requests/${medicationRequestId}/download`;
		return this.http.get(
			url,
			{ responseType: 'blob' }
		);
	}
}
