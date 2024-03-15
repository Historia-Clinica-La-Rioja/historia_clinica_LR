import { Component, Input } from '@angular/core';
import { EReferenceRegulationState, ReferenceCompleteDataDto } from '@api-rest/api-model';
import { ContextService } from '@core/services/context.service';
import { NO_INSTITUTION } from '../../../home/home.component';

@Component({
	selector: 'app-popup-actions',
	templateUrl: './popup-actions.component.html',
	styleUrls: ['./popup-actions.component.scss']
})
export class PopupActionsComponent {

	NO_INSTITUTION = NO_INSTITUTION;

	referenceApprovalState = {
		approved: EReferenceRegulationState.APPROVED,
		pending: EReferenceRegulationState.WAITING_APPROVAL,
	}

	@Input() reportCompleteData: ReferenceCompleteDataDto;

	constructor(
		readonly contextService: ContextService,
	) { }
}
