import { Injectable } from '@angular/core';
import { ContextService } from '@core/services/context.service';
import { DateFormat, momentFormat } from '@core/utils/moment.utils';
import { environment } from '@environments/environment';
import { Moment } from 'moment';
import { ViewPdfService } from '@presentation/dialogs/view-pdf/view-pdf.service';

@Injectable({
	providedIn: 'root'
})
export class DailyAppointmentService {

	constructor(
		private contextService: ContextService,
		private viewPdfService: ViewPdfService,
	) { }


	getDailyAppointmentsByDiaryIdAndDate(diaryId: number, date: Moment) {
		const apiDate: string =  momentFormat(date, DateFormat.API_DATE);
		const fileDate: string =  momentFormat(date, DateFormat.FILE_DATE);
		const url = `${environment.apiBase}/institutions/${this.contextService.institutionId}/medicalConsultations/daily-appointments-report/`;
		const fileName = `${`Turnos_`}${fileDate}`;

		this.viewPdfService.showDialog(url, fileName, { diaryId: diaryId.toString(), date: apiDate });

	}
}
