import { Component, EventEmitter, Input, Output } from '@angular/core';
import { EReferenceRegulationState, ReferenceCompleteDataDto } from '@api-rest/api-model';

@Component({
	selector: 'app-popup-actions',
	templateUrl: './popup-actions.component.html',
	styleUrls: ['./popup-actions.component.scss']
})
export class PopupActionsComponent {

	referenceApprovalState = {
		approved: EReferenceRegulationState.APPROVED,
		pending: EReferenceRegulationState.WAITING_APPROVAL,
	}

	@Input() reportCompleteData: ReferenceCompleteDataDto;
	@Output() newState = new EventEmitter<boolean>();

	constructor() { }
}
