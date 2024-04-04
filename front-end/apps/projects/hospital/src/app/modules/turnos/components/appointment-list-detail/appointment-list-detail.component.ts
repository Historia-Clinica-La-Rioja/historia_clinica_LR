import { Component, Input, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { CompleteDiaryDto, GroupAppointmentResponseDto } from '@api-rest/api-model';
import { dateToTimeDto, timeDtotoString } from '@api-rest/mapper/date-dto.mapper';
import { toApiFormat } from '@api-rest/mapper/date.mapper';
import { GroupAppointmentFacadeService } from '@api-rest/services/group-appointment-facade.service';
import { APPOINTMENT_STATES_ID, getAppointmentLabelColor, getAppointmentState } from '@turnos/constants/appointment';
import { AppointmentComponent, PatientAppointmentInformation } from '@turnos/dialogs/appointment/appointment.component';
import { ConfirmBookingComponent } from '@turnos/dialogs/confirm-booking/confirm-booking.component';
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
	@Input() date: Date;
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
		if (appointmentData.appointmentStateId !== APPOINTMENT_STATES_ID.BOOKED) {
			this.openAppointmentDialog(appointmentData);
		} else {
			this.openBookingDialog(appointmentData);
		}
	}

	private openAppointmentDialog = (appointmentData: PatientAppointmentInformation) => {
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

	private openBookingDialog = (appointmentData: PatientAppointmentInformation) => {
		this.dialog.open(ConfirmBookingComponent, {
			width: '30%',
			data: {
				date: toApiFormat(new Date(this.date)),
				diaryId: this.diary.id,
				hour: timeDtotoString(dateToTimeDto(new Date(this.date))),
				openingHoursId: this.diary.diaryOpeningHours,
				overturnMode: false,
				identificationTypeId: appointmentData.patient.typeId ? appointmentData.patient.typeId : 1,
				idNumber: this.detail.identificationNumber,
				appointmentId: appointmentData.appointmentId,
				phoneNumber: appointmentData.phoneNumber,
				fullName: this.detail.patientFullName,
				email: appointmentData.patient.email,
			}
		}).afterClosed()
		.subscribe((result: boolean) => {
			if (result) {
				this.groupAppointmentFacadeService.setAppointmentsFromDeterminatedDiaryDateTime(this.diaryIdAndDate);
			}
		})
	}
}
