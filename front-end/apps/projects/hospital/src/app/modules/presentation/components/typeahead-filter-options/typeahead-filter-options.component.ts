import { Component, EventEmitter, Input, Output, SimpleChanges } from '@angular/core';
import { TypeaheadOption } from '../typeahead/typeahead.component';
import { FormControl, FormGroup } from '@angular/forms';
import { MatOptionSelectionChange } from '@angular/material/core';
import { startWith, map } from 'rxjs';
import { SelectedFilterOption } from '../filters/filters.component';

@Component({
	selector: 'app-typeahead-filter-options',
	templateUrl: './typeahead-filter-options.component.html',
	styleUrls: ['./typeahead-filter-options.component.scss']
})
export class TypeaheadFilterOptionsComponent {

	@Input() filterOptions: TypeaheadFilterOptions<any>;
	@Input() titleInput: string;
	@Output() selectedOption = new EventEmitter<SelectedFilterOption>();

	form: FormGroup<SearchValue>;
	optionsFiltered: TypeaheadOption<any>[];

	constructor() {
		this.setFormAndSubscribe();
	}

	ngOnChanges(changes: SimpleChanges) {
		if (changes.filterOptions?.currentValue)
			this.optionsFiltered = this.filterOptions?.options;
	}

	select(event: MatOptionSelectionChange, option: TypeaheadOption<any>) {
		if (event.isUserInput) {
			const selectedOption = { key: this.filterOptions.key, value: option.value };
			this.selectedOption.emit(selectedOption);
		}
	}

	clear(event) {
		this.reset();
		event.stopPropagation();
	}

	private reset() {
		const key = this.filterOptions.key;
		const selectedOptionToClear = { key, value: null };
		this.form.controls.searchValue.reset();
		this.selectedOption.emit(selectedOptionToClear);
	}

	private filter(value: string): TypeaheadOption<any>[] {
		if (!value)
			return this.filterOptions?.options;
		const searchValue = value?.toLowerCase();
		return this.filterOptions?.options.filter(opt => {
			return opt.compareValue.toLowerCase().includes(searchValue);
		});
	}

	private setFormAndSubscribe() {
		this.form = new FormGroup<SearchValue>({
			searchValue: new FormControl(null),
		});

		this.form.controls.searchValue.valueChanges
			.pipe(
				startWith(''),
				map(value => this.filter(value)))
			.subscribe(filtered => this.optionsFiltered = filtered);
	}

}

interface SearchValue {
	searchValue: FormControl<string>;
}

export interface TypeaheadFilterOptions<T> {
	key: string;
	options: TypeaheadOption<T>[];
}
