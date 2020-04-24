import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';

@Component({
	selector: 'app-anamnesis-form',
	templateUrl: './anamnesis-form.component.html',
	styleUrls: ['./anamnesis-form.component.scss']
})
export class AnamnesisFormComponent implements OnInit {

	public form: FormGroup;

	constructor(
		private formBuilder: FormBuilder
	) {	}

	ngOnInit(): void {
		this.form = this.formBuilder.group({
			current_disease: [null],
			physical_examination: [null],
			studies_procedures: [null],
			patient_progress: [null],
			clinical_impression: [null],
			others: [null],
		});
	}

	save(): void {
		console.log('form: ', this.form);
	}

	back(): void {
		window.history.back();
	}

}
