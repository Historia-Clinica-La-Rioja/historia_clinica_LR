import { Component, EventEmitter, Input, Output } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatMenu } from '@angular/material/menu';
import { ERole } from '@api-rest/api-model';

@Component({
	selector: 'app-summary-card',
	templateUrl: './summary-card.component.html',
	styleUrls: ['./summary-card.component.scss']
})
export class SummaryCardComponent {

	@Input() header: SummaryHeader;
	@Input() tooltip: string;
	@Input() canEdit: ERole[] = [];
	@Input() editable = false;
	@Input() menu: false;
	@Input() matMenu: MatMenu;
	@Output() openInNew = new EventEmitter();

	constructor(
		public dialog: MatDialog
	) { }

}

export interface SummaryHeader {
	title: string;
	matIcon: string;
}
