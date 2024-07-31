import { Component, Input } from '@angular/core';
import { AssignedAppointmentDto } from '@api-rest/api-model';
import { dateDtoToDate, timeDtoToDate } from '@api-rest/mapper/date-dto.mapper';
import { DateFormatPipe } from '@presentation/pipes/date-format.pipe';

@Component({
	selector: 'app-assigned-appointment',
	templateUrl: './assigned-appointment.component.html',
	styleUrls: ['./assigned-appointment.component.scss']
})
export class AssignedAppointmentComponent {

	@Input() appointment: AssignedAppointmentDto;

	constructor(
		private readonly dateFormatPipe: DateFormatPipe
	) { }

	getViewDate(): string {
		return this.dateFormatPipe.transform(dateDtoToDate(this.appointment.date), 'fulldate');
	}

  getViewTime(): string {
		return this.dateFormatPipe.transform(timeDtoToDate(this.appointment.hour), 'time');
	}
}
