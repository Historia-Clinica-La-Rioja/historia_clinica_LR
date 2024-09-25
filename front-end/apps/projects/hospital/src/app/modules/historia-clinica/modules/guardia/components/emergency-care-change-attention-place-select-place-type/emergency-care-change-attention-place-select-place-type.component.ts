import { Component, EventEmitter, Input, Output, SimpleChanges } from '@angular/core';
import { EmergencyCareSectorHasAttentionPlaceDto } from '@api-rest/api-model';
import { Observable } from 'rxjs';
import { MatRadioChange } from '@angular/material/radio';
import { EmergencyCareAttentionPlaceService } from '../../services/emergency-care-attention-place.service';
import { SpaceType } from '../emergency-care-attention-place-sector/emergency-care-attention-place-sector.component';

@Component({
	selector: 'app-emergency-care-change-attention-place-select-place-type',
	templateUrl: './emergency-care-change-attention-place-select-place-type.component.html',
	styleUrls: ['./emergency-care-change-attention-place-select-place-type.component.scss']
})
export class EmergencyCareChangeAttentionPlaceSelectPlaceTypeComponent {

	spaceType = SpaceType;
	placesAvailability$: Observable<EmergencyCareSectorHasAttentionPlaceDto>;

	@Input() sectorId: number;
	@Output() selectedSpaceType = new EventEmitter<SpaceType>();

	constructor(private emergencyCareAttentionPlaceService: EmergencyCareAttentionPlaceService) { }

	ngOnChanges(changes: SimpleChanges) {
		if (changes.sectorId && this.sectorId !== null) {
			this.placesAvailability$ = this.emergencyCareAttentionPlaceService.getSectorHasPlaces(this.sectorId);
		}
	}

	onPlaceTypeChange($event: MatRadioChange) {
		this.selectedSpaceType.emit($event.value);
	}
}
