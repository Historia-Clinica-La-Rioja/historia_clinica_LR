import { Component, Input } from '@angular/core';
import { SpaceType } from '../emergency-care-attention-place-sector/emergency-care-attention-place-sector.component';

@Component({
	selector: 'app-emergency-care-change-attention-place-preview-change',
	templateUrl: './emergency-care-change-attention-place-preview-change.component.html',
	styleUrls: ['./emergency-care-change-attention-place-preview-change.component.scss']
})
export class EmergencyCareChangeAttentionPlacePreviewChangeComponent {

	@Input() lastAttentionPlace: PlacePreview;
	@Input() newAttentionPlace: PlacePreview;

	constructor() { }

}

export interface PlacePreview{
	sectorDescription: string,
	placeType: SpaceType,
	placeTypeDescription: string,
}
