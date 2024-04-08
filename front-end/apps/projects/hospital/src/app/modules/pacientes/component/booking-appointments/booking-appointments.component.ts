import { Component, EventEmitter, Input, OnChanges, Output } from '@angular/core';
import { BookedAppointmentDto } from '@api-rest/api-model';
import { AppointmentsService } from '@api-rest/services/appointments.service';
import { BOOKING_APPOINTMENTS_SUMMARY } from '@pacientes/constants/summaries';

@Component({
	selector: 'app-booking-appointments',
	templateUrl: './booking-appointments.component.html',
	styleUrls: ['./booking-appointments.component.scss']
})
export class BookingAppointmentsComponent implements OnChanges {

	@Input() identificationNumber: string;
	@Output() newAppointmentRequired = new EventEmitter();
	bookingAppointmentsSummary = BOOKING_APPOINTMENTS_SUMMARY;
	appointmentsList: BookedAppointmentDto[] = [];

	constructor(private readonly appointmentsService: AppointmentsService) { }

	ngOnChanges(): void {
		if (this.identificationNumber != null) {
			this.appointmentsService.getBookingAppointmentsList(this.identificationNumber).subscribe(
				appointments => this.appointmentsList = appointments
			);
		}
	}

	newAppointment() {
		this.newAppointmentRequired.emit();
	}

}
