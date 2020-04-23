import { Component, OnInit, Input } from '@angular/core';
import { MatTableDataSource } from '@angular/material/table';
import { TableService } from '@core/services/table.service';

@Component({
	selector: 'app-table',
	templateUrl: './table.component.html',
	styleUrls: ['./table.component.scss']
})
export class TableComponent implements OnInit {

	@Input() model: TableModel<any>;
	@Input() tableClass: string;

	dataSource = new MatTableDataSource<any>([]);

	columns: ColumnModel<any>[] = [];
	displayedColumns: string[] = [];
	filterEnabled: boolean = false;

	constructor(
		private tableService: TableService
	) { }

	ngOnInit(): void { }

	ngOnChanges() {
		this.setUpTable();
	}

	private setUpTable() {
		this.columns = this.model?.columns;
		this.dataSource.data = this.model?.data;
		this.displayedColumns = this.columns?.map(c => c.columnDef);

		//filtering
		this.filterEnabled = this.model?.enableFilter;
		this.dataSource.filterPredicate = this.tableService.predicateFilter;
	}

	applyFilter(event: Event) {
		const filterValue = (event.target as HTMLInputElement).value;
		this.dataSource.filter = filterValue.trim().toLowerCase();
	}

}

export interface ColumnModel<T> {
	columnDef: string;
	header?: string;
	text?: (row: T) => string | number;
	action?: {
		isDelete?: boolean,
		text?: string,
		do: (row: T) => void
	}
}

export interface TableModel<T> {
	columns: ColumnModel<T>[];
	data: T[];
	enableFilter?: boolean;
}
