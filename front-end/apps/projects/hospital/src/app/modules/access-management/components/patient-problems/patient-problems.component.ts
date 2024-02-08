import { Component, Input } from '@angular/core';
import { FormGroup, FormControl } from '@angular/forms';

@Component({
	selector: 'app-patient-problems',
	templateUrl: './patient-problems.component.html',
	styleUrls: ['./patient-problems.component.scss']
})
export class PatientProblemsComponent {

	form = new FormGroup({
		problems: new FormControl<string[]>({ disabled: true, value: null }),
	});

	@Input()
	set problems(problems: string[]) {
		this.form.controls.problems.setValue(problems);
	};

	constructor() { }

}
