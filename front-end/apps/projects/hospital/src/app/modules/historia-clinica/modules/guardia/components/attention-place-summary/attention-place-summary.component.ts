import { Component, Input } from '@angular/core';
import { EmergencyCareDetails } from '../emergency-care-attention-place-details/emergency-care-attention-place-details.component';
import { IDENTIFIER_CASES } from '@hsi-components/identifier-cases/identifier-cases.component';
import { SpaceType } from '../emergency-care-attention-place-sector/emergency-care-attention-place-sector.component';

@Component({
	selector: 'app-attention-place-summary',
	templateUrl: './attention-place-summary.component.html',
	styleUrls: ['./attention-place-summary.component.scss']
})
export class AttentionPlaceSummaryComponent {

	readonly IDENTIFIER_CASES = IDENTIFIER_CASES;
	readonly SpaceType = SpaceType;

	@Input() attentionPlaceDetails: EmergencyCareDetails;

	constructor() { }

}
