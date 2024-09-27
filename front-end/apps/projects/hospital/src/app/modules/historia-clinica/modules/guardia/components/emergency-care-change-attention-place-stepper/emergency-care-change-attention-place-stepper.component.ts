import { ChangeDetectorRef, Component, EventEmitter, Input, Output } from '@angular/core';
import { ChangeEmergencyCareEpisodeAttentionPlaceDto, EmergencyCareAttentionPlaceDto, EmergencyCareBedDto, EmergencyCareDoctorsOfficeDto, EmergencyCarePatientDto, ShockroomDto } from '@api-rest/api-model';
import { SpaceType } from '../emergency-care-attention-place-sector/emergency-care-attention-place-sector.component';
import { ButtonType } from '@presentation/components/button/button.component';
import { PlacePreview } from '../emergency-care-change-attention-place-preview-change/emergency-care-change-attention-place-preview-change.component';
import { MatStepper } from '@angular/material/stepper';

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
	@Input() episodeId: number;
	@Output() formEmitter: EventEmitter<ChangeEmergencyCareEpisodeAttentionPlaceDto> = new EventEmitter<ChangeEmergencyCareEpisodeAttentionPlaceDto>();

	constructor(
		private changeDetectorRef: ChangeDetectorRef
	) { }

	save() {
		this.formEmitter.emit(this.newPlace);
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

	setSelectedBed(selectedBed: EmergencyCareBedDto, stepper: MatStepper) {
		this.resetNewPlace();
		this.newPlace.emergencyCareEpisodeAttentionPlace.bedId = selectedBed.id;
		this.newPlacePreview.sectorDescription = selectedBed.sectorDescription;
		this.newPlacePreview.placeTypeDescription = selectedBed.description;
		this.changeDetectorRef.detectChanges();
		stepper.next();
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
			episodeId: this.episodeId
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
