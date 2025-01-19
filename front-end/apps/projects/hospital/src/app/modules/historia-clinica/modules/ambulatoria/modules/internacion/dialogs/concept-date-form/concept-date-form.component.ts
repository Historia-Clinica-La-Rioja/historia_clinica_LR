import { Component, Inject, OnInit } from '@angular/core';
import { UntypedFormBuilder, UntypedFormGroup, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { SnomedDto } from '@api-rest/api-model';
import { toApiFormat } from '@api-rest/mapper/date.mapper';
import { MIN_DATE } from '@core/utils/date.utils';

@Component({
	selector: 'app-concept-date-form',
	templateUrl: './concept-date-form.component.html',
	styleUrls: ['./concept-date-form.component.scss']
})
export class ConceptDateFormComponent implements OnInit {

	form: UntypedFormGroup;
	today: Date = new Date();
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

	selectedDate(date: Date) {
		this.form.controls.date.setValue(date)
	}

	addToList() {
		if (this.form.valid) {
			const concept: Concept = {
				snomedConcept: this.data.snomedConcept,
				data: this.form.value.date ?toApiFormat(this.form.value.date) : null,
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

