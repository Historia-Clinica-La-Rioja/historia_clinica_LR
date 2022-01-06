import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { ManualClassificationDto } from '@api-rest/api-model';

@Component({
	selector: 'app-epidemiological-report',
	templateUrl: './epidemiological-report.component.html',
	styleUrls: ['./epidemiological-report.component.scss']
})
export class EpidemiologicalReportComponent implements OnInit {

	form: FormGroup;

	constructor(
		@Inject(MAT_DIALOG_DATA) public data: { problemName: string, manualClassificationList: ManualClassificationDto[] },
		private dialogRef: MatDialogRef<EpidemiologicalReportComponent>,
		private readonly formBuilder: FormBuilder
	) { }

	ngOnInit(): void {
		this.form = this.formBuilder.group({
			manualClassification: [null, Validators.required]
		});
	}

	reportProblem(classificationId: number): void {
		if (this.form.valid) {
			const result: EpidemiologicalManualClassificationResult = {
				isReportable: true,
				manualClassification: this.findClassification(classificationId)
			}
			this.dialogRef.close(result);
		}
	}

	doNotReportProblem(): void {
		const result: EpidemiologicalManualClassificationResult = {
			isReportable: false
		}
		this.dialogRef.close(result);
	}

	goBack(): void {
		const result: EpidemiologicalManualClassificationResult = {
			isReportable: null
		}
		this.dialogRef.close(result);
	}

	private findClassification(id: number): ManualClassificationDto {
		return this.data.manualClassificationList.find(manualClassification => manualClassification.id === id);
	}

}

export interface EpidemiologicalManualClassificationResult {
	isReportable: boolean | null;
	manualClassification?: ManualClassificationDto;
}
