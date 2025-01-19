import { Component, Input, Output, EventEmitter } from '@angular/core';
import { newDate } from '@core/utils/moment.utils';
import { MatDialog } from '@angular/material/dialog';
import { EffectiveTimeDialogComponent } from '../../dialogs/effective-time-dialog/effective-time-dialog.component';

@Component({
	selector: 'app-effective-time',
	templateUrl: './effective-time.component.html',
	styleUrls: ['./effective-time.component.scss']
})
export class EffectiveTimeComponent {

	@Input() effectiveTime: Date = newDate();

	@Output() update = new EventEmitter<Date>();

	constructor(
		public dialog: MatDialog,
	) { }

	openDialog() {
		const dialogRef = this.dialog.open(EffectiveTimeDialogComponent, {
			disableClose: true,
			data: {
				datetime: this.effectiveTime
			}
		});
		dialogRef.afterClosed().subscribe(
			(newDatetime: Date) => {
				if (newDatetime) {
					this.effectiveTime = newDatetime;
					this.update.emit(newDatetime);
				}
			}
		);
	}

}
