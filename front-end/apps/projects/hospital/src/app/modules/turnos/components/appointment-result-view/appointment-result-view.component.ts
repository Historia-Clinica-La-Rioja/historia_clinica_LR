import { DatePipe } from '@angular/common';
import { Component, Input, OnInit } from '@angular/core';
import { DiaryAvailableProtectedAppointmentsDto } from '@api-rest/api-model';
import { dateDtoToDate } from '@api-rest/mapper/date-dto.mapper';
import { DatePipeFormat } from '@core/utils/date.utils';

@Component({
  selector: 'app-appointment-result-view',
  templateUrl: './appointment-result-view.component.html',
  styleUrls: ['./appointment-result-view.component.scss']
})
export class AppointmentResultViewComponent implements OnInit {

  @Input() appointment: DiaryAvailableProtectedAppointmentsDto;
  viewDate: string = '';
  viewMinutes: string = '';

  constructor(private readonly datePipe: DatePipe) { }

  ngOnInit(): void {
    this.viewDate = this.datePipe.transform(dateDtoToDate(this.appointment.date), DatePipeFormat.FULL_DATE);
    this.viewMinutes = this.appointment.hour.minutes < 10 ?
      '0' + this.appointment.hour.minutes :
      this.appointment.hour.minutes.toString();
  }

}
