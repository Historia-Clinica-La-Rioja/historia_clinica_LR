import { stringToDate } from './../../../api-rest/mapper/date-dto.mapper';
import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { NewAppointmentComponent } from '@turnos/dialogs/new-appointment/new-appointment.component';
import { EAppointmentModality, EmptyAppointmentDto, ReferenceSummaryDto } from '@api-rest/api-model';
import { HolidayCheckService } from '@shared-appointment-access-management/services/holiday-check.service';
import { ConfirmPrintAppointmentComponent } from '@shared-appointment-access-management/dialogs/confirm-print-appointment/confirm-print-appointment.component';
import { DateFormatPipe } from '@presentation/pipes/date-format.pipe';

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
	@Input() referenceSummary?: ReferenceSummaryDto;
	@Output() resetInformation = new EventEmitter<void>();
	appointmentDate: Date = new Date();

	constructor(
		private readonly dialog: MatDialog,
		private readonly dateFormatPipe: DateFormatPipe,
		private readonly holidayService: HolidayCheckService,
	) { }

	ngOnInit(): void {
		this.appointmentDate = new Date(this.emptyAppointment.date + 'T' + this.emptyAppointment.hour);
	}

	assign() {
		this.holidayService.checkAvailability(this.emptyAppointment.date).subscribe(isAvailable => {
			if (isAvailable) {
				this.assignAppointment();
			}
		});
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

					var fullAppointmentDate = this.dateFormatPipe.transform(stringToDate(this.emptyAppointment.date), 'fulldate');
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
