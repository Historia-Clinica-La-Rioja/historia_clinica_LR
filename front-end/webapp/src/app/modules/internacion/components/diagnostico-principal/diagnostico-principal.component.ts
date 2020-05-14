import { Component, OnInit, Output, EventEmitter, Input } from '@angular/core';
import { HealthConditionDto, SnomedDto } from '@api-rest/api-model';
import { FormBuilder, FormGroup } from '@angular/forms';
import { SEMANTICS_CONFIG } from '../../constants/snomed-semantics';

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
	readonly SEMANTICS_CONFIG = SEMANTICS_CONFIG;

	constructor(
		private readonly formBuilder: FormBuilder,
	) { }

	ngOnInit(): void {
		this.form = this.formBuilder.group({
			snomed: [null]
		});
	}

	setConcept(selectedConcept: SnomedDto): void {
		let newMainDiagnosis: HealthConditionDto;
		if (selectedConcept) {
			this.form.controls.snomed.setValue(selectedConcept.pt);
			newMainDiagnosis = {
				id: null,
				verificationId: null,
				statusId: null,
				snomed: selectedConcept
			};
		}
		this.onChange.emit(newMainDiagnosis);
	}

	resetForm(): void {
		delete this.diagnosis;
		this.form.reset();
	}

}
