import { Component, Inject, OnInit } from '@angular/core';
import { UntypedFormBuilder, UntypedFormGroup, Validators } from '@angular/forms';
import { ThemePalette } from '@angular/material/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { fixDate } from '@core/utils/date/format';

@Component({
	selector: 'app-date-picker',
	templateUrl: './date-picker.component.html',
	styleUrls: ['./date-picker.component.scss']
})
export class DatePickerComponent implements OnInit {

	okBottonColor: ThemePalette;
	form: UntypedFormGroup;

	constructor(
		public dialogRef: MatDialogRef<DatePickerComponent>,
		@Inject(MAT_DIALOG_DATA) public data: {
			title: string, okButtonLabel: string,
			cancelButtonLabel: string, okBottonColor?: ThemePalette,
			minDate?, maxDate?,
		},
		private formBuilder: UntypedFormBuilder,
	) { }

	ngOnInit(): void {
		this.okBottonColor = this.data.okBottonColor ? this.data.okBottonColor : 'primary';
		this.form = this.formBuilder.group({
			date: [null, Validators.required]
		});
	}

	confirm(): void {
		if (this.form.valid) {
			const fixSelectedDate = fixDate(this.form.controls.date.value);
			this.dialogRef.close(fixSelectedDate);
		}
	}
}
