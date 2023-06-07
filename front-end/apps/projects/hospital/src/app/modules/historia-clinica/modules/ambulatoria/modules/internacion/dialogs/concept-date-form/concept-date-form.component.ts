import { Component, Inject, OnInit } from '@angular/core';
import { UntypedFormBuilder, UntypedFormGroup, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { SnomedDto } from '@api-rest/api-model';
import { MIN_DATE } from '@core/utils/date.utils';
import { DateFormat, newMoment } from '@core/utils/moment.utils';
import { Moment } from 'moment';

@Component({
	selector: 'app-concept-date-form',
	templateUrl: './concept-date-form.component.html',
	styleUrls: ['./concept-date-form.component.scss']
})
export class ConceptDateFormComponent implements OnInit {

	form: UntypedFormGroup;
	today: Moment = newMoment();
	minDate = MIN_DATE;
	snomedConcept: SnomedDto;
	constructor(
		@Inject(MAT_DIALOG_DATA) public data: Concept,
		private formBuilder: UntypedFormBuilder,
		private dialogRef: MatDialogRef<ConceptDateFormComponent>,
	) { }

	ngOnInit() {

		this.form = this.formBuilder.group({
			date: [null],
			snomed: [this.data.snomedConcept.pt, Validators.required]
		});
	}

	chosenMonthHandler(newDate: Moment) {
		if (this.form.controls.date.value !== null) {
			const ctrlDate: Moment = this.form.controls.date.value;
			ctrlDate.month(newDate.month());
			this.form.controls.date.setValue(ctrlDate);
		} else {
			this.form.controls.date.setValue(newDate);
		}
	}

	chosenYearHandler(newDate: Moment) {
		if (this.form.controls.date.value !== null) {
			const ctrlDate: Moment = this.form.controls.date.value;
			ctrlDate.year(newDate.year());
			this.form.controls.date.setValue(ctrlDate);
		} else {
			this.form.controls.date.setValue(newDate);
		}
	}

	addToList() {
		if (this.form.valid) {
			const concept: Concept = {
				snomedConcept: this.data.snomedConcept,
				data: this.form.value.date ? this.form.value.date.format(DateFormat.API_DATE) : null,
			};
			this.dialogRef.close(concept);
		}
	}

	resetForm() {
		delete this.snomedConcept;
		this.form.reset();
		this.dialogRef.close()
	}

	close() {
		this.dialogRef.close()
	}
}

export interface Concept {
	label?: string,
	add?: string,
	title?: string,
	data: string;//date
	snomedConcept: SnomedDto;
}

