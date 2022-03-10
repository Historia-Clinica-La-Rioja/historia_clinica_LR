import { Component, Input, ViewChild, Injectable, OnChanges } from '@angular/core';
import { MatTableDataSource } from '@angular/material/table';
import { TableService } from '@core/services/table.service';
import { MatPaginator, MatPaginatorIntl } from '@angular/material/paginator';
import { ThemePalette } from '@angular/material/core';

@Component({
	selector: 'app-table',
	templateUrl: './table.component.html',
	styleUrls: ['./table.component.scss']
})
export class TableComponent implements OnChanges {

	ActionDisplays = ActionDisplays;
	TableStyles = TableStyles;

	@Input() model: TableModel<any>;
	@Input() mainStyle: TableStyle = TableStyles.DEFAULT;
	@Input() showTableHeader = true;
	@ViewChild(MatPaginator) set matPaginator(paginator: MatPaginator) {
		this.dataSource.paginator = paginator;
	}


	dataSource = new MatTableDataSource<any>([]);

	columns: ColumnModel<any>[] = [];
	displayedColumns: string[] = [];
	filterEnabled = false;
	paginationEnabled = false;
	pageSizeOptions: number[];

	constructor(
		private readonly tableService: TableService
	) { }

	ngOnChanges() {
		this.setUpTable();
	}

	private setUpTable() {
		this.pageSizeOptions = [];
		if (this.model) {
			const unrepeatedSizeOptions = [...new Set([...PAGE_SIZE_OPTIONS, this.model.data.length])];
			const pageSizeOptions = unrepeatedSizeOptions.filter(opt => this.betweenLimits(opt));
			pageSizeOptions.forEach(e => (e < 5) ? this.pageSizeOptions.push(5) : this.pageSizeOptions.push(e));
			this.columns = this.model.columns;
			this.dataSource.data = this.model.data;
			this.displayedColumns = this.columns?.map(c => c.columnDef);

			// filtering
			this.filterEnabled = this.model.enableFilter;
			this.dataSource.filterPredicate = this.tableService.predicateFilter;

			// pagination
			this.paginationEnabled = this.model.enablePagination;
		}

	}

	betweenLimits(opt: number): boolean {
		return opt <= this.model.data.length && opt <= PAGE_MAX_SIZE;
	}

	applyFilter(event: Event) {
		const filterValue = (event.target as HTMLInputElement).value;
		this.dataSource.filter = filterValue.trim().toLowerCase();
	}
}


const PAGE_SIZE_OPTIONS = [5, 10, 50];
const PAGE_MAX_SIZE = 50;

export enum TableStyles {
	PRIMARY = 'primary',
	SECONDARY = 'secondary',
	DEFAULT = 'default',
}

export enum ActionDisplays {
	ICON = 'icon',
	BUTTON = 'button',
	CHECKBOX = 'checkbox'
}

export type TableStyle = TableStyles.PRIMARY | TableStyles.SECONDARY | TableStyles.DEFAULT;
export type ActionDisplay = ActionDisplays.BUTTON | ActionDisplays.ICON | ActionDisplays.CHECKBOX;;

export interface ColumnModel<T> {
	columnDef: string;
	header?: string;
	text?: (row: T) => string | number;
	action?: {
		displayType: ActionDisplay,
		display?: string,
		matColor?: ThemePalette,
		do?: (row: T) => void,
		hide?: (row: T) => boolean,
		check?: (row: T, boolean) => void,
		isChecked?: (row: T) => boolean,
	};
}

export interface TableModel<T> {
	columns: ColumnModel<T>[];
	data: T[];
	enableFilter?: boolean;
	enablePagination?: boolean;
}

@Injectable()
export class MatPaginatorIntlAR extends MatPaginatorIntl {
	itemsPerPageLabel = 'Items por página';
	nextPageLabel = 'Siguiente';
	previousPageLabel = 'Anterior';
	lastPageLabel = 'Última página';
	firstPageLabel = 'Primera página';

	getRangeLabel = (page, pageSize, length) => {
		if (pageSize < 5) pageSize = 5;
		if (length === 0 || pageSize === 0) {
			return '0 de ' + length;
		}
		length = Math.max(length, 0);
		const startIndex = page * pageSize;
		const endIndex = startIndex < length ?
			Math.min(startIndex + pageSize, length) :
			startIndex + pageSize;
		return startIndex + 1 + ' - ' + endIndex + ' de ' + length;
	}

}

