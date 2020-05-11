import { Component, OnInit, Output, EventEmitter, Input } from '@angular/core';
import { HealthConditionDto, SnomedDto } from '@api-rest/api-model';
import { FormBuilder, FormGroup } from '@angular/forms';

@Component({
	selector: 'app-diagnostico-principal',
	templateUrl: './diagnostico-principal.component.html',
	styleUrls: ['./diagnostico-principal.component.scss']
})
export class DiagnosticoPrincipalComponent implements OnInit {

	@Output()
	onChange = new EventEmitter();

	@Input()
	diagnosis: HealthConditionDto;

	form: FormGroup;

	constructor(
		private formBuilder: FormBuilder,
	) { }

	ngOnInit(): void {
		this.form = this.formBuilder.group({
			snomed: [null]
		});
	}

	setConcept(selectedConcept: SnomedDto): void {
		let pt = selectedConcept ? selectedConcept.pt : '';
		this.form.controls.snomed.setValue(pt);
		let newMainDiagnosis: HealthConditionDto = {
			id: null,
			verificationId: null,
			statusId: null,
			snomed: selectedConcept
		};
		this.onChange.emit(newMainDiagnosis);
	}

	resetForm(): void {
		delete this.diagnosis;
		this.form.reset();
	}

}
