import { DatePipe } from '@angular/common';
import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { DiaryAvailableProtectedAppointmentsDto } from '@api-rest/api-model';
import { dateDtoToDate, timeDtoToDate } from '@api-rest/mapper/date-dto.mapper';
import { DatePipeFormat } from '@core/utils/date.utils';
import { DateFormat, dateToMoment } from '@core/utils/moment.utils';
import { ConfirmPrintAppointmentComponent } from '@turnos/dialogs/confirm-print-appointment/confirm-print-appointment.component';
import { NewAppointmentComponent } from '@turnos/dialogs/new-appointment/new-appointment.component';

@Component({
  selector: 'app-appointment-result-view',
  templateUrl: './appointment-result-view.component.html',
  styleUrls: ['./appointment-result-view.component.scss']
})
export class AppointmentResultViewComponent implements OnInit {

  @Input() appointment: DiaryAvailableProtectedAppointmentsDto;
  @Input() patientId: number;
  @Output() resetAppointmentList = new EventEmitter<void>();
  viewDate: string = '';
  viewMinutes: string = '';

  constructor(
    private readonly datePipe: DatePipe,
    private readonly dialog: MatDialog,
  ) { }

  ngOnInit(): void {
    this.viewDate = this.datePipe.transform(dateDtoToDate(this.appointment.date), DatePipeFormat.FULL_DATE);
    this.viewMinutes = this.appointment.hour.minutes < 10 ?
      '0' + this.appointment.hour.minutes :
      this.appointment.hour.minutes.toString();
  }

  assign(): void {
    const appointmentDate = dateToMoment(dateDtoToDate(this.appointment.date)).format(DateFormat.API_DATE);
    const appointmentHour = this.datePipe.transform(timeDtoToDate(this.appointment.hour), DatePipeFormat.MEDIUM_TIME);
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
        protectedAppointment: this.appointment,
      }
    });
    dialogRef.afterClosed().subscribe(
      (result: number) => {
        if (result !== -1) {
          this.resetAppointmentList.emit();

          var fullAppointmentDate = this.datePipe.transform(appointmentDate, DatePipeFormat.FULL_DATE);
          fullAppointmentDate = fullAppointmentDate[0].toUpperCase() + fullAppointmentDate.slice(1);
          const timeData = appointmentHour.split(":");

          this.dialog.open(ConfirmPrintAppointmentComponent, {
            width: '40%',
            data: {
              title: 'turnos.new-appointment.ASSIGNED_APPOINTMENT',
              content: 'Se ha asignado un turno el ' +
                `<strong>${fullAppointmentDate} ${timeData[0]}:${timeData[1]} hs </strong>` +
                ' para ' +
                `${this.appointment.professionalFullName} (${this.appointment?.clinicalSpecialty?.name})` + ' en ' +
                `${this.appointment.doctorOffice}`,
              appointmentId: result,
            },

          });
        }
      }
    );
  }

}
