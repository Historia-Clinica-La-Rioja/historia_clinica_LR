import { Component, EventEmitter, Input, Output } from '@angular/core';
import { UntypedFormBuilder, UntypedFormControl, UntypedFormGroup } from '@angular/forms';
import { MatRadioChange } from '@angular/material/radio';

@Component({
	selector: 'app-search-criteria',
	templateUrl: './search-criteria.component.html',
	styleUrls: ['./search-criteria.component.scss']
})
export class SearchCriteriaComponent {

	form: UntypedFormGroup;
	readonly searchCriteria = SearchCriteria;
	@Input() label: string;
	@Input() searchCriteryStyle?: string;
	@Input()
	set defaultOption(defaultOption: SearchCriteria) {
		if (defaultOption >= 0){
			this.form.controls.criteria.setValue(defaultOption);
		}
	}
	@Input()
	set disabled(disabled: boolean) {
		if(disabled) this.form.controls.criteria.disable();
		else this.form.controls.criteria.enable();
	}
	@Output() selectedOption = new EventEmitter<SearchCriteria>();

	constructor(
		private readonly formBuilder: UntypedFormBuilder,
	) {
		this.form = this.formBuilder.group({
			criteria: new UntypedFormControl(SearchCriteria.CONSULTATION),
		});
	 }

	emit(searchCriteriaValue: MatRadioChange) {
		this.selectedOption.emit(searchCriteriaValue.value);
	}

}

export enum SearchCriteria {
	CONSULTATION,
	PRACTICES
}