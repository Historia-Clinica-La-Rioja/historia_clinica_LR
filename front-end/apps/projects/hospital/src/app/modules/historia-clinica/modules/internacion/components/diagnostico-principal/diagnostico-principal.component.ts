import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { HealthConditionDto, SnomedDto } from '@api-rest/api-model';
import { FormBuilder, FormGroup } from '@angular/forms';
import { SEMANTICS_CONFIG } from '../../../../constants/snomed-semantics';
import { SnomedSemanticSearch, SnomedService } from '../../../../services/snomed.service';

@Component({
	selector: 'app-diagnostico-principal',
	templateUrl: './diagnostico-principal.component.html',
	styleUrls: ['./diagnostico-principal.component.scss']
})
export class DiagnosticoPrincipalComponent implements OnInit {

	@Output()
	changeMainDiagnosis = new EventEmitter();

	@Input()
	diagnosis: HealthConditionDto;

	form: FormGroup;
	readonly SEMANTICS_CONFIG = SEMANTICS_CONFIG;

	constructor(
		private readonly formBuilder: FormBuilder,
		private snomedService: SnomedService
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
		this.changeMainDiagnosis.emit(newMainDiagnosis);
	}

	resetForm(): void {
		delete this.diagnosis;
		this.form.reset();
		this.changeMainDiagnosis.emit();
	}

	openSearchDialog(searchValue: string): void {
		if (searchValue) {
			const search: SnomedSemanticSearch = {
				searchValue,
				eclFilter: this.SEMANTICS_CONFIG.diagnosis
			};
			this.snomedService.openConceptsSearchDialog(search)
				.subscribe((selectedConcept: SnomedDto) => this.setConcept(selectedConcept));
		}
	}
}
