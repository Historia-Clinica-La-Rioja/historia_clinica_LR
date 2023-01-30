import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
	name: 'paginate'
})
export class PaginatePipe implements PipeTransform {

	transform(items: Item<any>[], currentPage: number, perPage: number, filterSearch?: string): any[] {
		if (filterSearch)
			items = this.filter(filterSearch, items);

		let startIndex = (currentPage - 1) * perPage;
		let endIndex = startIndex + perPage;

		return items.slice(startIndex, endIndex).map(e => e.value);
	}

	private filter(filterSearch: string, items: Item<any>[]): Item<any>[] {
		return items.filter((e) => e?.description.toLowerCase().includes(filterSearch.toLowerCase()));
	}

}

export interface Item<T> {
	description: string;
	value: T;
}
