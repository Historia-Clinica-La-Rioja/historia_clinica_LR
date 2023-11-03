import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { PatientToMergeDto } from '@api-rest/api-model';
import { ContextService } from '@core/services/context.service';
import { environment } from '@environments/environment';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class PatientMergeService {

  constructor(private http: HttpClient,
	private readonly contextService: ContextService) { }

	merge(patientToMerge:PatientToMergeDto):Observable<number>{
		const url = `${environment.apiBase}/patient-merge/institution/${this.contextService.institutionId}/merge`;
		return this.http.post<number>(url,patientToMerge);
	}

	unmerge(patientToMerge:PatientToMergeDto):Observable<number>{
		const url = `${environment.apiBase}/patient-merge/institution/${this.contextService.institutionId}/unmerge`;
		return this.http.post<number>(url,patientToMerge);
	}
}
