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
			this.filterForm = this.toFormGroup();
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

	private toFormGroup(): FormGroup {
		const group: any = {};
		this.filters.forEach(filter => {
			const defaultValues = Array.isArray(filter.defaultValue) ? filter.defaultValue : [filter.defaultValue];
			const values = filter.options.filter(f => {
				return defaultValues.includes(f.id);
			});
			group[filter.key] = new FormControl(values);
		});
		return new FormGroup(group);
	}

	private setSelectedFilters() {
		Object.keys(this.filterForm.controls).forEach(controlName => {

			const controlValue = this.filterForm.controls[controlName].value;

			if (controlValue) {
				const selectedOption = controlValue.length ? this.toSelectedFilterOptions(controlName, controlValue) : this.toSelectedFilterOption(controlName, controlValue);
				const index = this.selectedFilterOptions?.findIndex(selectedFilterOption => selectedFilterOption.key === selectedOption.key);
				if (index >= 0)
					this.selectedFilterOptions[index] = selectedOption;
				else
					this.selectedFilterOptions.push(selectedOption);
			}
		});
	}

	private toSelectedFilterOptions(key: string, values: Option[]): SelectedFilterOption {
		return { key, value: values.map(value => value.id) }
	}

	private toSelectedFilterOption(key: string, value: Option): SelectedFilterOption {
		return { key, value: value.id }
	}
}

