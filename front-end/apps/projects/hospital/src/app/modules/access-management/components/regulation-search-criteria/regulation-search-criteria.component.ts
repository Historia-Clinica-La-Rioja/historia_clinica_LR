import { Component, EventEmitter, Input, Output, SimpleChanges } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import { MatRadioChange } from '@angular/material/radio';
import { SearchCriteria } from '@turnos/components/search-criteria/search-criteria.component';

@Component({
	selector: 'app-regulation-search-criteria',
	templateUrl: './regulation-search-criteria.component.html',
	styleUrls: ['./regulation-search-criteria.component.scss']
})
export class RegulationSearchCriteriaComponent {

	form: FormGroup<RegulationSearchCriteriaForm> = new FormGroup<RegulationSearchCriteriaForm>({
		criteria: new FormControl(RegulationSearchCriteria.CONSULTATION),
	});
	readonly searchCriteria = SearchCriteria;
	@Input() label: string;
	@Input() searchCriteryStyle?: string;
	@Input() defaultOption?: SearchCriteria;
	@Input() disabled = false;
	@Output() selectedOption = new EventEmitter<SearchCriteria>();

	constructor() { }

	ngOnChanges(changes: SimpleChanges) {
		if (this.form) {
			if (changes.defaultOption)
				this.form.controls.criteria.setValue(changes.defaultOption.currentValue);

			if (changes.disabled?.currentValue)
				this.form.controls.criteria.disable();

			else
				this.form.controls.criteria.enable();
		}
	}

	emit(searchCriteriaValue: MatRadioChange) {
		this.selectedOption.emit(searchCriteriaValue.value);
	}

}

export enum RegulationSearchCriteria {
	CONSULTATION,
	PRACTICES
}

interface RegulationSearchCriteriaForm {
	criteria: FormControl<RegulationSearchCriteria>;
}
