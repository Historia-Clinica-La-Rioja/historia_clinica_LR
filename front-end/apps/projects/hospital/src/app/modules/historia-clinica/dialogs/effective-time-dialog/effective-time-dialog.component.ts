import { Component, OnInit, Inject } from '@angular/core';
import { UntypedFormGroup, Validators, UntypedFormBuilder } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { newDate, buildFullDateFromDate } from '@core/utils/moment.utils';
import { hasError, futureTimeValidation } from '@core/utils/form.utils';
import { MIN_DATE, toHourMinute } from "@core/utils/date.utils";
import { isSameDay } from 'date-fns';

@Component({
	selector: 'app-effective-time-dialog',
	templateUrl: './effective-time-dialog.component.html',
	styleUrls: ['./effective-time-dialog.component.scss']
})
export class EffectiveTimeDialogComponent implements OnInit {

	timeForm: UntypedFormGroup;
	today: Date = newDate();
	minDate = MIN_DATE;

	hasError = hasError;

	constructor(
		@Inject(MAT_DIALOG_DATA) public data: { datetime: Date },
		public dialogRef: MatDialogRef<EffectiveTimeDialogComponent>,
		private formBuilder: UntypedFormBuilder,
	) { }

	ngOnInit(): void {
		this.timeForm = this.formBuilder.group({
			date: [this.data.datetime, Validators.required],
			time: [toHourMinute(this.data.datetime), this.getTimeValidators(this.data.datetime, this.today)],
		});

		this.timeForm.controls.date.valueChanges.subscribe(
			(selectedDate: Date) => {
					this.timeForm.controls.time.clearValidators();
					this.timeForm.controls.time.setValidators(this.getTimeValidators(selectedDate,this.today));
					this.timeForm.controls.time.updateValueAndValidity();
			}
		);
	}

	getTimeValidators(setDate: Date, prevDate: Date) {
		return isSameDay(setDate, prevDate) ?  [Validators.required, futureTimeValidation] : [Validators.required]
	}

	dateChanged(date: Date) {
		this.timeForm.controls.date.setValue(date)
	}

	set(): void {
		if (this.timeForm.valid) {
			const newTime: string = this.timeForm.controls.time.value;
			const newDatetime: Date = this.timeForm.controls.date.value;
			const result: Date = buildFullDateFromDate(newTime, newDatetime)
			this.dialogRef.close(result);
		}
	}

}
