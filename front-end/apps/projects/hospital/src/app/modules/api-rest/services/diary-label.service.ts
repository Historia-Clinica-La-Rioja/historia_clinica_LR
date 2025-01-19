import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { DiaryLabelDto } from '@api-rest/api-model';
import { ContextService } from '@core/services/context.service';
import { environment } from '@environments/environment';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class DiaryLabelService {

  	constructor(
		private http: HttpClient,
		private contextService: ContextService) { }

	getLabelsByDiary(diaryId: number): Observable<DiaryLabelDto[]> {
		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/medicalConsultations/diary/${diaryId}/labels`;
		return this.http.get<DiaryLabelDto[]>(url);
	}
}
