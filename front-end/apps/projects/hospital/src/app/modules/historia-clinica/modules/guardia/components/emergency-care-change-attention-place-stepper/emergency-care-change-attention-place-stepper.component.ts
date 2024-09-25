import { Component, EventEmitter, Input, Output } from '@angular/core';
import { ChangeEmergencyCareEpisodeAttentionPlaceDto, EmergencyCareAttentionPlaceDto, EmergencyCarePatientDto } from '@api-rest/api-model';
import { SpaceType } from '../emergency-care-attention-place-sector/emergency-care-attention-place-sector.component';

@Component({
	selector: 'app-emergency-care-change-attention-place-stepper',
	templateUrl: './emergency-care-change-attention-place-stepper.component.html',
	styleUrls: ['./emergency-care-change-attention-place-stepper.component.scss']
})
export class EmergencyCareChangeAttentionPlaceStepperComponent {

	sectorId: number = null;
	selectedSpaceType: SpaceType;
	newPlace: ChangeEmergencyCareEpisodeAttentionPlaceDto;

	@Input() patient: EmergencyCarePatientDto;
	@Output() formEmitter: EventEmitter<boolean> = new EventEmitter<boolean>();

	constructor() { }

	save() {
		//eventemitter del form al dialogo
		this.formEmitter.emit(true);
	}

	setSector(sector: EmergencyCareAttentionPlaceDto) {
		console.log(sector)
	}

	setSelectedSpaceType(selectedSpaceType: SpaceType) {
		this.selectedSpaceType = selectedSpaceType;
	}
}
