import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { ContextService } from '@core/services/context.service';
import { DiaryADto, CompleteDiaryDto, DiaryDto, BlockDto, AppointmentSearchDto, EmptyAppointmentDto } from '@api-rest/api-model';
import { environment } from '@environments/environment';
import { Observable } from 'rxjs';

@Injectable({
	providedIn: 'root'
})
export class DiaryService {

	constructor(
		private readonly http: HttpClient,
		private readonly contextService: ContextService
	) { }


	addDiary(agenda: DiaryADto): Observable<number> {
		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/medicalConsultations/diary`;
		return this.http.post<number>(url, agenda);
	}

	updateDiary(agenda: DiaryDto): Observable<number> {
		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/medicalConsultations/diary/${agenda.id}`;
		return this.http.put<number>(url, agenda);
	}

	get(diaryId: number): Observable<CompleteDiaryDto> {
		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/medicalConsultations/diary/${diaryId}`;
		return this.http.get<CompleteDiaryDto>(url);
	}

	delete(diaryId: number): Observable<boolean> {
		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/medicalConsultations/diary/${diaryId}`;
		return this.http.delete<boolean>(url);
	}

	blockAgendaRangeDateTime(diaryId: number, blockDto: BlockDto): Observable<boolean> {
		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/medicalConsultations/diary/${diaryId}/block`;
		return this.http.post<boolean>(url, blockDto);
	}

	unblock(diaryId: number, blockDto: BlockDto): Observable<boolean> {
		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/medicalConsultations/diary/${diaryId}/unblock`;
		return this.http.post<boolean>(url, blockDto);
	}

	getActiveDiariesAliases(): Observable<string[]> {
		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/medicalConsultations/diary/active-diaries-alias`;
		return this.http.get<string[]>(url);
	}

	generateEmptyAppointments(searchCriteria: AppointmentSearchDto): Observable<EmptyAppointmentDto[]> {
		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/medicalConsultations/diary/generate-empty-appointments`;
		return this.http.post<any>(url, searchCriteria);
	}

}
