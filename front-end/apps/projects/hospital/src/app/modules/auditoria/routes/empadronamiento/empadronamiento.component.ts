import { Component, OnInit } from '@angular/core';
import { AbstractControl, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatTabChangeEvent } from '@angular/material/tabs';
import { GenderDto, IdentificationTypeDto, PatientRegistrationSearchDto } from '@api-rest/api-model';
import { AuditPatientService } from '@api-rest/services/audit-patient.service';
import { PersonMasterDataService } from '@api-rest/services/person-master-data.service';
import { PERSON } from '@core/constants/validation-constants';
import { MIN_DATE } from '@core/utils/date.utils';
import { hasError } from '@core/utils/form.utils';
import { DateFormat, momentFormat, newMoment } from '@core/utils/moment.utils';
import { IDENTIFICATION_TYPE_IDS } from '@core/utils/patient.utils';
import { PATTERN_INTEGER_NUMBER } from '@core/utils/pattern.utils';
import { Moment } from 'moment';

@Component({
	selector: 'app-empadronamiento',
	templateUrl: './empadronamiento.component.html',
	styleUrls: ['./empadronamiento.component.scss']
})
export class EmpadronamientoComponent implements OnInit {
	hasError = hasError;
	personalInformationForm: FormGroup;
	patientIdForm: FormGroup;
	genders: GenderDto[];
	identificationTypeList: IdentificationTypeDto[];
	today: Moment = newMoment();
	minDate = MIN_DATE;
	patientStates = ["Temporario", "Permanente no validado", "Validado", "Permanente"];
	formSubmitted: boolean = false;
	optionsValidations = OptionsValidations;
	tabActiveIndex = 0;
	patientRegistrationSearch: PatientRegistrationSearchDto[];
	genderTableView: string[] = [];
	viewCardToAudit = true;

	readonly validations = PERSON;
	constructor(private readonly formBuilder: FormBuilder, private readonly personMasterDataService: PersonMasterDataService,
		private auditPatientService: AuditPatientService,
	) { }

	ngOnInit(): void {
		this.setMasterData();
		this.initForms();
		this.personMasterDataService.getGenders().subscribe(
			genders => {
				genders.forEach(gender => {
					this.genderTableView[gender.id] = gender.description;
				});
			});
	}

	private setMasterData(): void {

		this.personMasterDataService.getGenders()
			.subscribe(genders => {
				this.genders = genders;
			});

		this.personMasterDataService.getIdentificationTypes()
			.subscribe(identificationTypes => {
				this.identificationTypeList = identificationTypes;
			});
	}

	private initForms() {
		this.patientIdForm = this.formBuilder.group({
			patientId: [null, [Validators.pattern(PATTERN_INTEGER_NUMBER)]]
		})

		this.personalInformationForm = this.formBuilder.group({
			firstName: [null, [Validators.maxLength(PERSON.MAX_LENGTH.firstName), Validators.pattern(/^(?!\s)/)]],
			middleNames: [null, [Validators.maxLength(PERSON.MAX_LENGTH.middleNames), Validators.pattern(/^(?!\s)/)]],
			lastName: [null, [Validators.maxLength(PERSON.MAX_LENGTH.lastName), Validators.pattern(/^(?!\s)/)]],
			otherLastNames: [null, [Validators.maxLength(PERSON.MAX_LENGTH.otherLastNames), Validators.pattern(/^(?!\s)/)]],
			genderId: [null],
			identificationNumber: [, [Validators.maxLength(PERSON.MAX_LENGTH.identificationNumber), Validators.pattern(PATTERN_INTEGER_NUMBER), Validators.pattern(/^\S*$/)]],
			identificationTypeId: [IDENTIFICATION_TYPE_IDS.DNI],
			birthDate: [null],
			filterState: [this.patientStates, [Validators.required]],
			filterAudit: ['false'],
			filterStateValidation: [this.optionsValidations.BothValidations]
		});
	}

	search() {
		let patientSearchFilter = this.prepareSearchDto();
		this.formSubmitted = true;
		if ((this.tabActiveIndex === 0 && this.personalInformationForm.valid) || (this.tabActiveIndex === 1 && this.patientIdForm.valid)) {
			this.auditPatientService.getSearchRegistrationPatient(patientSearchFilter).subscribe((patientRegistrationSearchDto: PatientRegistrationSearchDto[]) => {
				this.patientRegistrationSearch = patientRegistrationSearchDto;
			})
		}
	}

	tabChanged(tabChangeEvent: MatTabChangeEvent): void {
		this.tabActiveIndex = tabChangeEvent.index;
		this.patientRegistrationSearch = [];
		this.initForms();
	}

	prepareSearchDto() {
		let filterDto: any;
		if (this.tabActiveIndex === 0) {
			filterDto = {
				patientId: null,
				lastName: this.personalInformationForm.controls.lastName.value,
				firstName: this.personalInformationForm.controls.firstName.value,
				middleNames: this.personalInformationForm.controls.middleNames.value,
				otherLastNames: this.personalInformationForm.controls.otherLastNames.value,
				genderId: this.personalInformationForm.controls.genderId.value,
				identificationTypeId: this.personalInformationForm.controls.identificationTypeId.value,
				identificationNumber: this.personalInformationForm.controls.identificationNumber.value,
				birthDate: this.personalInformationForm.controls.birthDate.value !== null ? momentFormat(this.personalInformationForm.controls.birthDate.value, DateFormat.API_DATE) : null,
				toAudit: this.personalInformationForm.controls.filterAudit.value ? this.personalInformationForm.controls.filterAudit.value === 'true' : true,
				temporary: this.personalInformationForm.controls.filterState.value.includes("Temporario") ? true : false,
				permanentNotValidated: this.personalInformationForm.controls.filterState.value.includes("Permanente no validado") ? true : false,
				validated: this.personalInformationForm.controls.filterState.value.includes("Validado") ? true : false,
				permanent: this.personalInformationForm.controls.filterState.value.includes("Permanente") ? true : false,
			}
		} else {
			filterDto = {
				patientId: this.patientIdForm.controls.patientId.value,
				lastName: null,
				firstName: null,
				middleNames: null,
				otherLastNames: null,
				genderId: null,
				identificationTypeId: null,
				identificationNumber: null,
				birthDate: null,
				toAudit: null,
				temporary: null,
				permanentNotValidated: null,
				validated: null,
				permanent: null,
			}
		}

		if (this.personalInformationForm.controls.filterStateValidation.value === OptionsValidations.ManualValidation) {
			filterDto.automaticValidation = false;
			filterDto.manualValidation = true;
		} else if (this.personalInformationForm.controls.filterStateValidation.value === OptionsValidations.AutomaticValidation) {
			filterDto.automaticValidation = true;
			filterDto.manualValidation = false;
		} else {
			filterDto.automaticValidation = true;
			filterDto.manualValidation = true;
		}

		return filterDto;

	}

	clear(control: AbstractControl): void {
		control.reset();
	}

}
export enum OptionsValidations {
	AutomaticValidation,
	ManualValidation,
	BothValidations,
}
