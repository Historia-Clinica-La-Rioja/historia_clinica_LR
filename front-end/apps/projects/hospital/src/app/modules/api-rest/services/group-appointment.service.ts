import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { GroupAppointmentResponseDto } from '@api-rest/api-model';
import { ContextService } from '@core/services/context.service';
import { environment } from '@environments/environment';
import { Observable } from 'rxjs';

@Injectable({
  	providedIn: 'root'
})
export class GroupAppointmentService {

  	constructor(private http: HttpClient,
				private contextService: ContextService) { }

	getAppointmentsFromDeterminatedDiaryDateTime(data: string): Observable<GroupAppointmentResponseDto[]> {
		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/medicalConsultations/appointments/group-appointments/get-appointments-from-determinated-diary-date-time`;
		let queryParams: HttpParams = new HttpParams();
		queryParams = queryParams.append('data', data);
		return this.http.get<GroupAppointmentResponseDto[]>(url, {params: queryParams});
	}
}
