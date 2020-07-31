import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { DiaryListDto } from '@api-rest/api-model';
import { environment } from '@environments/environment';
import { ContextService } from '@core/services/context.service';

@Injectable({
	providedIn: 'root'
})
export class DiariesService {

	constructor(
		private http: HttpClient,
		private contextService: ContextService,
	) { }

	getDiaries(healthcareProfessionalId: number): Observable<DiaryListDto[]> {
		let url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/medicalConsultations/diary`;
		return this.http.get<DiaryListDto[]>(url, { 
			params: { 'healthcareProfessionalId': JSON.stringify(healthcareProfessionalId) } 
		});
	}


}
