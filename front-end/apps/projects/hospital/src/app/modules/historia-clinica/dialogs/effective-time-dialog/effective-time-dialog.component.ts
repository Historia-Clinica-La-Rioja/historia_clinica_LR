import { Component, OnInit, Inject } from '@angular/core';
import { FormGroup, Validators, FormBuilder } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { Moment } from 'moment';
import { newMoment, momentFormat, DateFormat } from '@core/utils/moment.utils';
import { hasError, futureTimeValidation } from '@core/utils/form.utils';
import { MIN_DATE } from "@core/utils/date.utils";

@Component({
	selector: 'app-effective-time-dialog',
	templateUrl: './effective-time-dialog.component.html',
	styleUrls: ['./effective-time-dialog.component.scss']
})
export class EffectiveTimeDialogComponent implements OnInit {

	timeForm: FormGroup;
	today: Moment = newMoment();
	minDate = MIN_DATE;

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

		this.timeForm.controls.date.valueChanges.subscribe(
			(selectedDate: Moment) => {
				const selectedDateString = momentFormat(selectedDate, DateFormat.API_DATE);
				const todayDateString = momentFormat(this.today, DateFormat.API_DATE);
				if (selectedDateString === todayDateString) {
					this.timeForm.controls.time.setValidators([Validators.required, futureTimeValidation]);
					this.timeForm.controls.time.updateValueAndValidity();
				} else {
					this.timeForm.controls.time.setValidators(Validators.required);
					this.timeForm.controls.time.updateValueAndValidity();
				}
			}
		);
	}

	set(): void {
		if (this.timeForm.valid) {
			const newTime: string = this.timeForm.controls.time.value;
			const newDatetime: Moment = this.timeForm.controls.date.value;
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
