import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { AssignedAppointmentDto } from '@api-rest/api-model';
import { AppointmentsService } from '@api-rest/services/appointments.service';
import { ASSIGNED_APPOINTMENTS_SUMMARY } from '@pacientes/constants/summaries';

@Component({
	selector: 'app-assigned-appointments',
	templateUrl: './assigned-appointments.component.html',
	styleUrls: ['./assigned-appointments.component.scss']
})
export class AssignedAppointmentsComponent implements OnInit {

	@Input() patientId: number;
	@Output() newAppointmentRequired = new EventEmitter();
	assignedAppointmentsSummary = ASSIGNED_APPOINTMENTS_SUMMARY;
	appointmentsList: AssignedAppointmentDto[] = [];

	constructor(private readonly appointmentsService: AppointmentsService) { }

	ngOnInit(): void {
		this.appointmentsService.getAssignedAppointmentsList(this.patientId).subscribe(
			appointments => this.appointmentsList = appointments
		);
	}

	newAppointment() {
		this.newAppointmentRequired.emit();
	}

}
