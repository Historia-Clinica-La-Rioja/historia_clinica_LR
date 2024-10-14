import { Component, Input } from '@angular/core';
import { MasterDataDto } from '@api-rest/api-model';
import { IDENTIFIER_CASES } from '@hsi-components/identifier-cases/identifier-cases.component';

@Component({
	selector: 'app-emergency-care-attention-place-entry-details',
	templateUrl: './emergency-care-attention-place-entry-details.component.html',
	styleUrls: ['./emergency-care-attention-place-entry-details.component.scss']
})
export class EmergencyCareAttentionPlaceEntryDetailsComponent{

	readonly IDENTIFIER_CASES = IDENTIFIER_CASES;

	@Input() reason: string;
	@Input() type: MasterDataDto;

	constructor() { }

}
