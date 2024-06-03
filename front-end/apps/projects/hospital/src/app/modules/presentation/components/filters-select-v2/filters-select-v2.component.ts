import { Component, EventEmitter, Input, Output } from '@angular/core';
import { FormGroup, FormControl } from '@angular/forms';
import { Filter, SelectedFilterOption } from '../filters/filters.component';
import { Option } from '../filters-select/filters-select.component';

@Component({
	selector: 'app-filters-select-v2',
	templateUrl: './filters-select-v2.component.html',
	styleUrls: ['./filters-select-v2.component.scss']
})
export class FiltersSelectV2Component {

	filterForm: FormGroup;
	filters: Filter[] = [];
	selectedFilterOptions: SelectedFilterOption[] = [];

	@Input() set setFilters(filters: Filter[]) {
		if (filters.length) {
			this.filters = filters;
			this.filterForm = this.toFormGroup(this.filters);
		}
	}
	@Output() selectedFilters = new EventEmitter<SelectedFilterOption[]>();

	constructor() { }

	clear(nameControl: string) {
		this.filterForm.get(nameControl).setValue(null);
		this.selectedFilterOptions = this.selectedFilterOptions.filter(filterOption => filterOption.key !== nameControl);
		this.selectedFilters.emit(this.selectedFilterOptions);
	}

	emitSearchCriteria() {
		this.setSelectedFilters();
		this.selectedFilters.emit(this.selectedFilterOptions);
	}

	private toFormGroup(filters: Filter[]): FormGroup {
		const group: any = {};
		filters.forEach(filter => group[filter.key] = new FormControl(null));
		return new FormGroup(group);
	}

	private setSelectedFilters() {
		Object.keys(this.filterForm.controls).forEach(controlName => {

			const controlValue = this.filterForm.controls[controlName].value;

			if (controlValue) {
				const selectedOption = this.toSelectedFilterOption(controlName, controlValue);
				const index = this.selectedFilterOptions?.findIndex(selectedFilterOption => selectedFilterOption.key === selectedOption.key);
				if (index >= 0)
					this.selectedFilterOptions[index] = selectedOption;
				else
					this.selectedFilterOptions.push(selectedOption);
			}
		});
	}

	private toSelectedFilterOption(key: string, values: Option[]): SelectedFilterOption {
		return { key, value: values.map(value => value.id) }
	}
}

