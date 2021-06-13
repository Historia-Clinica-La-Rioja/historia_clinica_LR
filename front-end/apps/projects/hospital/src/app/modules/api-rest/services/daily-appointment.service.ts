import { Injectable } from '@angular/core';
import { ContextService } from '@core/services/context.service';
import { DownloadService } from '@core/services/download.service';
import { DateFormat, momentFormat } from '@core/utils/moment.utils';
import { environment } from '@environments/environment';
import { Moment } from 'moment';
import { Observable } from 'rxjs';

@Injectable({
	providedIn: 'root'
})
export class DailyAppointmentService {

	constructor(
		private downloadService: DownloadService,
		private contextService: ContextService, ) { }


	getDailyAppointmentsByDiaryIdAndDate(diaryId: number, date: Moment): Observable<any> {
		const apiDate: string =  momentFormat(date, DateFormat.API_DATE);
		const fileDate: string =  momentFormat(date, DateFormat.FILE_DATE);
		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/medicalConsultations/daily-appointments-report/`;
		const fileName = `${`Turnos_`}${fileDate}.pdf`;
		return this.downloadService.downloadPdfWithRequestParams(url, fileName, { diaryId, date: apiDate });
	}
}
