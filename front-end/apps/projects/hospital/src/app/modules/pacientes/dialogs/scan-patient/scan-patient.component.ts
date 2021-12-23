import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from "@angular/forms";
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { PatientInformationScan } from "@pacientes/pacientes.model";
import { isValid, parse } from 'date-fns';

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
			let birthDate;
			let name;
			let otherNames;
			let lastName;
			let otherLastNames;
			let dateNotFound = true;
			let lastNameNotFound = true;
			let namesNotFound = true;
			for (let i = 0; i < valueFormArray.length; i++) {
				if (this.checkIdentificationNumber(valueFormArray[i])) {
					identifNumber = valueFormArray[i].trim();
				}
				else {
					if ((valueFormArray[i].length == 1) && (this.checkGender(valueFormArray[i]) !== -1)) {
						gender = this.checkGender(valueFormArray[i]);
					}
					else {
						if ((valueFormArray[i].length >= 1) && dateNotFound && this.isDate(valueFormArray[i])) {
							dateNotFound = false;
							birthDate = valueFormArray[i];
						}
						else {
							if ((valueFormArray[i].length >= 1) && lastNameNotFound && this.canBeAName(valueFormArray[i])) {
								lastNameNotFound = false;
								const lastNamesArray = valueFormArray[i].split(' ');
								if (lastNamesArray.length > 0)
									lastName = lastNamesArray[0];
								if (lastNamesArray.length >= 1) {
									otherLastNames = '';
									for (let index = 1; index < lastNamesArray.length; index++) {
										otherLastNames += lastNamesArray[index] + ' ';
									}
									otherLastNames = otherLastNames.trim();
								}
							}
							else if ((valueFormArray[i].length >= 1) && !lastNameNotFound && namesNotFound && this.canBeAName(valueFormArray[i])) {
								namesNotFound = false;
								const namesArray = valueFormArray[i].split(' ');
								if (namesArray.length > 0)
									name = namesArray[0];
								if (namesArray.length >= 1) {
									otherNames = '';
									for (let index = 1; index < namesArray.length; index++) {
										otherNames += namesArray[index] + ' ';
									}
									otherNames = otherNames.trim();
								}
							}
						}
					}
				}
			}
			this.patientInformationScan = { identifType: identifType, identifNumber: identifNumber, gender: gender, birthDate: birthDate, firstName: name, middleNames: otherNames, lastName: lastName, otherLastNames: otherLastNames };
			this.dialogRef.close(this.patientInformationScan);
		}
		else {
			this.dialogRef.close(INVALID_INPUT);
		}
	}


	public closeDialog(): void {
		this.dialogRef.close();
	}

	private isDate(value: string): boolean {
		return isValid(parse(value, 'dd-MM-yyyy', new Date()));
	}

	private canBeAName(value: string): boolean {
		const REGULAR_EXPRESSION = new RegExp("^[a-zA-Z ]+$");
		if (value && value.length && value.length > 1)
			return REGULAR_EXPRESSION.test(value);
		return false;
	}
}
