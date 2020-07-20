import { Component, Input, OnInit } from '@angular/core';

@Component({
	selector: 'app-document-section',
	templateUrl: './document-section.component.html',
	styleUrls: ['./document-section.component.scss']
})
export class DocumentSectionComponent implements OnInit {

	@Input() sectionTitle: string;
	@Input() tableTitle: string;
	@Input() columns: ColumnConfig[];
	@Input() data: any[];

	displayedColumns: string[] = [];

	constructor() {

	}

	ngOnInit(): void {
		this.displayedColumns = this.columns?.map(c => c.def);
	}

}

export interface ColumnConfig {
	def: string;
	header?: string;
	text?: (row: any) => string | number;
}
