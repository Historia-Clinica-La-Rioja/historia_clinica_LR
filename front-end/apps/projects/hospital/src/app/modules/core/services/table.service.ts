import { Injectable } from '@angular/core';
import { SelectionModel } from '@angular/cdk/collections';
import { REMOVE_SUBSTRING_DNI } from '@core/constants/validation-constants';

@Injectable({
	providedIn: 'root'
})
export class TableService {

	constructor() { }

	isAllSelected = (dataSource, selection: SelectionModel<any>) => {
		const numSelected = selection.selected.length;
		const numRows = dataSource.length;
		return numSelected === numRows;
	}

	/** Selects all rows if they are not all selected; otherwise clear selection. */
	masterToggle = (dataSource, selection: SelectionModel<any>) => {
		this.isAllSelected(dataSource, selection) ?
			selection.clear() :
			dataSource.forEach(row => selection.select(row));
	}

	predicateFilter = (data, filter: string) => {
		const accumulator = (currentTerm, key) => {
			return this.nestedFilterCheck(currentTerm, data, key);
		};
		const dataStr = Object.keys(data).reduce(accumulator, '').toLowerCase();
		// Transform the filter by converting it to lowercase and removing whitespace.
		const transformedFilter = filter.trim().toLowerCase().replace(REMOVE_SUBSTRING_DNI, '');
		return dataStr.indexOf(transformedFilter) !== -1;
	}

	private nestedFilterCheck(search, data, key) {
		if (typeof data[key] === 'object') {
			for (const k in data[key]) {
				if (data[key][k] !== null) {
					search = this.nestedFilterCheck(search, data[key], k);
				}
			}
		} else {
			search += data[key];
		}
		return search;
	}

}
