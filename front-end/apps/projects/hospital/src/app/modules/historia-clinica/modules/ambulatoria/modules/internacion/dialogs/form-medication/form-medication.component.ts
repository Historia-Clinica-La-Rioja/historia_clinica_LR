import { Component, Inject } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MedicationDto, SnomedDto } from '@api-rest/api-model';

@Component({
	selector: 'app-form-medication',
	templateUrl: './form-medication.component.html',
	styleUrls: ['./form-medication.component.scss']
})
export class FormMedicationComponent {

	form: FormGroup;
	snomedConcept: SnomedDto;
	hideSuspended = false;

	constructor(
		@Inject(MAT_DIALOG_DATA) public data: Medication,

		private formBuilder: FormBuilder,

		private dialogRef: MatDialogRef<FormMedicationComponent>,


	) {
		this.hideSuspended = this.data.hideSuspended;
		this.form = this.formBuilder.group({
			note: [null],
			suspended: [false],
			snomed: [this.data.snomedConcept.pt, Validators.required]
		});
	}

	addToList() {
		if (this.form.valid) {
			const medicacion: MedicationDto = {
				note: this.form.value.note,
				snomed: this.data.snomedConcept,
				suspended: this.form.value.suspended
			};
			this.add(medicacion);
			this.resetForm();
		}
	}

	add(medicacion: MedicationDto) {
		this.dialogRef.close(medicacion);
	}

	resetForm() {
		delete this.snomedConcept;
		this.form.reset();
	}

	close() {
		this.dialogRef.close()
	}

}

export interface Medication {
	title: string;
	hideSuspended: boolean;
	snomedConcept: SnomedDto;
}
