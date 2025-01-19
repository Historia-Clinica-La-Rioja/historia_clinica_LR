import { Component, Input } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';

@Component({
	selector: 'app-home-institution-information',
	templateUrl: './home-institution-information.component.html',
	styleUrls: ['./home-institution-information.component.scss']
})
export class HomeInstitutionInformationComponent {

	form: FormGroup<HomeInstitutionInformationForm>;
	@Input()
	set homeInstitutionInfo(homeInstitutionInfo: HomeInstitutionInformation) {
		this.setFormValues(homeInstitutionInfo);
	}

	constructor() { 
		this.createForm();
	}

	private createForm() {
		this.form = new FormGroup<HomeInstitutionInformationForm>({
			province: new FormControl({ disabled: true, value: null }),
			department: new FormControl({ disabled: true, value: null }),
			institution: new FormControl({ disabled: true, value: null }),
		});
	}

	private setFormValues(homeInstitutionInfo: HomeInstitutionInformation) {
		const controls = this.form.controls;
		controls.province.setValue(homeInstitutionInfo.province);
		controls.department.setValue(homeInstitutionInfo.department);
		controls.institution.setValue(homeInstitutionInfo.institution);
	}

}

export interface HomeInstitutionInformation {
	province: string;
	department: string;
	institution: string;
}

interface HomeInstitutionInformationForm {
	province: FormControl<string>;
	department: FormControl<string>;
	institution: FormControl<string>;
}
