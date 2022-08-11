import { Component, Input, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { NewAppointmentComponent } from '@turnos/dialogs/new-appointment/new-appointment.component';
import { EmptyAppointmentDto } from '@api-rest/api-model';
import { ContextService } from '@core/services/context.service';
import { Router } from '@angular/router';

@Component({
	selector: 'app-appointment-details',
	templateUrl: './appointment-details.component.html',
	styleUrls: ['./appointment-details.component.scss'],
})
export class AppointmentDetailsComponent implements OnInit {

	@Input() emptyAppointment: EmptyAppointmentDto;
	appointmentTime: Date = new Date();

	constructor(
		private readonly dialog: MatDialog,
		private readonly contextService: ContextService,
		private readonly router: Router
	) {}

	ngOnInit(): void {
		const timeData = this.emptyAppointment.hour.split(":");
		this.appointmentTime.setHours(+timeData[0]);
		this.appointmentTime.setMinutes(+timeData[1]);
		this.appointmentTime.setSeconds(+timeData[2]);
	}

	assignAppointment() {
		const dialogReference = this.dialog.open(NewAppointmentComponent, {
			width: '35%',
			data: {
				date: this.emptyAppointment.date,
				diaryId: this.emptyAppointment.diaryId,
				hour: this.emptyAppointment.hour,
				openingHoursId: this.emptyAppointment.openingHoursId,
				overturnMode: false,
				patientId: null,
			}
		});
		dialogReference.afterClosed().subscribe(
			(result: boolean) => {
				if (result) {
					this.router.navigate([`institucion/${this.contextService.institutionId}/turnos/agenda/${this.emptyAppointment.diaryId}`])
					.then(() => {
						window.location.reload();
					});
				}
			}
		);
	}

}
