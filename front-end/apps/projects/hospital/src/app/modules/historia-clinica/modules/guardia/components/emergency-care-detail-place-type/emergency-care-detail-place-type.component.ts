import { Component, Input, OnChanges, OnInit, SimpleChanges } from '@angular/core';
import { SpaceType } from '../emergency-care-attention-place-sector/emergency-care-attention-place-sector.component';
import { SpaceTypeDetails } from '../emergency-care-attention-place-details/emergency-care-attention-place-details.component';
import { AvailableButtonType } from '@presentation/components/button-availability/button-availability.component';
import { Color } from '@presentation/colored-label/colored-label.component';

@Component({
	selector: 'app-emergency-care-detail-place-type',
	templateUrl: './emergency-care-detail-place-type.component.html',
	styleUrls: ['./emergency-care-detail-place-type.component.scss']
})
export class EmergencyCareDetailPlaceTypeComponent implements OnInit, OnChanges {

	labelDescription: string;
	labelColor: Color;
	buttonType: AvailableButtonType;

	@Input() spaceTypeDetails: SpaceTypeDetails;
	@Input() spaceType: SpaceType;

	ngOnInit() {
		this.updateAvailability();
	}

	ngOnChanges(changes: SimpleChanges) {
		if (changes.spaceTypeDetails && !changes.spaceTypeDetails.isFirstChange()) {
			this.updateAvailability();
		}
	}

	private updateAvailability() {
		const available = this.spaceTypeDetails?.available;

		this.labelDescription = available ?
			'guardia.home.attention_places.space.FREE' :
			'guardia.home.attention_places.space.NOT_FREE';

		this.labelColor = available ? Color.GREEN : Color.RED;

		this.buttonType = available ?
			AvailableButtonType.AVAILABLE :
			AvailableButtonType.NOT_AVAILABLE;
	}
}
