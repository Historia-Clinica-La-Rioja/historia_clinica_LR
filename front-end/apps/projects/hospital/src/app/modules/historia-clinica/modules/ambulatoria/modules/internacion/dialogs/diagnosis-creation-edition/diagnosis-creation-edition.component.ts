import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { DiagnosisDto, HealthConditionDto, SnomedDto } from '@api-rest/api-model';
import { SnomedECL } from '@api-rest/api-model'
import { SnomedSemanticSearch, SnomedService } from '@historia-clinica/services/snomed.service';

@Component({
	selector: 'app-diagnosis-creation-edition',
	templateUrl: './diagnosis-creation-edition.component.html',
	styleUrls: ['./diagnosis-creation-edition.component.scss']
})
export class DiagnosisCreationEditionComponent implements OnInit {

	snomedError = false;

	form: FormGroup;
	type: string;
	selection = false;
	diagnosis: DiagnosisDto;

	constructor(@Inject(MAT_DIALOG_DATA) public data: any,
		public dialogRef: MatDialogRef<DiagnosisCreationEditionComponent>,
		private formBuilder: FormBuilder,
		private snomedService: SnomedService) {
		this.type = data.type;
		this.diagnosis = data.diagnosis;
	}

	ngOnInit(): void {
		this.form = this.formBuilder.group({
			snomed: [null, Validators.required],
			validation: [false, Validators.required]
		});
		if (this.diagnosis){
			this.form.controls.snomed.setValue(this.diagnosis.snomed.pt);
			this.form.controls.validation.setValue(this.diagnosis.presumptive);
		}
	}

	saveDiagnosis() {
		this.snomedError = false;
		if (this.form.valid) {
			if (this.diagnosis.presumptive != this.form.value.validation) {
				this.diagnosis.presumptive = this.form.value.validation;
				this.diagnosis.isAdded = true;
			}
			this.dialogRef.close(this.diagnosis);
		}
		else
			this.snomedError = true;
	}

	setConcept(selectedConcept: SnomedDto): void {
		let newDiagnosis: HealthConditionDto | DiagnosisDto;
		if (selectedConcept) {
			this.form.controls.snomed.setValue(selectedConcept.pt);
			newDiagnosis = {
				id: null,
				verificationId: null,
				statusId: null,
				snomed: selectedConcept
			};
		}
		this.diagnosis = newDiagnosis;
	}

	openSearchDialog(searchValue: string): void {
		if (searchValue) {
			const search: SnomedSemanticSearch = {
				searchValue,
				eclFilter: SnomedECL.DIAGNOSIS
			};
			this.snomedService.openConceptsSearchDialog(search)
				.subscribe((selectedConcept: SnomedDto) => this.setConcept(selectedConcept));
		}
	}

	resetForm(): void {
		delete this.diagnosis;
		this.snomedError = false;
		this.form.reset( { validation: false });
	}

}
