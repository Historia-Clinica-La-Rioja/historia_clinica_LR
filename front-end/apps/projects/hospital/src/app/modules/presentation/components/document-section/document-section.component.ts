import { Component, Input, OnInit, EventEmitter, Output } from '@angular/core';

@Component({
	selector: 'app-document-section',
	templateUrl: './document-section.component.html',
	styleUrls: ['./document-section.component.scss']
})
export class DocumentSectionComponent implements OnInit {

	@Input() sectionTitle: string;
	@Input() sectionImportance: string;
	@Input() tableTitle: string;
	@Input() columns: ColumnConfig[];
	@Input() data: any[];
	@Input() reducedHeaderSize = false;
	@Input() addRemoveColumn = false;
	@Output() removeColumn = new EventEmitter();

	displayedColumns: string[] = [];

	constructor() {

	}

	ngOnInit(): void {
		this.displayedColumns = this.addRemoveColumn ? this.columns?.map(c => c.def).concat(['remove']) : this.columns?.map(c => c.def);
	}

}

export interface ColumnConfig {
	def: string;
	header?: string;
	text?: (row: any) => string | number;
}
