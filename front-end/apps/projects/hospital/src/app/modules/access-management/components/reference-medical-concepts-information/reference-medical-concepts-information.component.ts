import { Component, Input } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import { SearchCriteria } from '@turnos/components/search-criteria/search-criteria.component';

@Component({
	selector: 'app-reference-medical-concepts-information',
	templateUrl: './reference-medical-concepts-information.component.html',
	styleUrls: ['./reference-medical-concepts-information.component.scss']
})
export class ReferenceMedicalConceptsInformationComponent {

	form: FormGroup<ReferenceMedicalConceptsInformationForm>;
	readonly searchCriteria = SearchCriteria;

	@Input()
	set referenceMedicalConceptsInfo(referenceMedicalConceptsInfo: ReferenceMedicalConceptsInformation) {
		this.setFormValues(referenceMedicalConceptsInfo);
	}

	constructor() {
		this.createForm();
	}

	private setFormValues(referenceMedicalConceptsInfo: ReferenceMedicalConceptsInformation) {
		const controls = this.form.controls;

		if (referenceMedicalConceptsInfo.careLine) {
			controls.requestByCareLine.setValue(true);
			controls.careLine.setValue(referenceMedicalConceptsInfo.careLine);
		} else
			controls.requestByCareLine.setValue(false);

		if (!referenceMedicalConceptsInfo.consultation) {
			controls.referenceType.setValue(SearchCriteria.PRACTICES);
			controls.studyCategory.setValue(referenceMedicalConceptsInfo.studyCategory);
			controls.practice.setValue(referenceMedicalConceptsInfo.practice);
		}
		else
			controls.referenceType.setValue(SearchCriteria.CONSULTATION);

		controls.specialties.setValue(referenceMedicalConceptsInfo.specialties);
	}

	private createForm() {
		this.form = new FormGroup<ReferenceMedicalConceptsInformationForm>({
			requestByCareLine: new FormControl({ disabled: true, value: null }),
			careLine: new FormControl({ disabled: true, value: null }),
			referenceType: new FormControl({ disabled: true, value: null }),
			specialties: new FormControl({ disabled: true, value: null }),
			studyCategory: new FormControl({ disabled: true, value: null }),
			practice: new FormControl({ disabled: true, value: null })
		});
	}

}

export interface ReferenceMedicalConceptsInformation {
	careLine: string;
	specialties: string[];
	studyCategory: string;
	practice: string;
	consultation: boolean;
}

interface ReferenceMedicalConceptsInformationForm {
	requestByCareLine: FormControl<boolean>;
	careLine: FormControl<string>;
	referenceType: FormControl<SearchCriteria>;
	specialties: FormControl<string[]>;
	studyCategory: FormControl<string>;
	practice: FormControl<string>;
}
