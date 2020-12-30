import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { environment } from '@environments/environment';
import { Observable } from 'rxjs';
import { MedicationInfoDto, ChangeStateMedicationRequestDto, PrescriptionDto } from '@api-rest/api-model';
import { ContextService } from '@core/services/context.service';

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

	suspend(patientId: number, dayQuantity: number, medicationsIds: number[]): Observable<number> {
		let queryParams: HttpParams = new HttpParams();
		queryParams = queryParams.append('dayQuantity', JSON.stringify(dayQuantity));
		queryParams = queryParams.append('medicationsIds', JSON.stringify(medicationsIds));
		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/patient/${patientId}/suspend`;
		return this.http.put<number>(url, {params : queryParams});
	}

	finalize(patientId: number,medicationsIds: number[]): Observable<number> {
		let queryParams: HttpParams = new HttpParams();
		queryParams = queryParams.append('medicationsIds', JSON.stringify(medicationsIds));
		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/patient/${patientId}/finalize`;
		return this.http.put<number>(url, {params : queryParams});
	}

	reactivate(patientId: number, medicationsIds: number[]): Observable<number> {
		let queryParams: HttpParams = new HttpParams();
		queryParams = queryParams.append('medicationsIds', JSON.stringify(medicationsIds));
		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/patient/${patientId}/reactivate`;
		return this.http.put<number>(url, {params : queryParams});
	}

	medicationRequestList(patientId: number, statusId: string, medicationStatement: string, healthCondition: string): Observable<MedicationInfoDto[]> {
		let queryParams: HttpParams = new HttpParams();
	//	queryParams = queryParams.append('statusId', statusId);
		queryParams = queryParams.append('medicationStatement', medicationStatement);
		queryParams = queryParams.append('healthCondition', healthCondition);
		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/patient/${patientId}/medication-requests`;
		return this.http.get<MedicationInfoDto[]>(url, {params : queryParams});
	}

	download(patientId: number, medicationRequestId: number): Observable<string> {
		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/patient/${patientId}/medication-requests/${medicationRequestId}/download`;
		return this.http.get<string>(url);
	}
}
