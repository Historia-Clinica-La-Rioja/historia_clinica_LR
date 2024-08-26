import { Component, Input } from '@angular/core';
import { DateTimeDto } from '@api-rest/api-model';

@Component({
  selector: 'app-emergency-care-waiting-status',
  templateUrl: './emergency-care-waiting-status.component.html',
  styleUrls: ['./emergency-care-waiting-status.component.scss']
})
export class EmergencyCareWaitingStatusComponent {

  @Input() stateUpdateOn: DateTimeDto;

  constructor() { }

}
