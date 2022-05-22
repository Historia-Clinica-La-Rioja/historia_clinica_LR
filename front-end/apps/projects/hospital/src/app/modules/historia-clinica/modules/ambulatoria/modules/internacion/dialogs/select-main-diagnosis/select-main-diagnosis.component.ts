import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { DiagnosisDto, HealthConditionDto } from '@api-rest/api-model';

@Component({
	selector: 'app-select-main-diagnosis',
	templateUrl: './select-main-diagnosis.component.html',
	styleUrls: ['./select-main-diagnosis.component.scss']
})
export class SelectMainDiagnosisComponent implements OnInit {

	currentMainDiagnosis: HealthConditionDto;
	otherDiagnoses: DiagnosisDto[];
	form: FormGroup;

	constructor(@Inject(MAT_DIALOG_DATA) public data: any,
		public dialogRef: MatDialogRef<SelectMainDiagnosisComponent>,
		private formBuilder: FormBuilder) {
			this.currentMainDiagnosis = data.currentMainDiagnosis;
			this.otherDiagnoses = data.otherDiagnoses;
	}

	ngOnInit(): void {
		this.form = this.formBuilder.group({
			diagnoses: [this.currentMainDiagnosis, Validators.required]
		});
	}

	saveNewMainDiagnosis() {
		this.dialogRef.close(this.form.value.diagnoses);
	}

}
