import { Component, Input } from '@angular/core';
import { SpaceType } from '../emergency-care-attention-place-sector/emergency-care-attention-place-sector.component';
import { SpaceTypeDetails } from '../emergency-care-attention-place-details/emergency-care-attention-place-details.component';
import { AvailableButtonType } from '@presentation/components/button-availability/button-availability.component';
import { Color } from '@presentation/colored-label/colored-label.component';
import { PLACE_TYPE } from './emergency-care-detail-place-type.constants';

@Component({
	selector: 'app-emergency-care-detail-place-type',
	templateUrl: './emergency-care-detail-place-type.component.html',
	styleUrls: ['./emergency-care-detail-place-type.component.scss']
})
export class EmergencyCareDetailPlaceTypeComponent {

	detailPlaceType: DetailPlaceType;
	_spaceTypeDetails: SpaceTypeDetails;
	readonly PLACE_TYPE = PLACE_TYPE;

	@Input()
	set spaceTypeDetails(spaceTypeDetails: SpaceTypeDetails) {
		this._spaceTypeDetails = spaceTypeDetails;
		this.updateAvailability(spaceTypeDetails)
	};
	@Input() spaceType: SpaceType;

	private updateAvailability(spaceTypeDetails: SpaceTypeDetails) {
		this.detailPlaceType = this.PLACE_TYPE[spaceTypeDetails.state];
	}
}

interface DetailPlaceType {
	labelDescription: string;
	labelColor: Color;
	buttonType: AvailableButtonType;
}
