import { Component, OnInit } from '@angular/core';
import { AbstractControl, UntypedFormBuilder, UntypedFormGroup, Validators } from '@angular/forms';
import { PersonMasterDataService } from '@api-rest/services/person-master-data.service';
import { GenderDto, IdentificationTypeDto, LimitedPatientSearchDto, PatientSearchDto } from '@api-rest/api-model';
import { AppFeature } from '@api-rest/api-model';
import { atLeastOneValueInFormGroup, hasError, } from '@core/utils/form.utils';
import { newDate, } from '@core/utils/moment.utils';
import { PatientService, PersonInformationRequest } from '@api-rest/services/patient.service';
import { PERSON, REMOVE_SUBSTRING_DNI } from '@core/constants/validation-constants';
import { MIN_DATE } from "@core/utils/date.utils";
import { FeatureFlagService } from "@core/services/feature-flag.service";
import { IDENTIFICATION_TYPE_IDS } from '@core/utils/patient.utils';
import { TableModel } from '@presentation/components/table/table.component';
import { toApiFormat } from '@api-rest/mapper/date.mapper';

@Component({
	selector: 'app-home',
	templateUrl: './home.component.html',
	styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {
	patientData: PatientSearchDto[] = [];
	genderTableView: string[] = [];
	public personalInformationForm: UntypedFormGroup;
	public genders: GenderDto[];
	public identificationTypeList: IdentificationTypeDto[];
	public hasError = hasError;
	public today = newDate();
	public formSubmitted: boolean;
	public requiringValues: boolean;
	public patientResultsLength: number;
	public readonly validations = PERSON;
	readonly MAX_RESULT_SIZE: number = 150;
	nameSelfDeterminationEnabled: boolean;
	minDate = MIN_DATE;
	requiringAtLeastOneMoreValue: boolean;
	public tableModel: TableModel<PatientSearchDto>;


	constructor(
		private readonly formBuilder: UntypedFormBuilder,
		private readonly personMasterDataService: PersonMasterDataService,
		private readonly patientService: PatientService,
		private readonly featureFlagService: FeatureFlagService,

	) {
		this.featureFlagService.isActive(AppFeature.HABILITAR_DATOS_AUTOPERCIBIDOS).subscribe(isEnabled => {
			this.nameSelfDeterminationEnabled = isEnabled
		});
	}

	ngOnInit(): void {
		this.initPersonalInformationForm();
		this.setMasterData();

	}

	private scrollToSearchResults() {
		let searchResult = document.getElementById("searchResults");
		searchResult.scrollIntoView({ behavior: 'smooth' });
	}

	private initPersonalInformationForm() {
		this.personalInformationForm = this.formBuilder.group({
			firstName: [null, [Validators.maxLength(PERSON.MAX_LENGTH.firstName), Validators.pattern(/^(?!\s).*\S$/)]],
			middleNames: [null, [Validators.maxLength(PERSON.MAX_LENGTH.middleNames), Validators.pattern(/^(?!\s).*\S$/)]],
			lastName: [null, [Validators.maxLength(PERSON.MAX_LENGTH.lastName), Validators.pattern(/^(?!\s).*\S$/)]],
			otherLastNames: [null, [Validators.maxLength(PERSON.MAX_LENGTH.otherLastNames), Validators.pattern(/^(?!\s).*\S$/)]],
			genderId: [],
			identificationNumber: [null, [Validators.maxLength(PERSON.MAX_LENGTH.identificationNumber), Validators.pattern(/^\S*$/)]],
			identificationTypeId: [IDENTIFICATION_TYPE_IDS.DNI],
			birthDate: []
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

		this.personMasterDataService.getGenders().subscribe(
			genders => {
				genders.forEach(gender => {
					this.genderTableView[gender.id] = gender.description;
				});
			});
	}

	save(): void {
		this.requiringValues = false;
		this.requiringAtLeastOneMoreValue = false;

		const atLeastOneValueSet: boolean = atLeastOneValueInFormGroup(this.personalInformationForm);
		if (!atLeastOneValueSet) {
			this.formSubmitted = false;
			this.requiringValues = true;
			return;
		}

		if (this.onlyFirstNameSet()) {
			this.formSubmitted = false;
			this.requiringAtLeastOneMoreValue = true;
			return;
		}

		if ((this.personalInformationForm.valid)) {
			this.formSubmitted = true;
			this.requiringValues = false;
			this.requiringAtLeastOneMoreValue = false;
			this.personalInformationForm.value.identificationNumber = this.personalInformationForm.value.identificationNumber?.replace(REMOVE_SUBSTRING_DNI, '');
			const personalInformationFilter = this.getPersonalInformationFilters();
			this.patientService.searchPatientOptionalFilters(personalInformationFilter)
				.subscribe((data: LimitedPatientSearchDto) => {
					this.patientData = data.patientList;
					this.patientResultsLength = data.actualPatientSearchSize;
					setTimeout(() => {
						this.scrollToSearchResults();
					}, 500);
				});
		}
	}

	clear(control: AbstractControl): void {
		control.reset();
	}

	onlyFirstNameSet(): boolean {
		const firstNameIsSeted = (this.personalInformationForm.value.firstName !== null);
		return firstNameIsSeted && !this.otherAttributesAreSet();
	}

	otherAttributesAreSet(): boolean {
		let formValues = this.personalInformationForm.value;
		if (formValues.identificationTypeId !== null)
			return true
		if (formValues.identificationNumber !== null)
			return true
		if (formValues.lastName !== null)
			return true
		if (formValues.middleNames !== null)
			return true
		if (formValues.otherLastNames !== null)
			return true
		if (formValues.genderId !== null)
			return true
		if (formValues.birthDate !== null)
			return true
		return false;
	}

	setSelectedDate(selectedDate: Date) {
		this.personalInformationForm.controls.birthDate.setValue(selectedDate);
	}

	private getPersonalInformationFilters(): PersonInformationRequest {
		const filters = this.personalInformationForm.value;
		return {
			...filters,
			...(filters.birthDate && { birthDate: toApiFormat(filters.birthDate) })
		}
	}


}
