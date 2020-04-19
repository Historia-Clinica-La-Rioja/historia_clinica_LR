import { Injectable } from '@angular/core';

@Injectable({
	providedIn: 'root'
})
export class TableService {

	constructor() { }

	predicateFilter = (data, filter: string) => {
		const accumulator = (currentTerm, key) => {
			return this.nestedFilterCheck(currentTerm, data, key);
		};
		const dataStr = Object.keys(data).reduce(accumulator, '').toLowerCase();
		// Transform the filter by converting it to lowercase and removing whitespace.
		const transformedFilter = filter.trim().toLowerCase();
		return dataStr.indexOf(transformedFilter) !== -1;
	};

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
