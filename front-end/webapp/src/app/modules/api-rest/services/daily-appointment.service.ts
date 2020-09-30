import { Injectable } from '@angular/core';
import { ContextService } from '@core/services/context.service';
import { DownloadService } from '@core/services/download.service';
import { environment } from '@environments/environment';
import { Observable } from 'rxjs';

@Injectable({
	providedIn: 'root'
})
export class DailyAppointmentService {

	constructor(
		private downloadService: DownloadService,
		private contextService: ContextService,) { }


	getDailyAppointmentsByDiaryIdAndDate(diaryId: number, date: string): Observable<any> {
		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/medicalConsultations/daily-appointments-report/`;
		const fileName = `${`Turnos_`}${date}.pdf`;
		return this.downloadService.downloadPdfWithRequestParams(url, fileName, { diaryId, date });
	}
}
