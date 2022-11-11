import { DatePipe } from '@angular/common';
import { Component, Input, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { DiaryAvailableProtectedAppointmentsDto } from '@api-rest/api-model';
import { dateDtoToDate } from '@api-rest/mapper/date-dto.mapper';
import { DatePipeFormat } from '@core/utils/date.utils';
import { NewAppointmentComponent } from '@turnos/dialogs/new-appointment/new-appointment.component';

@Component({
  selector: 'app-appointment-result-view',
  templateUrl: './appointment-result-view.component.html',
  styleUrls: ['./appointment-result-view.component.scss']
})
export class AppointmentResultViewComponent implements OnInit {

  @Input() appointment: DiaryAvailableProtectedAppointmentsDto;
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
    const dialogRef = this.dialog.open(NewAppointmentComponent, {
      width: '35%',
      data: {
        date: null,
        diaryId: this.appointment.diaryId,
        hour: null,
        openingHoursId: null,
        overturnMode: null,
        patientId: null,
        protectedAppointment: this.appointment,
      }
    });
    dialogRef.afterClosed().subscribe(resp => console.log(resp)
    );
  }

}
