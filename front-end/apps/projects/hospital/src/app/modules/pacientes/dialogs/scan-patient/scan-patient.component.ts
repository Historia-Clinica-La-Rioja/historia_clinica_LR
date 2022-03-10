import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from "@angular/forms";
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { GenderDto, IdentificationTypeDto } from '@api-rest/api-model';
import { PatientInformationScan } from "@pacientes/pacientes.model";
import { isValid, parse } from 'date-fns';

const DATA_SPLIT_ENGLISH = '@';
const DATA_SPLIT_SPANISH = '"';
const MIN_AMOUNT_DATA_FROM_VALID_SCAN = 8;
const AMOUNT_DATA_DNI_V1 = 15;
const AMOUNT_DATA_DNI_V2 = 9;
const enum DNI_V1 {
	IDENTIFICATION_NUMBER = 1,
	GENDER = 8,
	NAMES = 5,
	LAST_NAMES = 4,
	BIRTH_DATE = 7,
}
const enum DNI_V2 {
	IDENTIFICATION_NUMBER = 4,
	GENDER = 3,
	NAMES = 2,
	LAST_NAMES = 1,
	BIRTH_DATE = 6,
}

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

	private getGenderId(gender: string): number | undefined {
		const genderOption = this.data.genderOptions.find(option => option.description.includes(gender));
		return genderOption ? genderOption.id : undefined;
	}

	public captureInformation(valueForm): void {

		let valueFormArray: string[];
		if (valueForm.target.value.includes(DATA_SPLIT_SPANISH))
			valueFormArray = valueForm.target.value.split(DATA_SPLIT_SPANISH);
		else if (valueForm.target.value.includes(DATA_SPLIT_ENGLISH))
			valueFormArray = valueForm.target.value.split(DATA_SPLIT_ENGLISH);

		if (valueFormArray?.length >= MIN_AMOUNT_DATA_FROM_VALID_SCAN) {
			if (valueFormArray?.length <= AMOUNT_DATA_DNI_V2) {
				this.loadPatientInformationScan(
					valueFormArray[DNI_V2.IDENTIFICATION_NUMBER],
					valueFormArray[DNI_V2.GENDER],
					valueFormArray[DNI_V2.NAMES],
					valueFormArray[DNI_V2.LAST_NAMES],
					valueFormArray[DNI_V2.BIRTH_DATE]
				);
			}
			else if (valueFormArray?.length === AMOUNT_DATA_DNI_V1) {
				this.loadPatientInformationScan(
					valueFormArray[DNI_V1.IDENTIFICATION_NUMBER],
					valueFormArray[DNI_V1.GENDER],
					valueFormArray[DNI_V1.NAMES],
					valueFormArray[DNI_V1.LAST_NAMES],
					valueFormArray[DNI_V1.BIRTH_DATE]
				);
			}

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
		const REGULAR_EXPRESSION = /^[a-zA-Z '´ñÑáÁéÉíÍóÓúÚ]+$/;
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

	private getIdentifTypeID(typeDesc: string): number | undefined {
		const IDENTIF_TYPE = this.data.identifyTypeArray.find(identifType => identifType.description.toUpperCase() === typeDesc.toUpperCase());
		return IDENTIF_TYPE ? IDENTIF_TYPE.id : undefined;
	}

	private loadPatientInformationScan(identifNumber: string, gender: string, allNames: string, allLastNames: string, birthDate: string): void {
		let names: Names;
		if (this.canBeAName(allNames))
			names = this.splitNames(allNames);
		let lastNames: Names;
		if (this.canBeAName(allLastNames))
			lastNames = this.splitNames(allLastNames);
		this.patientInformationScan = {
			identifType: this.getIdentifTypeID('DNI'),
			identifNumber: this.isAnIdentificationNumber(identifNumber) ? identifNumber.trim() : undefined,
			gender: this.getGenderId(gender),
			birthDate: this.isDate(birthDate) ? birthDate.split('/').join('-') : undefined,
			firstName: names?.first,
			middleNames: names?.second,
			lastName: lastNames?.first,
			otherLastNames: lastNames?.second
		}
	}
}

interface Names {
	first: string,
	second?: string
}