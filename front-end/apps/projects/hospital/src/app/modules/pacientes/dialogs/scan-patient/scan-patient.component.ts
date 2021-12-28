import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from "@angular/forms";
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { GenderDto, IdentificationTypeDto } from '@api-rest/api-model';
import { PatientInformationScan } from "@pacientes/pacientes.model";
import { isValid, parse } from 'date-fns';

const DATA_SPLIT_ENGLISH = '@';
const DATA_SPLIT_SPANISH = '"';
const MIN_AMOUNT_DATA_FROM_VALID_SCAN = 8;
export const INVALID_INPUT = 'invalid_input';
@Component({
	selector: 'app-scan-patient',
	templateUrl: './scan-patient.component.html',
	styleUrls: ['./scan-patient.component.scss']
})

export class ScanPatientComponent implements OnInit {

	public formScanPatient: FormGroup;
	private patientInformationScan: PatientInformationScan;

	constructor(
		private formBuilder: FormBuilder,
		private dialogRef: MatDialogRef<ScanPatientComponent>,
		@Inject(MAT_DIALOG_DATA) public data: {
			genderOptions: GenderDto[],
			identifyTypeArray: IdentificationTypeDto[]
		}
	) { }

	ngOnInit(): void {
		this.formScanPatient = this.formBuilder.group({
			informationPatient: []
		})
	}

	private isAnIdentificationNumber(identificationNumber: string): boolean {
		let identificationWithoutBlankSpaces = identificationNumber.trim();
		const REG_EXP = /^\d+$/;
		return ((REG_EXP.test(identificationWithoutBlankSpaces)) && (identificationWithoutBlankSpaces.length <= 9) && (identificationWithoutBlankSpaces.length > 6));
	}

	private getGenderId(gender: string): number {
		const genderOption = this.data.genderOptions.find(option => option.description.includes(gender));
		return genderOption ? genderOption.id : -1;
	}

	public captureInformation(valueForm): void {

		let valueFormArray: string[];
		if (valueForm.target.value.includes(DATA_SPLIT_SPANISH))
			valueFormArray = valueForm.target.value.split(DATA_SPLIT_SPANISH);
		else if (valueForm.target.value.includes(DATA_SPLIT_ENGLISH))
			valueFormArray = valueForm.target.value.split(DATA_SPLIT_ENGLISH);

		if (valueFormArray?.length >= MIN_AMOUNT_DATA_FROM_VALID_SCAN) {
			let identifType = this.data.identifyTypeArray[0].id;
			let identifNumber: string;
			let gender: number;
			let birthDate: string;
			let dateFound = false;
			let firstName: string;
			let middleNames: string;
			let namesFound = false;
			let lastName: string;
			let otherLastNames: string;
			let lastNameFound = false;
			for (let valueStr of valueFormArray) {
				if (this.isAnIdentificationNumber(valueStr))
					identifNumber = valueStr.trim();
				else if (valueStr.length === 1) {
					let genderId = this.getGenderId(valueStr);
					if (genderId !== -1) gender = genderId;
				}
				else if (valueStr.length > 1) {
					if (!dateFound && this.isDate(valueStr)) {
						dateFound = true;
						birthDate = valueStr.split('/').join('-');
					}
					else {
						const canBeAName = this.canBeAName(valueStr);
						if (!lastNameFound && canBeAName) {
							lastNameFound = true;
							const lastNames = this.splitNames(valueStr);
							lastName = lastNames.first;
							otherLastNames = lastNames.second;
						}
						else if (lastNameFound && !namesFound && canBeAName) {
							namesFound = true;
							const names = this.splitNames(valueStr);
							firstName = names.first;
							middleNames = names.second;
						}
					}
				}
			}
			this.patientInformationScan = { identifType, identifNumber, gender, birthDate, firstName, middleNames, lastName, otherLastNames };
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
		let valueDate = value.split('/').join('-');
		return isValid(parse(valueDate, 'dd-MM-yyyy', new Date()));
	}

	private canBeAName(value: string): boolean {
		const REGULAR_EXPRESSION = /^[a-zA-Z '´]+$/;
		return REGULAR_EXPRESSION.test(value);
	}

	private splitNames(names: string): Names {
		const namesArray = names.split(' ');
		let first: string;
		let second: string;
		if (namesArray.length > 0) {
			first = namesArray[0];
			if (namesArray.length > 1) {
				second = '';
				for (let index = 1; index < namesArray.length; index++)
					second += namesArray[index] + ' ';
				second = second.trim();
			}
		}
		return { first, second }
	}
}

interface Names {
	first: string,
	second?: string
}