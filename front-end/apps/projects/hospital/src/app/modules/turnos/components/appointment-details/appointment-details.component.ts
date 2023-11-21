import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { NewAppointmentComponent } from '@turnos/dialogs/new-appointment/new-appointment.component';
import { EAppointmentModality, EmptyAppointmentDto, ReferenceSummaryDto } from '@api-rest/api-model';
import { DatePipeFormat } from '@core/utils/date.utils';
import { DatePipe } from '@angular/common';
import { ConfirmPrintAppointmentComponent } from '@turnos/dialogs/confirm-print-appointment/confirm-print-appointment.component';
import { HolidaysService } from '@api-rest/services/holidays.service';
import { DateFormat, momentFormat } from '@core/utils/moment.utils';
import { DiscardWarningComponent } from '@presentation/dialogs/discard-warning/discard-warning.component';
import { Moment } from 'moment';
import { AppointmentsFacadeService } from '@turnos/services/appointments-facade.service';

@Component({
	selector: 'app-appointment-details',
	templateUrl: './appointment-details.component.html',
	styleUrls: ['./appointment-details.component.scss'],
})
export class AppointmentDetailsComponent implements OnInit {
	readonly MODALITY_PATIENT_VIRTUAL_ATTENTION = EAppointmentModality.PATIENT_VIRTUAL_ATTENTION;
	readonly MODALITY_SECOND_OPINION_VIRTUAL = EAppointmentModality.SECOND_OPINION_VIRTUAL_ATTENTION;
	@Input() modalityAttention?: EAppointmentModality;
	@Input() emptyAppointment: EmptyAppointmentDto;
	@Input() patientId: number;
	@Input() searchInitialDate: Moment;
	@Input() searchEndingDate: Moment;
	@Input() referenceSummary?: ReferenceSummaryDto;
	@Output() resetInformation = new EventEmitter<void>();
	appointmentTime: Date = new Date();
	private selectedHolidayDay: Date;

	constructor(
		private readonly dialog: MatDialog,
		private readonly datePipe: DatePipe,
		private readonly holidayService: HolidaysService,
		private readonly appointmentsFacade: AppointmentsFacadeService,
	) { }

	ngOnInit(): void {
		const timeData = this.emptyAppointment.hour.split(":");
		this.appointmentTime.setHours(+timeData[0]);
		this.appointmentTime.setMinutes(+timeData[1]);
		this.appointmentTime.setSeconds(+timeData[2]);
	}

	checkAvailability() {
		const initialDate = momentFormat(this.searchInitialDate, DateFormat.API_DATE);
		const endingDate = momentFormat(this.searchEndingDate, DateFormat.API_DATE);

		let isHoliday = false;
		this.holidayService.getHolidays(initialDate, endingDate).subscribe(holidays => {
			if (holidays.length) {
				this.selectedHolidayDay = this.appointmentsFacade.checkIfHoliday(holidays, this.emptyAppointment.date);
				isHoliday = this.selectedHolidayDay ? true : false;

				if (isHoliday) {
					const dialogRef = this.dialog.open(DiscardWarningComponent, {
						data: this.appointmentsFacade.getHolidayData(this.selectedHolidayDay)
					});
					dialogRef.afterClosed().subscribe((result: boolean) => {
						if (result || result == undefined) {
							dialogRef?.close();
						}
						else {
							this.assignAppointment();
						}
					});
				} else {
					this.assignAppointment();
				}
			}
			else
				this.assignAppointment();
		})
	}

	assignAppointment() {
		const dialogReference = this.dialog.open(NewAppointmentComponent, {
			width: '43%',
			disableClose: true,
			data: {
				date: this.emptyAppointment.date,
				diaryId: this.emptyAppointment.diaryId,
				hour: this.emptyAppointment.hour,
				openingHoursId: this.emptyAppointment.openingHoursId,
				overturnMode: false,
				patientId: this.patientId ? this.patientId : null,
				modalityAttention: this.modalityAttention,
				protectedAppointment: !!this.referenceSummary,
				referenceSummary: this.referenceSummary,
			}
		});
		dialogReference.afterClosed().subscribe(
			(result: any) => {
				if (result !== -1) {
					this.resetInformation.emit();

					var fullAppointmentDate = this.datePipe.transform(this.emptyAppointment.date, DatePipeFormat.FULL_DATE);
					fullAppointmentDate = fullAppointmentDate[0].toUpperCase() + fullAppointmentDate.slice(1);
					const timeData = this.emptyAppointment.hour.split(":");

					let specialtyAndAlias= '';
					if (this.emptyAppointment.alias)
						specialtyAndAlias = `${this.emptyAppointment.alias}`;
					if (this.emptyAppointment.clinicalSpecialtyName)
						specialtyAndAlias = `${specialtyAndAlias} (${this.emptyAppointment.clinicalSpecialtyName})`;
					if (result.email && (this.modalityAttention === this.MODALITY_PATIENT_VIRTUAL_ATTENTION || this.modalityAttention === this.MODALITY_SECOND_OPINION_VIRTUAL)) {
						var message = 'Se podrá acceder a la teleconsulta a través del link que se ha enviado a ' + `<strong> ${result.email}</strong>`
					}

					this.dialog.open(ConfirmPrintAppointmentComponent, {
						width: '40%',
						data: {
							title: 'turnos.new-appointment.ASSIGNED_APPOINTMENT',
							content: 'Se ha asignado un turno el ' +
								`<strong>${fullAppointmentDate} ${timeData[0]}:${timeData[1]} hs </strong>` +
								' para ' +
								`${this.emptyAppointment.doctorFullName}
							  ${specialtyAndAlias}` + ' en ' +
								`${this.emptyAppointment.doctorsOfficeDescription}`,
							message: message,
							appointmentId: result.id,
						},

					});
				}
			}
		);
	}

}
