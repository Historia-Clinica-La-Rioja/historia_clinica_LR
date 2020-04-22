import { Component, OnInit, Input } from '@angular/core';

@Component({
	selector: 'app-table-simple',
	templateUrl: './table-simple.component.html',
	styleUrls: ['./table-simple.component.scss']
})
export class TableSimpleComponent implements OnInit {

	@Input() model: TableModel<any>;
	@Input() tableClass: string;

	columns: ColumnModel<any>[];
	dataSource: any[];
	displayedColumns: string[];

	constructor() { }

	ngOnInit(): void {
		this.columns = this.model.columns;
		this.dataSource = this.model.data;
		this.displayedColumns = this.columns.map(c => c.columnDef);
	}

}

export interface ColumnModel<T> {
	columnDef: string;
	header?: string;
	text?: (row: T) => string;
	action?: {
		isDelete?: boolean,
		text?: string,
		do: (row: T) => void
	}
}

export interface TableModel<T> {
	columns: ColumnModel<T>[];
	data: T[];
}
