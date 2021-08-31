import { Component, Input, OnInit, TemplateRef, ViewChild} from '@angular/core';
import {CellTemplatesComponent} from '@presentation/components/cell-templates/cell-templates.component';

@Component({
	selector: 'app-document-section-table',
	templateUrl: './document-section-table.component.html',
	styleUrls: ['./document-section-table.component.scss']
})
export class DocumentSectionTableComponent implements OnInit {

	@ViewChild('cellTemplates', {static: true}) cellTemplates: CellTemplatesComponent;

	@Input() tableTitle: string;
	@Input() columns: TableColumnConfig[];
	@Input() data: any[];

	displayedColumns: string[] = [];

	ngOnInit(): void {
		this.displayedColumns = this.columns?.map(c => c.def);
	}

	getTemplate(ngTemplateName: string): TemplateRef<any> {
		return this.cellTemplates.getTemplate(ngTemplateName);
	}

}

export interface TableColumnConfig {
	def: string;
	header?: string;
	text?: (row: any) => string | number;
	action?: (rowIndex: number) => any;
	template?: string;
}
