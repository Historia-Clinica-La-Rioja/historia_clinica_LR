import { Component, Inject, OnDestroy, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialog } from '@angular/material/dialog';
import { CompleteDiaryDto, GroupAppointmentResponseDto } from '@api-rest/api-model';
import { dateToDateTimeDto } from '@api-rest/mapper/date-dto.mapper';
import { GroupAppointmentFacadeService } from '@api-rest/services/group-appointment-facade.service';
import { MAX_APPOINTMENT_PER_HOUR } from '@turnos/utils/appointment.utils';
import { CalendarEvent } from 'angular-calendar';
import { Subscription } from 'rxjs';

@Component({
	selector: 'app-appointment-list',
	templateUrl: './appointment-list.component.html',
	styleUrls: ['./appointment-list.component.scss']
})
export class AppointmentListComponent implements OnInit, OnDestroy {

	appointments: GroupAppointmentResponseDto[] = [];
	diaryIdAndDate: string;
	subscription: Subscription;

	constructor(private readonly groupAppointmentFacadeService: GroupAppointmentFacadeService,
				private readonly dialogRef: MatDialog,
				@Inject(MAT_DIALOG_DATA) public data: { date: Date, hasPermissionToAssignShift: boolean, agenda: CompleteDiaryDto, appointments: CalendarEvent[]}) { }

	ngOnInit(): void {
		this.diaryIdAndDate = JSON.stringify({
			diaryId: this.data.agenda.id,
			date: dateToDateTimeDto(this.data.date)
		});
		this.groupAppointmentFacadeService.setAppointmentsFromDeterminatedDiaryDateTime(this.diaryIdAndDate);
		this.subscription = this.groupAppointmentFacadeService.appointments$.subscribe((result: GroupAppointmentResponseDto[]) => {
			this.appointments = result;
			if (this.appointments.length < MAX_APPOINTMENT_PER_HOUR)
				this.dialogRef.closeAll();
		})
	}

	ngOnDestroy(): void {
		this.subscription.unsubscribe();
	}
}
