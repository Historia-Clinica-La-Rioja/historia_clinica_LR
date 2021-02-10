import {Component, Input, OnChanges, OnInit} from '@angular/core';
import {MatDialog} from '@angular/material/dialog';

@Component({
	selector: 'app-external-summary-card',
	templateUrl: './external-summary-card.component.html',
	styleUrls: ['./external-summary-card.component.scss']
})
export class ExternalSummaryCardComponent implements OnInit, OnChanges {

	@Input() header: string;
	minimized: boolean = false;

	constructor(
		public dialog: MatDialog
	) { }

	ngOnInit(): void {}

	ngOnChanges() {
		this.minimized = false;
	}

	toggle(): void {
		this.minimized = !this.minimized;
	}
}
