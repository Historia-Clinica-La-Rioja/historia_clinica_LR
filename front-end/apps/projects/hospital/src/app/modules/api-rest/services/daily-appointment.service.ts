import { Injectable } from '@angular/core';
import { ContextService } from '@core/services/context.service';
import { DateFormat, momentFormat } from '@core/utils/moment.utils';
import { environment } from '@environments/environment';
import { Moment } from 'moment';
import { DownloadService } from '@core/services/download.service';

@Injectable({
	providedIn: 'root'
})
export class DailyAppointmentService {

	constructor(
		private contextService: ContextService,
		private readonly downloadService: DownloadService,
	) { }

	getDailyAppointmentsByDiaryIdAndDate(diaryId: number, date: Moment) {
		const apiDate: string =  momentFormat(date, DateFormat.API_DATE);
		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/medicalConsultations/daily-appointments-report/`;
		this.downloadService.fetchFile(url, undefined, { diaryId: diaryId, date: apiDate });

	}

}
