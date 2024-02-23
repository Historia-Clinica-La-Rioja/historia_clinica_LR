import { Component, Input, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { CompleteDiaryDto, GroupAppointmentResponseDto } from '@api-rest/api-model';
import { GroupAppointmentFacadeService } from '@api-rest/services/group-appointment-facade.service';
import { getAppointmentLabelColor, getAppointmentState } from '@turnos/constants/appointment';
import { AppointmentComponent, PatientAppointmentInformation } from '@turnos/dialogs/appointment/appointment.component';
import { CalendarEvent } from 'angular-calendar';

@Component({
	selector: 'app-appointment-list-detail',
	templateUrl: './appointment-list-detail.component.html',
	styleUrls: ['./appointment-list-detail.component.scss']
})
export class AppointmentListDetailComponent implements OnInit {

	@Input() detail: GroupAppointmentResponseDto;
	@Input() diary: CompleteDiaryDto;
	@Input() hasPermissionToAssignShift: boolean;
	@Input() appointments: CalendarEvent[];
	@Input() diaryIdAndDate: string;
	appointmentStateDescription: string;
	appointmentStateColor: string; 

	constructor(private readonly dialog: MatDialog,
				private readonly groupAppointmentFacadeService: GroupAppointmentFacadeService) {}

	ngOnInit(): void {
		this.appointmentStateDescription = getAppointmentState(this.detail.appointmentStateId).description.toUpperCase();
		this.appointmentStateColor = getAppointmentLabelColor(this.detail.appointmentStateId);
	}

	openDetails = () => {
		const appointmentData: PatientAppointmentInformation = this.appointments.find(a => a.meta.appointmentId === this.detail.appointmentId).meta;
		this.dialog.open(AppointmentComponent, {
			disableClose: true,
			data: {
				appointmentData: appointmentData,
				hasPermissionToAssignShift: this.hasPermissionToAssignShift,
				agenda: this.diary
			},
		}).afterClosed()
		.subscribe((_) => this.groupAppointmentFacadeService.setAppointmentsFromDeterminatedDiaryDateTime(this.diaryIdAndDate))
	}
}
