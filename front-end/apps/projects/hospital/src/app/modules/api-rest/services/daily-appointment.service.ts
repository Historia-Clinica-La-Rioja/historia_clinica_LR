import { Injectable } from '@angular/core';
import { ContextService } from '@core/services/context.service';
import { environment } from '@environments/environment';
import { DownloadService } from '@core/services/download.service';
import { toApiFormat } from '@api-rest/mapper/date.mapper';

@Injectable({
	providedIn: 'root'
})
export class DailyAppointmentService {

	constructor(
		private contextService: ContextService,
		private readonly downloadService: DownloadService,
	) { }

	getDailyAppointmentsByDiaryIdAndDate(diaryId: number, date: Date) {
		const apiDate: string =  toApiFormat(date);
		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/medicalConsultations/daily-appointments-report/`;
		this.downloadService.fetchFile(url, undefined, { diaryId: diaryId, date: apiDate });

	}

}
