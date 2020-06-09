import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';

@Component({
	selector: 'app-summary-card',
	templateUrl: './summary-card.component.html',
	styleUrls: ['./summary-card.component.scss']
})
export class SummaryCardComponent implements OnInit {

	@Input() header: SummaryHeader;
	@Output() openInNew = new EventEmitter();

	constructor(
		public dialog: MatDialog
	) { }

	ngOnInit(): void { }

	click() {
		this.openInNew.emit();
	}
}

export interface SummaryHeader {
	title: string;
	matIcon: string;
}
