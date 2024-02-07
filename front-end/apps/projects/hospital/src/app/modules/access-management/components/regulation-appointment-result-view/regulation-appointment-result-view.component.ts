import { RegulationNewAppointmentData, RegulationNewAppointmentPopUpComponent } from '@access-management/dialogs/regulation-new-appointment-pop-up/regulation-new-appointment-pop-up.component';
import { DatePipe } from '@angular/common';
import { Component, EventEmitter, Input, Output } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { EAppointmentModality, DiaryAvailableAppointmentsDto, ReferenceSummaryDto } from '@api-rest/api-model';
import { dateDtoToDate, timeDtoToDate } from '@api-rest/mapper/date-dto.mapper';
import { DateFormat, DatePipeFormat } from '@core/utils/date.utils';
import { ConfirmPrintAppointmentComponent } from '@shared-appointment-access-management/dialogs/confirm-print-appointment/confirm-print-appointment.component';
import { HolidayCheckService } from '@shared-appointment-access-management/services/holiday-check.service';
import { SearchAppointmentCriteria } from '@turnos/components/search-appointments-in-care-network/search-appointments-in-care-network.component';
import format from 'date-fns/format';

@Component({
	selector: 'app-regulation-appointment-result-view',
	templateUrl: './regulation-appointment-result-view.component.html',
	styleUrls: ['./regulation-appointment-result-view.component.scss']
})
export class RegulationAppointmentResultViewComponent {

	readonly MODALITY_ON_SITE_ATTENTION = EAppointmentModality.ON_SITE_ATTENTION;
	@Input() modalityAttention?: EAppointmentModality;
	@Input() appointment: DiaryAvailableAppointmentsDto;
	@Input() searchAppointmentCriteria: SearchAppointmentCriteria;
	@Input() patientId: number;
	@Input() referenceSummary: ReferenceSummaryDto;
	@Output() resetInformation = new EventEmitter<void>();

	constructor(
		private readonly datePipe: DatePipe,
		private readonly dialog: MatDialog,
		private readonly holidayService: HolidayCheckService,
	) { }

	assign(): void {
		const appointmentDate = format(dateDtoToDate(this.appointment.date), DateFormat.API_DATE);
		const appointmentHour = this.datePipe.transform(timeDtoToDate(this.appointment.hour), DatePipeFormat.MEDIUM_TIME);
		this.holidayService.checkAvailability(appointmentDate).subscribe(isAvailable => {
			if (isAvailable) {
				const data: RegulationNewAppointmentData = {
					date: appointmentDate,
					diaryId: this.appointment.diaryId,
					hour: appointmentHour,
					openingHoursId: this.appointment.openingHoursId,
					overturnMode: this.appointment.overturnMode,
					modalityAttention: this.modalityAttention,
					searchAppointmentCriteria: this.searchAppointmentCriteria,
					institutionId: this.appointment.institution.id,
					patientId: this.patientId,
					referenceSummary: this.referenceSummary
				}
				const dialogRef = this.dialog.open(RegulationNewAppointmentPopUpComponent, {
					width: '45%',
					disableClose: true,
					data,
				});
				dialogRef.afterClosed().subscribe(
					(result: any) => {
						if (result !== -1) {
							this.resetInformation.emit();

							var fullAppointmentDate = this.datePipe.transform(appointmentDate, DatePipeFormat.FULL_DATE);
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
