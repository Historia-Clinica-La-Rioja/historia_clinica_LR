import { Component, OnInit } from '@angular/core';
import { UntypedFormBuilder, UntypedFormGroup, Validators } from '@angular/forms';
import { hasError, VALIDATIONS } from '@core/utils/form.utils';
import { IDENTIFICATION_TYPE_IDS, PATIENT_TYPE } from '@core/utils/patient.utils';
import { ActivatedRoute, Router } from '@angular/router';
import { ERole } from '@api-rest/api-model';
import { GenderDto, IdentificationTypeDto, PatientSearchDto } from '@api-rest/api-model';
import { PatientService } from '@api-rest/services/patient.service';
import { PersonMasterDataService } from '@api-rest/services/person-master-data.service';
import { dateISOParseDate, newDate } from '@core/utils/moment.utils';
import { MatDialog } from '@angular/material/dialog';
import { ViewPatientDetailComponent } from '../../component/view-patient-detail/view-patient-detail.component';
import { ContextService } from '@core/services/context.service';
import { PERSON } from '@core/constants/validation-constants';
import { MIN_DATE } from "@core/utils/date.utils";
import { PermissionsService } from '@core/services/permissions.service';
import { differenceInYears } from 'date-fns';
import { DateFormatPipe } from '@presentation/pipes/date-format.pipe';
import { toApiFormat } from '@api-rest/mapper/date.mapper';
import { ParamsToSearchPerson } from '@pacientes/component/search-create/search-create.component';
import { map, take } from 'rxjs';
import { encode, toParamsToSearchPerson } from '@pacientes/utils/search.utils';

const ROUTE_NEW = 'pacientes/new';
const ROUTE_NEW_TEMPORARY = 'pacientes/temporary';
const ROUTE_HOME = 'pacientes';
const TIME_TO_PREVENT_SCROLL = 100;
const ROUTE_GUARD = 'guardia/nuevo-episodio/administrativa';

@Component({
	selector: 'app-search',
	templateUrl: './search.component.html',
	styleUrls: ['./search.component.scss']
})
export class SearchComponent implements OnInit {
	hasInstitutionalAdministratorRole = false;
	readonly PERSON_MAX_LENGHT = PERSON;
	patientData: PatientSearchDto[] = [];
	minDate = MIN_DATE;
	today: Date = newDate();
	paramsInformation: ParamsToSearchPerson;
	public formSearchSubmitted = false;
	public formSearch: UntypedFormGroup;
	public identifyTypeArray: IdentificationTypeDto[];
	public identifyTypeViewPatientDetail = {};
	public genderOptions: GenderDto[];
	public genderOptionsViewTable = {};
	public viewSearch = true;
	public isLoading = true;
	public hasError = hasError;
	public identificationTypeId;
	public identificationNumber;
	public genderId;
	searchPatient: ParamsToSearchPerson;
	public noIdentity: boolean;
	private readonly routePrefix;
	public genderFieldDisabled = false;
	public identificationTypeFieldDisabled = false;
	public identificationNumberFieldDisabled = false;
	fromGuardModule = false;

	constructor(
		private formBuilder: UntypedFormBuilder,
		private patientService: PatientService,
		private router: Router,
		private route: ActivatedRoute,
		private personMasterDataService: PersonMasterDataService,
		public dialog: MatDialog,
		private contextService: ContextService,
		private permissionsService: PermissionsService,
		private readonly dateFormatPipe: DateFormatPipe

	) {
		this.routePrefix = 'institucion/' + this.contextService.institutionId + '/';
	}

	ngOnInit(): void {
		this.route.queryParams.pipe(take(1), map(params => toParamsToSearchPerson(params))).subscribe(params => {
			this.paramsInformation = params;
			this.fromGuardModule = params.fromGuardModule;
			this.identificationTypeId = this.paramsInformation.identificationTypeId;
			this.identificationNumber = this.paramsInformation.identificationNumber;
			this.genderId = this.paramsInformation.genderId;
			this.noIdentity = this.paramsInformation.noIdentity;
			if (!this.noIdentity) {
				this.buildFormSearchWithValidations(params);
				this.isLoading = false;
			} else {
				setTimeout(() => {
					this.buildFormSearchWithoutValidations(params);
				}, TIME_TO_PREVENT_SCROLL);
			}
		});

		this.personMasterDataService.getIdentificationTypes().subscribe(
			identificationTypes => {
				this.identifyTypeArray = identificationTypes;
				this.identifyTypeArray.forEach(identificationType => {
					this.identifyTypeViewPatientDetail[identificationType.id] = identificationType.description;
				});
			});

		this.personMasterDataService.getGenders().subscribe(
			genders => {
				this.genderOptions = genders;
				this.genderOptions.forEach(gender => {
					this.genderOptionsViewTable[gender.id] = gender.description;
				});
			});

		this.permissionsService.hasContextAssignments$([ERole.ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE, ERole.ADMINISTRADOR_INSTITUCIONAL_PRESCRIPTOR]).subscribe(hasInstitutionalAdministratorRole => this.hasInstitutionalAdministratorRole = hasInstitutionalAdministratorRole);

	}


	openDialog(patient: PatientSearchDto): void {
		this.dialog.open(ViewPatientDetailComponent, {
			width: '450px',
			data: {
				id: patient.idPatient,
				firstName: patient.person.firstName,
				lastName: patient.person.lastName,
				age: calculateAge(String(patient.person.birthDate)),
				gender: this.genderOptionsViewTable[patient.person.genderId],
				birthDate: (patient.person.birthDate === undefined) ? '' :
					this.dateFormatPipe.transform(patient.person.birthDate, 'date'),
				identificationNumber: patient.person.identificationNumber,
				identificationTypeId: this.identifyTypeViewPatientDetail[patient.person.identificationTypeId]
			}
		});

		function calculateAge(birthDate: string): number {
			const today = newDate();
			const birth = dateISOParseDate(birthDate)
			const result = differenceInYears(today, birth)
			return result;
		}
	}

	private buildFormSearchWithValidations(params) {
		this.formSearch = this.formBuilder.group({
			identificationNumber: [params.identificationNumber, this.identificationTypeId == IDENTIFICATION_TYPE_IDS.NO_POSEE ? [] :
				[Validators.required, Validators.maxLength(VALIDATIONS.MAX_LENGTH.identif_number), Validators.pattern(/^\S*$/)]],
			identificationTypeId: [params.identificationTypeId ? Number(params.identificationTypeId) : null, Validators.required],
			firstName: [params.firstName, Validators.required],
			middleNames: [params.middleNames],
			lastName: [params.lastName, Validators.required],
			otherLastNames: [params.otherLastNames],
			genderId: [Number(params.genderId) ? Number(params.genderId) : undefined, Validators.required],
			birthDate: [params.birthDate ? (params.birthDate) : undefined, Validators.required]
		});
		this.lockFormField(params);
	}

	private buildFormSearchWithoutValidations(params) {
		this.formSearch = this.formBuilder.group({
			identificationNumber: [params.identificationNumber != 0 ? params.identificationNumber : '', [Validators.maxLength(VALIDATIONS.MAX_LENGTH.identif_number), Validators.pattern(/^\S*$/)]],
			identificationTypeId: [Number(params.identificationTypeId)],
			firstName: [params.firstName],
			middleNames: [params.middleNames],
			lastName: [params.lastName],
			otherLastNames: [params.otherLastNames],
			genderId: [Number(params.genderId) ? Number(params.genderId) : undefined],
			birthDate: [params.birthDate ? dateISOParseDate(params.birthDate) : undefined]
		});
		this.lockFormField(params);
		this.isLoading = false;
	}

	private lockFormField(params) {

		if (params.identificationNumber) {
			this.formSearch.controls.identificationNumber.disable();
			this.identificationNumberFieldDisabled = true;
		}
		if (params.identificationTypeId) {
			this.formSearch.controls.identificationTypeId.disable();
			this.identificationTypeFieldDisabled = true;
		}
		if (params.genderId) {
			this.formSearch.controls.genderId.disable();
			this.genderFieldDisabled = true;
		}

	}

	back() {
		const route = this.fromGuardModule ? ROUTE_GUARD : ROUTE_HOME;
		this.router.navigate([this.routePrefix + route]);
	}

	submit() {
		this.formSearchSubmitted = true;
		if (this.formSearch.valid) {
			this.searchPatient = {
				firstName: this.formSearch.controls.firstName.value,
				lastName: this.formSearch.controls.lastName.value,
				genderId: this.formSearch.controls.genderId.value ? this.formSearch.controls.genderId.value : null,
				identificationTypeId: this.formSearch.controls.identificationTypeId.value ? this.formSearch.controls.identificationTypeId.value : null,
				identificationNumber: this.formSearch.controls.identificationNumber.value,
				birthDate: this.formSearch.controls.birthDate.value ? toApiFormat(this.formSearch.controls.birthDate.value) : null,
				otherLastNames: this.formSearch.controls.otherLastNames.value,
				middleNames: this.formSearch.controls.middleNames.value,
				typeId: PATIENT_TYPE.PERMANENT_INVALID
			};
			this.goToNextState(this.searchPatient);
		}
	}

	private goToNextState(person: ParamsToSearchPerson) {
		this.patientService.getPatientByCMD(JSON.stringify(person)).subscribe(
			(patientsFound: PatientSearchDto[]) => {
				if (!patientsFound.length) {
					this.goToAddPatient(person);
				} else {
					this.patientData = patientsFound;
					this.viewSearch = false;
				}
			}
		);
	}

	goToAddPatient(person: ParamsToSearchPerson) {
		if (this.noIdentity || this.identificationTypeId == IDENTIFICATION_TYPE_IDS.NO_POSEE) {
			this.router.navigate([this.routePrefix + ROUTE_NEW_TEMPORARY], {
				queryParams: person
			});

		} else {
			person.fromGuardModule = this.fromGuardModule;
			let encryptedPerson = encode(JSON.stringify(person));
			this.router.navigate([this.routePrefix + ROUTE_NEW], {
				queryParams: {
					person: encryptedPerson
				}
			});
		}
	}

	goToNewPatient() {
		if (this.searchPatient) {
			this.goToAddPatient(this.searchPatient);
		} else {
			const person: ParamsToSearchPerson = {
				identificationTypeId: this.identificationTypeId,
				identificationNumber: this.identificationNumber,
				genderId: this.genderId,
				typeId: PATIENT_TYPE.PERMANENT_INVALID
			};
			this.goToAddPatient(person);
		}
	}

	viewSearchComponent(): boolean {
		return this.viewSearch;
	}

	showSpinner(): boolean {
		return this.isLoading;
	}

}
