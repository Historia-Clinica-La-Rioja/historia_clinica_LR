import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { ERole } from '@api-rest/api-model';

@Component({
	selector: 'app-summary-card',
	templateUrl: './summary-card.component.html',
	styleUrls: ['./summary-card.component.scss']
})
export class SummaryCardComponent implements OnInit {

	@Input() header: SummaryHeader;
	@Input() tooltip: string;
	@Input() canEdit: ERole[] = [];
	@Input() editable: boolean = false;
	@Input() hasCovidAlert: boolean = false;
	@Output() openInNew = new EventEmitter();
	@Output() covidAlertEvent = new EventEmitter();

	constructor(
		public dialog: MatDialog
	) { }

	ngOnInit(): void { }

}

export interface SummaryHeader {
	title: string;
	matIcon: string;
}
