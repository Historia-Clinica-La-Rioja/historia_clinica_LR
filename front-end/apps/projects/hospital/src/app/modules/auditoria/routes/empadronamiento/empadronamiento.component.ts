import { Component, Input, OnInit } from '@angular/core';
import { AbstractControl, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatTabChangeEvent } from '@angular/material/tabs';
import { BMPersonDto, GenderDto, IdentificationTypeDto, MergedPatientSearchDto, PatientRegistrationSearchDto } from '@api-rest/api-model';
import { toApiFormat } from '@api-rest/mapper/date.mapper';
import { AuditPatientService } from '@api-rest/services/audit-patient.service';
import { PersonMasterDataService } from '@api-rest/services/person-master-data.service';
import { PERSON, REMOVE_SUBSTRING_DNI } from '@core/constants/validation-constants';
import { capitalize } from '@core/utils/core.utils';
import { MIN_DATE } from '@core/utils/date.utils';
import { fixDate } from '@core/utils/date/format';
import { hasError } from '@core/utils/form.utils';
import { newDate } from '@core/utils/moment.utils';
import { IDENTIFICATION_TYPE_IDS } from '@core/utils/patient.utils';
import { PATTERN_INTEGER_NUMBER } from '@core/utils/pattern.utils';

@Component({
	selector: 'app-empadronamiento',
	templateUrl: './empadronamiento.component.html',
	styleUrls: ['./empadronamiento.component.scss']
})
export class EmpadronamientoComponent implements OnInit {
	@Input() isUnlinkPatient: boolean;
	hasError = hasError;
	personalInformationForm: FormGroup;
	patientIdForm: FormGroup;
	genders: GenderDto[];
	identificationTypeList: IdentificationTypeDto[];
	today: Date = newDate();
	minDate = MIN_DATE;
	patientStates: string[] = [];
	formSubmitted: boolean = false;
	optionsValidations = OptionsValidations;
	tabActiveIndex = 0;
	resultSearchPatient: PatientRegistrationSearchDto[] | MergedPatientSearchDto[];
	resultPatientsToAudit: PatientRegistrationSearchDto[] = [];
	genderTableView: string[] = [];
	viewCardToAudit = true;
	readonly validations = PERSON;
	private applySearchFilter = '';

	constructor(private readonly formBuilder: FormBuilder, private readonly personMasterDataService: PersonMasterDataService,
		private auditPatientService: AuditPatientService,
	) { }

	ngOnInit(): void {
		this.initForms();
		this.setMasterData();

	}

	private setMasterData(): void {
		this.auditPatientService.getTypesPatient().subscribe(res => {
			this.patientStates = res.map(r =>
				r.description
			);
			this.personalInformationForm.controls.filterState.setValue(this.patientStates)
		})
		this.personMasterDataService.getGenders()
			.subscribe(genders => {
				this.genders = genders;
			});
		this.personMasterDataService.getIdentificationTypes()
			.subscribe(identificationTypes => {
				this.identificationTypeList = identificationTypes;
			});
		this.personMasterDataService.getGenders().subscribe(
			genders => {
				genders.forEach(gender => {
					this.genderTableView[gender.id] = gender.description;
				});
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
			if (this.isUnlinkPatient) {
				this.auditPatientService.getSearchMergedPatient(patientSearchFilter).subscribe((patientMergedSearchDto: MergedPatientSearchDto[]) => {
					this.resultSearchPatient = patientMergedSearchDto;
				})
			} else {
				this.auditPatientService.getSearchRegistrationPatient(patientSearchFilter).subscribe((patientRegistrationSearchDto: PatientRegistrationSearchDto[]) => {
					this.resultSearchPatient = patientRegistrationSearchDto;
				})
			}
		}
	}

	tabChanged(tabChangeEvent: MatTabChangeEvent): void {
		this.tabActiveIndex = tabChangeEvent.index;
		this.resultSearchPatient = [];
		this.initForms();
		if (this.tabActiveIndex === 2) {
			this.getMarkedForAudit();
		}
	}

	getMarkedForAudit() {
		this.auditPatientService.getFetchPatientsToAudit().subscribe((patientRegistrationSearchDto: PatientRegistrationSearchDto[]) => {
			this.resultPatientsToAudit = patientRegistrationSearchDto;
			this.resultSearchPatient = this.resultPatientsToAudit;
		})
	}

	prepareSearchDto() {
		let filterDto: any;
		if (this.tabActiveIndex === 0) {
			const date: Date = fixDate(this.personalInformationForm.controls.birthDate.value);
			filterDto = {
				patientId: null,
				lastName: this.personalInformationForm.controls.lastName.value,
				firstName: this.personalInformationForm.controls.firstName.value,
				middleNames: this.personalInformationForm.controls.middleNames.value,
				otherLastNames: this.personalInformationForm.controls.otherLastNames.value,
				genderId: this.personalInformationForm.controls.genderId.value,
				identificationTypeId: this.personalInformationForm.controls.identificationTypeId.value,
				identificationNumber: this.personalInformationForm.controls.identificationNumber.value,
				birthDate: this.personalInformationForm.controls.birthDate.value !== null ? toApiFormat(date) : null,
				toAudit: this.personalInformationForm.controls.filterAudit.value ? this.personalInformationForm.controls.filterAudit.value === 'true' : true,
				temporary: !!this.personalInformationForm.controls.filterState.value.includes("Temporario"),
				permanentNotValidated: !!this.personalInformationForm.controls.filterState.value.includes("Permanente no validado"),
				validated: !!this.personalInformationForm.controls.filterState.value.includes("Validado"),
				permanent: !!this.personalInformationForm.controls.filterState.value.includes("Permanente"),
				rejected: !!this.personalInformationForm.controls.filterState.value.includes("Rechazado"),
			};
		}
		else {
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

	applyFilter($event: any): void {
		this.applySearchFilter = ($event.target as HTMLInputElement).value?.replace(REMOVE_SUBSTRING_DNI, '');
		this.applyFiltes();
	}

	private applyFiltes(): void {
		this.resultSearchPatient = this.filter();
	}

	private filter(): PatientRegistrationSearchDto[] | MergedPatientSearchDto[] {
		let listFilter = this.resultSearchPatient;
		if (this.applySearchFilter) {
			listFilter = (listFilter as PatientRegistrationSearchDto[]).filter((e: PatientRegistrationSearchDto) => this.getFullName(e.person).toLowerCase().includes(this.applySearchFilter.toLowerCase())
				|| e?.person.identificationNumber.toString().includes(this.applySearchFilter))
		} else {
			listFilter = this.resultPatientsToAudit;
		}
		return listFilter;
	}

	getFullName(patient: BMPersonDto): string {
		const names = [
			patient?.firstName,
			patient?.middleNames,
			patient?.lastName,
			patient?.otherLastNames
		].filter(name => name !== undefined && name.trim() !== '');

		const capitalizedNames = names.map(name => capitalize(name));
		return capitalizedNames.join(' ');
	}
}
export enum OptionsValidations {
	AutomaticValidation,
	ManualValidation,
	BothValidations,
}
