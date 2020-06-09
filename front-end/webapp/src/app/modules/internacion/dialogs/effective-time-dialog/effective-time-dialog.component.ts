import { Component, OnInit, Inject } from '@angular/core';
import { FormGroup, Validators, FormBuilder, FormControl, ValidationErrors } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { Moment } from 'moment';
import { newMoment, momentFormat, DateFormat } from '@core/utils/moment.utils';
import { hasError } from '@core/utils/form.utils';

function futureTimeValidation(control: FormControl): ValidationErrors | null {
	// TODO move to a different file for reuse
	let time: string = control.value;
	let today: Moment = newMoment();
	// Este chequeo se hace para evitar que no tire el mensaje de error del pattern junto con este.
	if (time.match('([0-1]{1}[0-9]{1}|20|21|22|23):[0-5]{1}[0-9]{1}')) {
		if (time > momentFormat(today, DateFormat.HOUR_MINUTE)) {
			return {
				futureTime: true
			}
		}
	}
	return null;
}
@Component({
	selector: 'app-effective-time-dialog',
	templateUrl: './effective-time-dialog.component.html',
	styleUrls: ['./effective-time-dialog.component.scss']
})
export class EffectiveTimeDialogComponent implements OnInit {

	timeForm: FormGroup;
	today: Moment = newMoment();

	hasError = hasError;

	constructor(
		@Inject(MAT_DIALOG_DATA) public data: { datetime: Moment },
		public dialogRef: MatDialogRef<EffectiveTimeDialogComponent>,
		private formBuilder: FormBuilder,
	) { }

	ngOnInit(): void {
		this.timeForm = this.formBuilder.group({
			date: [this.data.datetime, Validators.required],
			time: [momentFormat(this.data.datetime, DateFormat.HOUR_MINUTE), [Validators.required, futureTimeValidation]],
		});
	}

	set(): void {
		if (this.timeForm.valid) {
			const newTime: string = this.timeForm.controls['time'].value;
			const newDatetime: Moment = this.timeForm.controls['date'].value;
			newDatetime.set({
				hour: +newTime.substr(0, 2),
				minute: +newTime.substr(3, 2),
				second: 0,
				millisecond: 0,
			});
			this.dialogRef.close(newDatetime);
		}
	}

}
