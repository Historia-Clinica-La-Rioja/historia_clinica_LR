import { Component, EventEmitter, Input, Output } from '@angular/core';
import { InstitutionDescription } from '../institution-description/institution-description.component';
import { ButtonType } from '@presentation/components/button/button.component';

@Component({
	selector: 'app-institution-description-detail',
	templateUrl: './institution-description-detail.component.html',
	styleUrls: ['./institution-description-detail.component.scss']
})
export class InstitutionDescriptionDetailComponent {

	@Input() set setInstitutionDescription(institutionDescription: InstitutionDescription) {
		this.institutionDescription = institutionDescription;
	}
	institutionDescription: InstitutionDescription;
	@Output() stepperEdit = new EventEmitter<boolean>();
	@Output() patientSearch = new EventEmitter<boolean>();
	ButtonType = ButtonType;

	edit = () => {
		this.stepperEdit.emit(true);
	}
	
	goToPatientSearch = () => {
		this.patientSearch.emit(true);
	}

}
