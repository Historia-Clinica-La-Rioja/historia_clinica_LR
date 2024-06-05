import { Component, Inject, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { NON_WHITESPACE_REGEX } from '@core/utils/form.utils';

@Component({
	selector: 'app-reason-pop-up',
	templateUrl: './reason-pop-up.component.html',
	styleUrls: ['./reason-pop-up.component.scss']
})
export class ReasonPopUpComponent implements OnInit {

	form: FormGroup<MotiveForm>;

	constructor(
		@Inject(MAT_DIALOG_DATA) public data: ReasonPopupData,
		private readonly dialogRef: MatDialogRef<ReasonPopUpComponent>,
	) { }

	ngOnInit(): void {
		this.form = new FormGroup<MotiveForm>({
			motive: new FormControl(null, [Validators.required, Validators.pattern(NON_WHITESPACE_REGEX)]),
		});
	}

	confirm() {
		if (this.form.valid)
			this.dialogRef.close(this.form.value.motive);
		else
			this.form.markAllAsTouched();
	}

}

interface MotiveForm {
	motive: FormControl<string>;
}

export interface ReasonPopupData {
	title: string;
	subtitle: string;
	placeholder: string;
}
