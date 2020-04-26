import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MasterDataInterface } from '@api-rest/api-model';

@Component({
	selector: 'app-anamnesis-form',
	templateUrl: './anamnesis-form.component.html',
	styleUrls: ['./anamnesis-form.component.scss']
})
export class AnamnesisFormComponent implements OnInit {

	public form: FormGroup;

	bloodTypes: MasterDataInterface<string>[] = [{id: '1', description: 'MasterData Example 1'}, {id: '2', description: 'MasterData Example 2'}, {id: '3', description: 'MasterData Example 3'}];

	constructor(
		private formBuilder: FormBuilder
	) {	}

	ngOnInit(): void {
		this.form = this.formBuilder.group({
			bloodType: [null, Validators.required],
			height: [null, Validators.required],
			weight: [null, Validators.required],
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
