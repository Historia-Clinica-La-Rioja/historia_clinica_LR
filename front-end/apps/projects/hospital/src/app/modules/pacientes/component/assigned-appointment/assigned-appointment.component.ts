import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-assigned-appointment',
  templateUrl: './assigned-appointment.component.html',
  styleUrls: ['./assigned-appointment.component.scss']
})
export class AssignedAppointmentComponent {

  @Input() appointment: AssignedAppointment;

  constructor() { }

}

export interface AssignedAppointment {
  professionalName: string;
  license: number;
  specialties: string[];
  date: string;
  hour: string;
  office: string;
}
