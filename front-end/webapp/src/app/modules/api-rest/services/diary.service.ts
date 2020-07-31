import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { ContextService } from '@core/services/context.service';
import { DiaryADto } from '@api-rest/api-model';
import { environment } from '@environments/environment';
import { Observable } from 'rxjs';

@Injectable({
	providedIn: 'root'
})
export class DiaryService {

	constructor(
		private http: HttpClient,
		private readonly contextService: ContextService
	) { }


	addDiary(agenda: DiaryADto): Observable<number> {
		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/medicalConsultations/diary`;
		return this.http.post<number>(url, agenda);
	}
}
