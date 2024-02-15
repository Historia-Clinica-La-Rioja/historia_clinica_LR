import { Component, EventEmitter, Input, Output } from '@angular/core';
import { TypeaheadFilterOptions } from '../typeahead-filter-options/typeahead-filter-options.component';
import { SelectedFilterOption } from '../filters/filters.component';

@Component({
	selector: 'app-filters-typeahead',
	templateUrl: './filters-typeahead.component.html',
	styleUrls: ['./filters-typeahead.component.scss']
})
export class FiltersTypeaheadComponent {

	selectedFilterOptions: SelectedFilterOption[] = [];

	@Input() filters: FilterTypeahead[];
	@Output() selectedFilters = new EventEmitter<SelectedFilterOption[]>();

	constructor() { }

	setSearchCriteria(selectedOption: SelectedFilterOption) {
		selectedOption ? this.setFilterOption(selectedOption) : this.clearOption(selectedOption);
		this.selectedFilters.emit(this.selectedFilterOptions);
	}

	private setFilterOption(option: SelectedFilterOption) {
		const indexOfSelectedOption = this.selectedFilterOptions?.findIndex(filter => filter.key === option.key);

		(indexOfSelectedOption >= 0) ? this.selectedFilterOptions[indexOfSelectedOption] = option : this.selectedFilterOptions.push(option);
	}

	private clearOption(option: SelectedFilterOption) {
		this.selectedFilterOptions = this.selectedFilterOptions.filter(filter => filter.key !== option.key);
	}
}

export interface FilterTypeahead {
	name: string;
	typeaheadOption: TypeaheadFilterOptions<any>;
}
