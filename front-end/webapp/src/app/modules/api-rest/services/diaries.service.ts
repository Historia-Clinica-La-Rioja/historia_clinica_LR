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

	getDiaries(healthcareProfessionalId: number, specialtyId?: number): Observable<DiaryListDto[]> {
		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/medicalConsultations/diary`;
		const params = specialtyId ?
			{
				healthcareProfessionalId: JSON.stringify(healthcareProfessionalId),
				specialtyId: specialtyId ? JSON.stringify(specialtyId) : undefined
			} :
			{
				healthcareProfessionalId: JSON.stringify(healthcareProfessionalId)
			};
		return this.http.get<DiaryListDto[]>(url, {params});
	}

	delete(diaryId: number): Observable<boolean> {
		let url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/medicalConsultations/diary/${diaryId}`;
		return this.http.delete<boolean>(url);
	}

}
