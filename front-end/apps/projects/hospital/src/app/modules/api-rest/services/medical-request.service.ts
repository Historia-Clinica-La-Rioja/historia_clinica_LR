import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {ContextService} from '@core/services/context.service';
import {environment} from '@environments/environment';
import {Observable} from 'rxjs';
import {MedicalRequestDto, NewMedicalRequestDto} from '@api-rest/api-model';

@Injectable({
	providedIn: 'root'
})
export class MedicalRequestService {

	constructor(
		private readonly http: HttpClient,
		private readonly contextService: ContextService
	) {
	}

	getList(patientId: number): Observable<MedicalRequestDto[]> {
		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/patient/${patientId}/medical-requests`;
		return this.http.get<MedicalRequestDto[]>(url);
	}

	create(patientId: number, newMedicalRequest: NewMedicalRequestDto): Observable<number>{
		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/patient/${patientId}/medical-requests`;
		return this.http.post<number>(url, newMedicalRequest);
	}

	finalize(patientId: number, medicalRequestId: number): Observable<string>{
		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/patient/${patientId}/medical-requests/${medicalRequestId}`;
		return this.http.put<string>(url,{});
	}
}
