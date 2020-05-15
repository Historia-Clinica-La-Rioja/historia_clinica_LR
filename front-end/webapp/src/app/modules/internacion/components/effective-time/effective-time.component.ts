import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { newMoment } from '@core/utils/moment.utils';
import { Moment } from 'moment';
import { MatDialog } from '@angular/material/dialog';
import { EffectiveTimeDialogComponent } from '../../dialogs/effective-time-dialog/effective-time-dialog.component';

@Component({
	selector: 'app-effective-time',
	templateUrl: './effective-time.component.html',
	styleUrls: ['./effective-time.component.scss']
})
export class EffectiveTimeComponent implements OnInit {

	@Input('model')
	dateObj: Moment = newMoment();

	@Output('onChange')
	update = new EventEmitter();

	constructor(
		public dialog: MatDialog,
	) { }

	ngOnInit(): void {}

	openDialog() {
		const dialogRef = this.dialog.open(EffectiveTimeDialogComponent, {
			disableClose: true,
			data: {
				datetime: this.dateObj.clone()
			}
		});
		dialogRef.afterClosed().subscribe(
			(newDatetime: Moment) => {
				if (newDatetime) {
					this.dateObj = newDatetime;
					this.update.emit(newDatetime);
				}
			}
		);
	}

}
