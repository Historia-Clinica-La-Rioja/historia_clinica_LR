import { DatePipe } from '@angular/common';
import { Component, Input } from '@angular/core';
import { AssignedAppointmentDto } from '@api-rest/api-model';
import { dateDtoToDate, timeDtoToDate } from '@api-rest/mapper/date-dto.mapper';
import { DatePipeFormat } from '@core/utils/date.utils';
import { DateFormatPipe } from '@presentation/pipes/date-format.pipe';

@Component({
	selector: 'app-assigned-appointment',
	templateUrl: './assigned-appointment.component.html',
	styleUrls: ['./assigned-appointment.component.scss']
})
export class AssignedAppointmentComponent {

	@Input() appointment: AssignedAppointmentDto;

	constructor(
		private readonly datePipe: DatePipe,
		private readonly dateFormatPipe: DateFormatPipe
	) { }

	getViewDate(): string {
		return this.datePipe.transform(dateDtoToDate(this.appointment.date), DatePipeFormat.FULL_DATE);
	}

	getViewTime(): string {
		return this.dateFormatPipe.transform(timeDtoToDate(this.appointment.hour), 'time');
	}

}
