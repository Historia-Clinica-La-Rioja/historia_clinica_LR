import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { DiaryAvailableAppointmentsDto, EAppointmentModality, ReferenceSummaryDto } from '@api-rest/api-model';
import { dateDtoToDate, stringToDate, timeDtoToDate } from '@api-rest/mapper/date-dto.mapper';
import { NewAppointmentComponent } from '@turnos/dialogs/new-appointment/new-appointment.component';
import { SearchAppointmentCriteria } from '../search-appointments-in-care-network/search-appointments-in-care-network.component';
import { HolidayCheckService } from '@shared-appointment-access-management/services/holiday-check.service';
import { ConfirmPrintAppointmentComponent } from '@shared-appointment-access-management/dialogs/confirm-print-appointment/confirm-print-appointment.component';
import { toApiFormat } from '@api-rest/mapper/date.mapper';
import { DateFormatPipe } from '@presentation/pipes/date-format.pipe';

@Component({
	selector: 'app-appointment-result-view',
	templateUrl: './appointment-result-view.component.html',
	styleUrls: ['./appointment-result-view.component.scss']
})
export class AppointmentResultViewComponent implements OnInit {
	readonly MODALITY_ON_SITE_ATTENTION = EAppointmentModality.ON_SITE_ATTENTION;
	@Input() modalityAttention?: EAppointmentModality;
	@Input() appointment: DiaryAvailableAppointmentsDto;
	@Input() patientId: number;
	@Input() searchAppointmentCriteria: SearchAppointmentCriteria;
	@Input() referenceSummary?: ReferenceSummaryDto;
	@Output() resetInformation = new EventEmitter<void>();
	viewDate: string = '';
	viewMinutes: string = '';

	constructor(
		private readonly dateFormatPipe: DateFormatPipe,
		private readonly dialog: MatDialog,
		private readonly holidayService: HolidayCheckService,
	) { }

	ngOnInit(): void {
		this.viewDate = this.dateFormatPipe.transform(dateDtoToDate(this.appointment.date), 'fulldate');
		this.viewMinutes = this.appointment.hour.minutes < 10 ?
			'0' + this.appointment.hour.minutes :
			this.appointment.hour.minutes.toString();
	}

	assign(): void {
		const appointmentDate = toApiFormat(dateDtoToDate(this.appointment.date));
		const appointmentHour = this.dateFormatPipe.transform(timeDtoToDate(this.appointment.hour), 'time');
		this.holidayService.checkAvailability(appointmentDate).subscribe(isAvailable => {
			if (isAvailable) {
				const dialogRef = this.dialog.open(NewAppointmentComponent, {
					width: '45%',
					disableClose: true,
					data: {
						date: appointmentDate,
						diaryId: this.appointment.diaryId,
						hour: appointmentHour,
						openingHoursId: this.appointment.openingHoursId,
						overturnMode: this.appointment.overturnMode,
						patientId: this.patientId ? this.patientId : null,
						protectedAppointment: true,
						modalityAttention: this.modalityAttention,
						searchAppointmentCriteria: this.searchAppointmentCriteria,
						referenceSummary: this.referenceSummary,
						institutionId: this.appointment.institution.id
					}
				});
				dialogRef.afterClosed().subscribe(
					(result: any) => {
						if (result !== -1) {
							this.resetInformation.emit();

							var fullAppointmentDate = this.dateFormatPipe.transform(stringToDate(appointmentDate), 'fulldate');
							fullAppointmentDate = fullAppointmentDate[0].toUpperCase() + fullAppointmentDate.slice(1);
							const timeData = appointmentHour.split(":");

							let specialtyAndAlias = '';
							if (result.alias)
								specialtyAndAlias = `${result.alias}`;
							if (result.clinicalSpecialtyName)
								specialtyAndAlias = `${specialtyAndAlias} (${result.clinicalSpecialtyName})`;

							if (result.email && !(this.modalityAttention === this.MODALITY_ON_SITE_ATTENTION)) {
								var message = 'Se podrá acceder a la teleconsulta a través del link que se ha enviado a ' + `<strong> ${result.email}</strong>`
							}

							this.dialog.open(ConfirmPrintAppointmentComponent, {
								width: '40%',
								data: {
									title: 'turnos.new-appointment.ASSIGNED_APPOINTMENT',
									content: 'Se ha asignado un turno el ' +
										`<strong>${fullAppointmentDate} ${timeData[0]}:${timeData[1]} hs </strong>` +
										' para ' +
										`${this.appointment.professionalFullName} ${specialtyAndAlias}` + ' en ' +
										`${this.appointment.doctorOffice}`,
									appointmentId: result.id,
									message: message,
								},

							});
						}
					}
				);
			}
		});
	}
}
