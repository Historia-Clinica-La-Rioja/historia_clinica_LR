import {AfterViewInit, Component, Input, OnInit, TemplateRef, ViewChild} from '@angular/core';
import {CellTemplatesComponent} from '@presentation/components/cell-templates/cell-templates.component';
import {Subject} from 'rxjs';

@Component({
	selector: 'app-document-section-table',
	templateUrl: './document-section-table.component.html',
	styleUrls: ['./document-section-table.component.scss']
})
export class DocumentSectionTableComponent implements OnInit, AfterViewInit {

	@ViewChild('cellTemplates', {static: true}) cellTemplates: CellTemplatesComponent;

	@Input() tableTitle: string;
	@Input() columns: TableColumnConfig[];
	@Input() data: any[];

	private readonly sub = new Subject<TableColumnConfig[]>();

	displayedColumns: string[] = [];

	ngOnInit(): void {
		this.displayedColumns = this.columns?.map(c => c.def);
	}

	ngAfterViewInit(): void {
		this.showMessageSuccess();
	}

	showMessageSuccess(){
		const that = this;

		setTimeout(() => {
			that.sub.next(that.columns);
		}, 100);

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
	templateName?: string;
}
