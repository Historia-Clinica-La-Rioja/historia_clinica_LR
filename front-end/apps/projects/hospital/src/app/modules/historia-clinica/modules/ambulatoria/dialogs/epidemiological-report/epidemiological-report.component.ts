import { Component, Inject, OnInit } from '@angular/core';
import { FormArray, FormBuilder, FormControl, FormGroup, ValidatorFn } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { SnvsEventManualClassificationsDto } from '@api-rest/api-model';

@Component({
	selector: 'app-epidemiological-report',
	templateUrl: './epidemiological-report.component.html',
	styleUrls: ['./epidemiological-report.component.scss']
})
export class EpidemiologicalReportComponent implements OnInit {

	public form: FormGroup;
	private formArray: FormArray;
	public tryToSubmit = false;

	constructor(
		@Inject(MAT_DIALOG_DATA) public data: { problemName: string, snvsEventManualClassificationsList: SnvsEventManualClassificationsDto[] },
		private dialogRef: MatDialogRef<EpidemiologicalReportComponent>,
		private readonly formBuilder: FormBuilder
	) { }

	ngOnInit(): void {
		this.formArray = this.formBuilder.array([], this.requiredAtLeastOneSelected());
		this.data.snvsEventManualClassificationsList.forEach(_ => this.formArray.push(new FormControl(null)));
		this.form = this.formBuilder.group({
			classifications: this.formArray
		});
	}

	private requiredAtLeastOneSelected(): ValidatorFn {
		const validator: ValidatorFn = (formArray: FormArray) => {
			let result = { notSelected: true };
			for (let i = 0; (i < formArray.length) && (result !== null); i++)
				if (formArray.at(i).value !== null)
					result = null;
			return result;
		};
		return validator;
	}

	reportProblem(): void {
		if (this.form.valid) {
			this.tryToSubmit = false;
			let reports = [];
			let controlsArray = this.form.get('classifications') as FormArray;
			for (let i = 0; i < controlsArray.length; i++) {
				if (controlsArray.at(i).value !== null) {
					const report: EpidemiologicalReport = {
						eventId: this.data.snvsEventManualClassificationsList[i].snvsEvent.eventId,
						groupEventId: this.data.snvsEventManualClassificationsList[i].snvsEvent.groupEventId,
						manualClassificationId: controlsArray.at(i).value
					}
					reports.push(report);
				}
			}

			const result: EpidemiologicalManualClassificationResult = {
				reportProblem: true,
				reports: reports
			}
			this.dialogRef.close(result);
		}
		else
			this.tryToSubmit = true;
	}

	doNotReportProblem(): void {
		const result: EpidemiologicalManualClassificationResult = {
			reportProblem: false
		}
		this.dialogRef.close(result);
	}

	goBack(): void {
		this.dialogRef.close(null);
	}

	clear(index: number): void {
		let controlsArray = this.form.get('classifications') as FormArray;
		controlsArray.at(index).setValue(null);
	}

	hadValue(index: number): boolean {
		let controlsArray = this.form.get('classifications') as FormArray;
		return controlsArray.at(index).value;
	}

}

export interface EpidemiologicalManualClassificationResult {
	reportProblem: boolean;
	reports?: EpidemiologicalReport[];
}

export interface EpidemiologicalReport {
	manualClassificationId: number,
	groupEventId: number,
	eventId: number
}
