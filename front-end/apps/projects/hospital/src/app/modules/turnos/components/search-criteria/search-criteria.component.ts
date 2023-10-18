import { Component, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChanges } from '@angular/core';
import { UntypedFormBuilder, UntypedFormControl, UntypedFormGroup } from '@angular/forms';
import { MatRadioChange } from '@angular/material/radio';

@Component({
	selector: 'app-search-criteria',
	templateUrl: './search-criteria.component.html',
	styleUrls: ['./search-criteria.component.scss']
})
export class SearchCriteriaComponent implements OnInit, OnChanges {

	form: UntypedFormGroup;
	readonly searchCriteria = SearchCriteria;
	@Input() label: string;
	@Input() searchCriteryStyle?: string;
	@Input() defaultOption?: SearchCriteria;
	@Input() disabled = false;
	@Output() selectedOption = new EventEmitter<SearchCriteria>();

	constructor(
		private readonly formBuilder: UntypedFormBuilder,
	) { }

	ngOnInit() {

		this.form = this.formBuilder.group({
			criteria: new UntypedFormControl(SearchCriteria.CONSULTATION),
		});

	}

	ngOnChanges(changes: SimpleChanges) {
		if (this.form) {
			if (changes.defaultOption)
				this.form.controls.criteria.setValue(changes.defaultOption.currentValue);

			if (changes.disabled.currentValue)
				this.form.controls.criteria.disable();
			else
				this.form.controls.criteria.enable();
		}
	}

	emit(searchCriteriaValue: MatRadioChange) {
		this.selectedOption.emit(searchCriteriaValue.value);
	}

}

export enum SearchCriteria {
	CONSULTATION,
	PRACTICES
}