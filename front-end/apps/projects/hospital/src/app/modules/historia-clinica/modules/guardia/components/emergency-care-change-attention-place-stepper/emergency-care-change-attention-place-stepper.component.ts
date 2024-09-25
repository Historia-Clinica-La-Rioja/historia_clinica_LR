import { Component, EventEmitter, Input, Output } from '@angular/core';
import { ChangeEmergencyCareEpisodeAttentionPlaceDto, EmergencyCareAttentionPlaceDto, EmergencyCareDoctorsOfficeDto, EmergencyCarePatientDto, ShockroomDto } from '@api-rest/api-model';
import { SpaceType } from '../emergency-care-attention-place-sector/emergency-care-attention-place-sector.component';

@Component({
	selector: 'app-emergency-care-change-attention-place-stepper',
	templateUrl: './emergency-care-change-attention-place-stepper.component.html',
	styleUrls: ['./emergency-care-change-attention-place-stepper.component.scss']
})
export class EmergencyCareChangeAttentionPlaceStepperComponent {

	spaceType = SpaceType;

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
		this.resetSelections();
		this.sectorId = sector.id;
	}

	setSelectedSpaceType(selectedSpaceType: SpaceType) {
		this.selectedSpaceType = selectedSpaceType;
	}

	setSelectedDoctorOffice(selectedDoctorOffice: EmergencyCareDoctorsOfficeDto) {
		this.resetNewPlace();
		this.newPlace.emergencyCareEpisodeAttentionPlace.doctorsOfficeId = selectedDoctorOffice.id;
	}

	setSelectedShockroom(selectedShockroom: ShockroomDto) {
		this.resetNewPlace();
		this.newPlace.emergencyCareEpisodeAttentionPlace.shockroomId = selectedShockroom.id;
		console.log(this.newPlace)
	}

	private resetSelections(){
		this.selectedSpaceType = null;
		this.resetNewPlace();
	}

	private resetNewPlace(){
		this.newPlace = {
			emergencyCareEpisodeAttentionPlace: {
				bedId: null,
				doctorsOfficeId: null,
				shockroomId: null
			},
			episodeId: null //pasar como input desde componente padre
		};
	}
}
