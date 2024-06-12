import { Component, Inject, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { AppFeature, DiagnosisDto, HealthConditionDto, SnomedDto } from '@api-rest/api-model';
import { SnomedECL } from '@api-rest/api-model'
import { FeatureFlagService } from '@core/services/feature-flag.service';
import { SnomedSemanticSearch, SnomedService } from '@historia-clinica/services/snomed.service';
import { HEALTH_CLINICAL_STATUS, HEALTH_VERIFICATIONS } from '../../constants/ids';

@Component({
	selector: 'app-diagnosis-creation-edition',
	templateUrl: './diagnosis-creation-edition.component.html',
	styleUrls: ['./diagnosis-creation-edition.component.scss']
})
export class DiagnosisCreationEditionComponent implements OnInit {

	readonly HEALTH_VERIFICATIONS = HEALTH_VERIFICATIONS;
	readonly eclFilter = SnomedECL.DIAGNOSIS;
	readonly DiagnosisMode = DiagnosisMode;
	isSnomedError = false;
	form: FormGroup<DiagnosisCreationEditForm>;
	mode: DiagnosisMode;
	diagnosis: DiagnosisDto;
	hasPresumptiveOption: boolean;
	searchConceptsLocallyFF = false;

	constructor(
		@Inject(MAT_DIALOG_DATA) public data: DiagnosisCreationEditionData,
		public dialogRef: MatDialogRef<DiagnosisCreationEditionComponent>,
		private readonly featureFlagService: FeatureFlagService,
		private snomedService: SnomedService
	) {
		this.mode = data.diagnosisMode;
		this.diagnosis = data.diagnosis;
		this.hasPresumptiveOption = !data?.isMainDiagnosis;
		this.featureFlagService.isActive(AppFeature.HABILITAR_BUSQUEDA_LOCAL_CONCEPTOS).subscribe(isOn => this.searchConceptsLocallyFF = isOn);
	}

	ngOnInit(): void {
		this.form = new FormGroup<DiagnosisCreationEditForm>({
			snomed: new FormControl(null, Validators.required),
			verificationId: new FormControl(HEALTH_VERIFICATIONS.CONFIRMADO, Validators.required)
		});

		if (this.diagnosis) {
			this.form.controls.snomed.setValue(this.diagnosis.snomed.pt);
			this.form.controls.verificationId.setValue(this.diagnosis.verificationId);
		}
	}

	saveDiagnosis() {
		this.isSnomedError = false;
		if (this.form.valid) {
			this.diagnosis = this.buildDiagnosisDto();
			this.dialogRef.close(this.diagnosis);
		}
		else
			this.isSnomedError = true;
	}

	setConcept(selectedConcept: SnomedDto): void {
		let newDiagnosis: HealthConditionDto | DiagnosisDto;
		if (selectedConcept) {
			this.form.controls.snomed.setValue(selectedConcept.pt);
			newDiagnosis = {
				id: null,
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

	openSearchDialogSnomed(snomed: SnomedDto) {
		if (snomed) {
			this.setConcept(snomed);
		}
	}

	resetForm(): void {
		delete this.diagnosis;
		this.isSnomedError = false;
		this.form.reset({ verificationId: HEALTH_VERIFICATIONS.CONFIRMADO });
	}

	private buildDiagnosisDto(): DiagnosisDto {
		return {
			...this.diagnosis,
			verificationId: this.form.value.verificationId,
			presumptive: this.form.value.verificationId === HEALTH_VERIFICATIONS.PRESUNTIVO,
			isAdded: true,
			statusId: HEALTH_CLINICAL_STATUS.ACTIVO
		}
	}

}

export enum DiagnosisMode {
	CREATION = 'CREATION',
	EDITION = 'EDITION',
}

interface DiagnosisCreationEditionData {
	diagnosisMode: DiagnosisMode;
	isMainDiagnosis: boolean;
	hasPresumtiveMainDiagnosis: boolean;
	diagnosis: DiagnosisDto;
}

interface DiagnosisCreationEditForm {
	snomed: FormControl<string>;
	verificationId: FormControl<string>;
}