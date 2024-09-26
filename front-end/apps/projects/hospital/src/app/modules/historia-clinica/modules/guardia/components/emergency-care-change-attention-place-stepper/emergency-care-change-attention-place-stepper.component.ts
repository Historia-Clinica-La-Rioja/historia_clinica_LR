import { Component, EventEmitter, Input, Output } from '@angular/core';
import { ChangeEmergencyCareEpisodeAttentionPlaceDto, EmergencyCareAttentionPlaceDto, EmergencyCareDoctorsOfficeDto, EmergencyCarePatientDto, ShockroomDto } from '@api-rest/api-model';
import { SpaceType } from '../emergency-care-attention-place-sector/emergency-care-attention-place-sector.component';
import { ButtonType } from '@presentation/components/button/button.component';
import { PlacePreview } from '../emergency-care-change-attention-place-preview-change/emergency-care-change-attention-place-preview-change.component';

@Component({
	selector: 'app-emergency-care-change-attention-place-stepper',
	templateUrl: './emergency-care-change-attention-place-stepper.component.html',
	styleUrls: ['./emergency-care-change-attention-place-stepper.component.scss']
})
export class EmergencyCareChangeAttentionPlaceStepperComponent {

	spaceType = SpaceType;
	buttonType = ButtonType;

	sectorId: number = null;
	selectedSpaceType: SpaceType;
	newPlace: ChangeEmergencyCareEpisodeAttentionPlaceDto;
	newPlacePreview: PlacePreview;

	@Input() patient: EmergencyCarePatientDto;
	@Input() lastPlacePreview: PlacePreview;
	@Output() formEmitter: EventEmitter<boolean> = new EventEmitter<boolean>();

	constructor() { }

	save() {
		//eventemitter del form al dialogo
		this.formEmitter.emit(true);
	}

	setSector(sector: EmergencyCareAttentionPlaceDto) {
		this.resetAll();
		this.newPlace = null;
		this.sectorId = sector.id;
		this.newPlacePreview.sectorDescription = sector.description;
	}

	setSelectedSpaceType(selectedSpaceType: SpaceType) {
		this.newPlace = null;
		this.resetNewPlacePreview();
		this.selectedSpaceType = selectedSpaceType;
		this.newPlacePreview.placeType = selectedSpaceType;
	}

	setSelectedDoctorOffice(selectedDoctorOffice: EmergencyCareDoctorsOfficeDto) {
		this.resetNewPlace();
		this.newPlace.emergencyCareEpisodeAttentionPlace.doctorsOfficeId = selectedDoctorOffice.id;
		this.newPlacePreview.placeTypeDescription = selectedDoctorOffice.description;
	}

	setSelectedShockroom(selectedShockroom: ShockroomDto) {
		this.resetNewPlace();
		this.newPlace.emergencyCareEpisodeAttentionPlace.shockroomId = selectedShockroom.id;
		this.newPlacePreview.placeTypeDescription = selectedShockroom.description;
	}

	private resetAll(){
		this.selectedSpaceType = null;
		this.resetNewPlace()
		this.resetNewPlacePreview();
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

	private resetNewPlacePreview(){
		this.newPlacePreview = {
			...this.newPlacePreview,
			placeType : null,
			placeTypeDescription: null,
		}
	}
}
