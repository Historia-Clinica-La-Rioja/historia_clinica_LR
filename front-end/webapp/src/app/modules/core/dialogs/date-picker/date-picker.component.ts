import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ThemePalette } from '@angular/material/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';

@Component({
	selector: 'app-date-picker',
	templateUrl: './date-picker.component.html',
	styleUrls: ['./date-picker.component.scss']
})
export class DatePickerComponent implements OnInit {

	okBottonColor: ThemePalette;
	form: FormGroup;

	constructor(
		public dialogRef: MatDialogRef<DatePickerComponent>,
		@Inject(MAT_DIALOG_DATA) public data: {
			title: string, okButtonLabel: string,
			cancelButtonLabel: string, okBottonColor?: ThemePalette,
			minDate?, maxDate?,
		},
		private formBuilder: FormBuilder,
	) { }

	ngOnInit(): void {
		this.okBottonColor = this.data.okBottonColor ? this.data.okBottonColor : 'primary';
		this.form = this.formBuilder.group({
			date: [null, Validators.required]
		});
	}

	confirm(): void {
		if (this.form.valid) {
			this.dialogRef.close(this.form.controls.date.value);
		}
	}
}
