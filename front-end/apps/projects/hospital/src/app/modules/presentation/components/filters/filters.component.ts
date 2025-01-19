import { Component, EventEmitter, Input, Output } from '@angular/core';
import { TypeaheadFilterOptions } from '../typeahead-filter-options/typeahead-filter-options.component';
import { Option } from '@presentation/components/filters-select/filters-select.component';

@Component({
	selector: 'app-filters',
	templateUrl: './filters.component.html',
	styleUrls: ['./filters.component.scss']
})
export class FiltersComponent {

	private searchCriteria: SelectedFilters;
	isFilterExpanded = false;

	@Input() filters: FiltersType;
	@Output() selectedFilters = new EventEmitter<SelectedFilters>();

	constructor() { }

	setSelectResults(newFilters: SelectedFilterOption[]) {
		this.searchCriteria = {
			...this.searchCriteria,
			select: newFilters
		}
	}

	setTypeaheadResults(newFilters: SelectedFilterOption[]) {
		this.searchCriteria = {
			...this.searchCriteria,
			typeahead: newFilters
		}
	}

	emitResult() {
		this.selectedFilters.emit(this.searchCriteria);
	}

}

export interface FiltersType {
	selects?: Filter[];
	typeaheads?: FilterTypeahead[];
}

export interface FilterTypeahead {
	name: string;
	typeaheadOption: TypeaheadFilterOptions<any>;
}

export interface SelectedFilterOption {
	key: string;
	value: any;
}

export interface SelectedFilters {
	select?: SelectedFilterOption[];
	typeahead?: SelectedFilterOption[];
}

export interface Filter {
	key: string,
	name: string,
	options: Option[],
	isMultiple?: boolean,
	defaultValue?: any[],
}
