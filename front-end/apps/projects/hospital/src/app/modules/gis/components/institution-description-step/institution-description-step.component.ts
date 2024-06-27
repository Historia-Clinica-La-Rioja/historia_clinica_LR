import { Component, EventEmitter, Input, Output } from '@angular/core';
import { InstitutionDescription } from '../institution-description/institution-description.component';
import { ButtonType } from '@presentation/components/button/button.component';

@Component({
	selector: 'app-institution-description-step',
	templateUrl: './institution-description-step.component.html',
	styleUrls: ['./institution-description-step.component.scss']
})
export class InstitutionDescriptionStepComponent {

	@Input() set setInstitutionDescription(institutionDescription: InstitutionDescription) {
		this.institutionDescription = institutionDescription;
	}
	institutionDescription: InstitutionDescription;
	@Output() nextStep = new EventEmitter<boolean>();
	@Output() previousStep = new EventEmitter<boolean>();
	ButtonType = ButtonType;
	
	next = () => {
		this.nextStep.emit(true);
	}

	previousStepper = () => {
		this.previousStep.emit(true);
	}
}
