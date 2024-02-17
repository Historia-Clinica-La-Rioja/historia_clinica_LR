import { Component, Input, OnInit } from '@angular/core';
import { CalendarEvent } from 'angular-calendar';

const MIN_APPOINTMENTS = 7;
const MILISECOND = 1000;
const MINUTES = 60;

@Component({
  selector: 'app-calendar-event-view',
  templateUrl: './calendar-event-view.component.html',
  styleUrls: ['./calendar-event-view.component.scss']
})

export class CalendarEventViewComponent implements OnInit {

  @Input() event: CalendarEvent;
  @Input() appointmentDuration: number;

  showInformation: boolean;

  constructor() { }

  ngOnInit(): void {
    if(this.event.end) this.showInformation = this.calculateAmountOfAppointmentsInSlot() >= MIN_APPOINTMENTS;
  }

  calculateAmountOfAppointmentsInSlot(): number {
    const amountOfMinutesInSlot = (this.event.end.getTime() - this.event.start.getTime()) / MILISECOND / MINUTES;
    return Math.abs(Math.round(amountOfMinutesInSlot) / this.appointmentDuration);
  }

}
