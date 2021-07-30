import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Moment } from 'moment';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import { newMoment } from '@core/utils/moment.utils';


@Component({
	selector: 'app-aplicar-vacuna-2',
	templateUrl: './aplicar-vacuna-2.component.html',
	styleUrls: ['./aplicar-vacuna-2.component.scss']
})
export class AplicarVacuna2Component implements OnInit {

	doses: any[];
	schemes: any[];
	conditions: any[];
	billableForm: FormGroup;
	today: Moment = newMoment();

	constructor(
		@Inject(MAT_DIALOG_DATA) public data: { patientId: number },
		private readonly formBuilder: FormBuilder,
	) {
	}

	ngOnInit(): void {
		this.billableForm = this.formBuilder.group({
			date: [this.today, Validators.required],
			snomed: [null, Validators.required],
			condition: [null, Validators.required],
			scheme: [null, Validators.required],
			dose: [null, Validators.required],
			lot: [null],
			note: [null]
		});
	}

	chosenYearHandler(newDate: Moment) {
		if (this.billableForm.controls.date.value !== null) {
			const ctrlDate: Moment = this.billableForm.controls.date.value;
			ctrlDate.year(newDate.year());
			this.billableForm.controls.date.setValue(ctrlDate);
		} else {
			this.billableForm.controls.date.setValue(newDate);
		}
	}

	chosenMonthHandler(newDate: Moment) {
		if (this.billableForm.controls.date.value !== null) {
			const ctrlDate: Moment = this.billableForm.controls.date.value;
			ctrlDate.month(newDate.month());
			this.billableForm.controls.date.setValue(ctrlDate);
		} else {
			this.billableForm.controls.date.setValue(newDate);
		}
	}

	submit() { }

}
