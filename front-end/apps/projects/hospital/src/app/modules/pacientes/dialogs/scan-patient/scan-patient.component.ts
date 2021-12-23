import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from "@angular/forms";
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { PatientInformationScan } from "@pacientes/pacientes.model";

const REG_EXP = new RegExp('^[0-9]+$');
const DATA_SPLIT_ENGLISH = '@';
const DATA_SPLIT_SPANISH = '"';
const INVALID_INPUT = 'invalid_input';
@Component({
	selector: 'app-scan-patient',
	templateUrl: './scan-patient.component.html',
	styleUrls: ['./scan-patient.component.scss']
})

export class ScanPatientComponent implements OnInit {


	public formScanPatient: FormGroup;
	patientInformationScan: PatientInformationScan;

	constructor(private formBuilder: FormBuilder, private dialogRef: MatDialogRef<ScanPatientComponent>, @Inject(MAT_DIALOG_DATA) public data: {
		genderOptions: any,
		identifyTypeArray: any
	}) { }

	ngOnInit(): void {
		this.formScanPatient = this.formBuilder.group({
			informationPatient: []
		})
	}

	private checkIdentificationNumber(identificationNumber: string): boolean {
		let identificationWithoutBlankSpaces = identificationNumber.trim();
		return ((REG_EXP.test(identificationWithoutBlankSpaces)) && (identificationWithoutBlankSpaces.length <= 9) && (identificationWithoutBlankSpaces.length > 6));
	}

	private checkGender(gender: string): any {
		for (let i = 0; i < this.data.genderOptions.length; i++) {
			if (this.data.genderOptions[i].description.includes(gender)) {
				return (this.data.genderOptions[i].id);
			}
		}
		return -1;
	}

	public captureInformation(valueForm): void {
		let valueFormArray: string[];
		if (valueForm.target.value.includes(DATA_SPLIT_SPANISH)) {
			valueFormArray = valueForm.target.value.split(DATA_SPLIT_SPANISH);
		}
		else {
			if (valueForm.target.value.includes(DATA_SPLIT_ENGLISH)) {
				valueFormArray = valueForm.target.value.split(DATA_SPLIT_ENGLISH);
			}
		}
		if ((valueFormArray !== undefined) && (valueFormArray.length >= 8)) {
			let identifType = this.data.identifyTypeArray[0].id;
			let identifNumber;
			let gender;
			for (let i = 0; i < valueFormArray.length; i++) {
				if (this.checkIdentificationNumber(valueFormArray[i])) {
					identifNumber = valueFormArray[i].trim();
				}
				else {
					if ((valueFormArray[i].length == 1) && (this.checkGender(valueFormArray[i]) !== -1)) {
						gender = this.checkGender(valueFormArray[i]);
					}
				}
			}
			this.patientInformationScan = { identifType: identifType, identifNumber: identifNumber, gender: gender };
			this.dialogRef.close(this.patientInformationScan);
		}
		else {
			this.dialogRef.close(INVALID_INPUT);
		}
	}

	public closeDialog(): void {
		this.dialogRef.close();
	}
}
