import { Component, Input } from '@angular/core';

@Component({
	selector: 'app-associated-parameterized-form-information',
	templateUrl: './associated-parameterized-form-information.component.html',
	styleUrls: ['./associated-parameterized-form-information.component.scss']
})
export class AssociatedParameterizedFormInformationComponent {

	parametersInformationMap = new Map<string, string>();
	parametersMapKeys: string[] = [];
	formName = '';

	@Input() set associatedParameterizedForm(associatedParameterizedForm: AssociatedParameterizedFormInformation) {
		this.formName = associatedParameterizedForm.name;
		this.parametersInformationMap = associatedParameterizedForm.parameters;
		this.parametersMapKeys = Array.from(this.parametersInformationMap.keys());
	};

	constructor() { }

}

export interface AssociatedParameterizedFormInformation {
	name: string;
	parameters: Map<string, string>;
}
