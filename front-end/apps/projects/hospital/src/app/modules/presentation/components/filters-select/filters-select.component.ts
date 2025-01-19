import { Component, EventEmitter, Input, Output } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import { filter } from 'rxjs';

@Component({
	selector: 'app-filters-select',
	templateUrl: './filters-select.component.html',
	styleUrls: ['./filters-select.component.scss']
})
export class FiltersSelectComponent {
	@Input() set setFilters(filters: filter[]) {
		this.filters = filters;
		this.filterForm = this.toFormGroup(this.filters);
	}
	@Input() filtersSelectStyle?: string;
	@Output() searchCriteria = new EventEmitter();
	filterForm: FormGroup;
	filters: filter[];
	isFilterExpanded: boolean = false;

	constructor() { }

	toggleFilter(value: boolean) {
		this.isFilterExpanded = value;
	}

	cleanStatuses(nameControl:any) {
		this.filterForm.get(nameControl).setValue(null);
	}

	emitSearchCriteria(){
		this.searchCriteria.emit(this.filterForm.value)
	}

	toFormGroup(filters: filter[]) {
		const group: any = {};
		if (filters.length) {
			filters.forEach(filter => {
				group[filter.key] = new FormControl(null)
			});
		}
		return new FormGroup(group);
	}

}
export interface filter {
	key: string,
	name: string,
	options: any[]
}
export interface Option {
	id: any,
	description: string,
}
