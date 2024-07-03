import { Component, EventEmitter, Input, Output } from '@angular/core';
import { InstitutionDescription } from '../institution-description/institution-description.component';

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

	edit = () => {
		this.stepperEdit.emit(true);
	}

}
