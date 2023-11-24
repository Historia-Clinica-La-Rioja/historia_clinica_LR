import { Component, Input } from '@angular/core';
import { ReferenceCompleteDataDto } from '@api-rest/api-model';

@Component({
  selector: 'app-popup-actions',
  templateUrl: './popup-actions.component.html',
  styleUrls: ['./popup-actions.component.scss']
})
export class PopupActionsComponent {

  @Input() reportCompleteData: ReferenceCompleteDataDto;
  constructor() { }
}
