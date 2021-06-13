import { SelectionModel } from '@angular/cdk/collections';

export interface BasicTable<T> {
	data: T[];
	columns: BasicColumn<T>[];
	displayedColumns: string[];
}

export interface TableCheckbox<T> extends BasicTable<T> {
	selection: SelectionModel<T>;
}

export interface BasicColumn<T> {
	def: string;
	header?: string;
	display: (row: T, index?: number) => string | number;
}
