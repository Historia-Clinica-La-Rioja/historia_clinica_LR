import {Component, Input, OnChanges } from '@angular/core';
import {MatDialog} from '@angular/material/dialog';

@Component({
	selector: 'app-external-summary-card',
	templateUrl: './external-summary-card.component.html',
	styleUrls: ['./external-summary-card.component.scss']
})
export class ExternalSummaryCardComponent implements OnChanges {

	@Input() header: string;
	minimized = false;

	constructor(
		public dialog: MatDialog
	) { }

	ngOnChanges() {
		this.minimized = false;
	}

	toggle(): void {
		this.minimized = !this.minimized;
	}
}
