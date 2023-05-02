import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { PatientToMergeDto } from '@api-rest/api-model';
import { ContextService } from '@core/services/context.service';
import { environment } from '@environments/environment';
import { Observable } from 'rxjs';

@Injectable({
	providedIn: 'root'
})
export class PatientToMergeService {

	private readonly url = `${environment.apiBase}/patient-merge/institution/${this.contextService.institutionId}`;
	private readonly post = `${this.url}/merge`

	constructor(
		private http: HttpClient,
		private readonly contextService: ContextService
	) { }

	merge(patientToMergeDto: PatientToMergeDto): Observable<number> {
		return this.http.post<number>(this.post, patientToMergeDto)
	}
}
