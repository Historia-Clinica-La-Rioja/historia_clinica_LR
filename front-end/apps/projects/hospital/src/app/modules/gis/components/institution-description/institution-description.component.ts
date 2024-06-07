import { Component, EventEmitter, Input, Output } from '@angular/core';
import { GetSanitaryResponsibilityAreaInstitutionAddressDto, GlobalCoordinatesDto } from '@api-rest/api-model';
import { ButtonType } from '@presentation/components/button/button.component';
import { transformCoordinates } from '../../constants/coordinates.utils';

export interface InstitutionDescription {
	address: GetSanitaryResponsibilityAreaInstitutionAddressDto,
	title: string,
	institution: string,
	coordinates: GlobalCoordinatesDto
}

@Component({
	selector: 'app-institution-description',
	templateUrl: './institution-description.component.html',
	styleUrls: ['./institution-description.component.scss']
})
export class InstitutionDescriptionComponent {

	@Input() set setInstitutionDescription(institutionDescription: InstitutionDescription) {
		this.institutionDescription = institutionDescription;
		this.coordinatesToShow = transformCoordinates(institutionDescription.coordinates);
	}
	@Input() showActions: boolean = true;
	@Output() nextStep = new EventEmitter<boolean>();
	@Output() previousStep = new EventEmitter<boolean>();

	institutionDescription: InstitutionDescription;
	isSaving: boolean = false;
	ButtonType = ButtonType;
	coordinatesToShow: string;

	constructor() {}

	next = () => {
		this.nextStep.emit(true);
	}

	previousStepper = () => {
		this.previousStep.emit(true);
	}
}
