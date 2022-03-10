import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef } from '@angular/material/dialog';

@Component({
	selector: 'app-diet',
	templateUrl: './diet.component.html',
	styleUrls: ['./diet.component.scss']
})

export class DietComponent implements OnInit {
	form: FormGroup;

	constructor(
		private readonly formBuilder: FormBuilder,

		private dialog: MatDialogRef<DietComponent>

	) {

	}
	ngOnInit(): void {
		this.form = this.formBuilder.group({
			description: [null, [Validators.required]],
		});
	}

	save(): void {
		if (this.form.valid)
			this.dialog.close(this.form.value.description);
	}
}
