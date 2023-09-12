import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, UntypedFormGroup } from '@angular/forms';

@Component({
	selector: 'app-filters-select',
	templateUrl: './filters-select.component.html',
	styleUrls: ['./filters-select.component.scss']
})
export class FiltersSelectComponent implements OnInit {
	isFilterExpanded: boolean = false;
	filters: filter[] = [{ key:'institution',name: 'Institución', options: [1, 2, 3, 4] }, {key:'speciality', name: 'Especialidad', options: [9, 8, 7] }]
	filterForm: UntypedFormGroup;

	constructor() { }

	ngOnInit(): void {
		this.filterForm = this.toFormGroup(this.filters);
	}

	toggleFilter(value: boolean) {
		this.isFilterExpanded = value;
	}
	cleanStatuses() {
		//this.filterForm.controls.status.setValue(null);
	}

	toFormGroup(filters: filter[]) {
		const group: any = {};

		filters.forEach(filter => {
			group[filter.key] = new FormControl(new FormControl(filter.key || ''))
		});
		return new FormGroup(group);
	}

}
export interface filter {
	key:string,
	name: string,
	options: any[]

}
