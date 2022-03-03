import { Component, Input } from '@angular/core';
import { AssignedAppointmentDto } from '@api-rest/api-model';

@Component({
  selector: 'app-assigned-appointment',
  templateUrl: './assigned-appointment.component.html',
  styleUrls: ['./assigned-appointment.component.scss']
})
export class AssignedAppointmentComponent {

  @Input() appointment: AssignedAppointmentDto;

  constructor() { }

}