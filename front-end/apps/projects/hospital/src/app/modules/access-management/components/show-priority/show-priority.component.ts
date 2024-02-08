import { Component, Input } from '@angular/core';
import { MasterDataDto } from '@api-rest/api-model';
import { PRIORITY } from '@historia-clinica/modules/ambulatoria/constants/reference-masterdata';

@Component({
  selector: 'app-show-priority',
  templateUrl: './show-priority.component.html',
  styleUrls: ['./show-priority.component.scss']
})
export class ShowPriorityComponent {

  PRIORITY = PRIORITY;
  @Input() priority: MasterDataDto; 

  constructor() { }
}
