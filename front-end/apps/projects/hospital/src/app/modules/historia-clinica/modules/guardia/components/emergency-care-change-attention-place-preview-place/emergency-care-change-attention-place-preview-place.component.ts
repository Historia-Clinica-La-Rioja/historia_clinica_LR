import { Component, Input } from '@angular/core';
import { PlacePreview } from '../emergency-care-change-attention-place-preview-change/emergency-care-change-attention-place-preview-change.component';
import { IDENTIFIER_CASES } from '@hsi-components/identifier-cases/identifier-cases.component';
import { SpaceType } from '../emergency-care-attention-place-sector/emergency-care-attention-place-sector.component';

@Component({
	selector: 'app-emergency-care-change-attention-place-preview-place',
	templateUrl: './emergency-care-change-attention-place-preview-place.component.html',
	styleUrls: ['./emergency-care-change-attention-place-preview-place.component.scss']
})
export class EmergencyCareChangeAttentionPlacePreviewPlaceComponent{

	readonly IDENTIFIER_CASES = IDENTIFIER_CASES;
	spaceType = SpaceType;

	@Input() title: string;
	@Input() placeInfo: PlacePreview;

	constructor() { }

}
