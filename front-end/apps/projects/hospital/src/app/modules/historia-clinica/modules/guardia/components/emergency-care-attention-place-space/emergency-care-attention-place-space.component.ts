import { Component, Input } from '@angular/core';
import { SpaceItem } from '../emergency-care-attention-place-sector/emergency-care-attention-place-sector.component';

@Component({
	selector: 'app-emergency-care-attention-place-space',
	templateUrl: './emergency-care-attention-place-space.component.html',
	styleUrls: ['./emergency-care-attention-place-space.component.scss']
})
export class EmergencyCareAttentionPlaceSpaceComponent {

	@Input() space: SpaceItem

	constructor() { }

}
