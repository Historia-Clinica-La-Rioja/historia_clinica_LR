import { Component, Input, OnInit } from '@angular/core';
import { AssignedAppointmentDto } from '@api-rest/api-model';
import { dateDtoToDate, timeDtoToDate } from '@api-rest/mapper/date-dto.mapper';

@Component({
	selector: 'app-assigned-appointment',
	templateUrl: './assigned-appointment.component.html',
	styleUrls: ['./assigned-appointment.component.scss']
})
export class AssignedAppointmentComponent implements OnInit{

	appointmentDate: Date;
	appointmentHour: Date;
	@Input() appointment: AssignedAppointmentDto;

	constructor() {	}

	ngOnInit(): void {
		this.appointmentDate = dateDtoToDate(this.appointment.date);
		this.appointmentHour = timeDtoToDate(this.appointment.hour);
	}

	
}
