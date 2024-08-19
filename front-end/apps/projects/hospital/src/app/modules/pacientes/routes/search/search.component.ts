import { Component, OnInit } from '@angular/core';
import { UntypedFormBuilder, UntypedFormGroup, Validators } from '@angular/forms';
import { hasError, VALIDATIONS } from '@core/utils/form.utils';
import { IDENTIFICATION_TYPE_IDS, PATIENT_TYPE } from '@core/utils/patient.utils';
import { ActivatedRoute, Router } from '@angular/router';
import { ERole } from '@api-rest/api-model';
import { MasterDataDto } from '@api-rest/api-model';
import { GenderDto, IdentificationTypeDto, PatientSearchDto } from '@api-rest/api-model';
import { AppFeature } from '@api-rest/api-model';
import { PatientService } from '@api-rest/services/patient.service';
import { PersonMasterDataService } from '@api-rest/services/person-master-data.service';
import { dateISOParseDate, newDate } from '@core/utils/moment.utils';
import { ActionDisplays, TableModel } from '@presentation/components/table/table.component';
import { PersonService } from '@api-rest/services/person.service';
import { finalize } from 'rxjs/operators';
import { MatDialog } from '@angular/material/dialog';
import { ViewPatientDetailComponent } from '../../component/view-patient-detail/view-patient-detail.component';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { ContextService } from '@core/services/context.service';
import { FeatureFlagService } from '@core/services/feature-flag.service';
import { PERSON } from '@core/constants/validation-constants';
import { NavigationService } from '@pacientes/services/navigation.service';
import { MIN_DATE } from "@core/utils/date.utils";
import { PatientNameService } from "@core/services/patient-name.service";
import { PermissionsService } from '@core/services/permissions.service';
import { differenceInYears } from 'date-fns';
import { DateFormatPipe } from '@presentation/pipes/date-format.pipe';
import { toApiFormat } from '@api-rest/mapper/date.mapper';

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
	ffOfCardsIsOn = false;
	readonly PERSON_MAX_LENGHT = PERSON;
	patientData: PatientSearchDto[] = [];
	minDate = MIN_DATE;
	today: Date = newDate();
	genders: MasterDataDto[] = [];
	identificationTypes: MasterDataDto[] = [];
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
	public searchPatient;
	public noIdentity: boolean;
	private readonly routePrefix;
	public matchingPatient: TableModel<PatientSearchDto>;
	public genderFieldDisabled = false;
	public identificationTypeFieldDisabled = false;
	public identificationNumberFieldDisabled = false;
	fromGuardModule = false;

	constructor(
		private formBuilder: UntypedFormBuilder,
		private patientService: PatientService,
		private personService: PersonService,
		private router: Router,
		private route: ActivatedRoute,
		private personMasterDataService: PersonMasterDataService,
		private snackBarService: SnackBarService,
		public dialog: MatDialog,
		private contextService: ContextService,
		private featureFlagService: FeatureFlagService,
		public navigationService: NavigationService,
		private readonly patientNameService: PatientNameService,
		private permissionsService: PermissionsService,
		private readonly dateFormatPipe: DateFormatPipe

	) {
		this.routePrefix = 'institucion/' + this.contextService.institutionId + '/';
		this.personMasterDataService.getGenders()
			.subscribe(genders => {
				this.genders = genders;
			});
		this.personMasterDataService.getIdentificationTypes()
			.subscribe(identificationTypes => {
				this.identificationTypes = identificationTypes;
			});
	}

	ngOnInit(): void {
		if (this.navigationService.goBackFromNewPatient())
			this.back();
		else {
			this.route.queryParams.subscribe(params => {
				this.fromGuardModule = params.fromGuardModule;
				this.identificationTypeId = params.identificationTypeId;
				this.identificationNumber = +params.identificationNumber;
				this.genderId = params.genderId;
				this.noIdentity = params.noIdentity === 'true';
				if (!this.noIdentity) {
					this.buildFormSearchWithValidations(params);
					this.featureFlagService.isActive(AppFeature.HABILITAR_SERVICIO_RENAPER)
						.subscribe(result => {
							if (result && Number(this.identificationTypeId) === IDENTIFICATION_TYPE_IDS.DNI) {
								this.callRenaperService();
							} else {
								this.isLoading = false;
							}
						});
				} else {
					setTimeout(() => {
						this.buildFormSearchWithoutValidations(params);
					}, TIME_TO_PREVENT_SCROLL);
				}

				this.personMasterDataService.getIdentificationTypes().subscribe(
					identificationTypes => {
						this.identifyTypeArray = identificationTypes;
						identificationTypes.forEach(identificationType => {
							this.identifyTypeViewPatientDetail[identificationType.id] = identificationType.description;
						});
					});

				this.personMasterDataService.getGenders().subscribe(
					genders => {
						this.genderOptions = genders;
						genders.forEach(gender => {
							this.genderOptionsViewTable[gender.id] = gender.description;
						});
					});
			});
		}

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
				birthDate: this.formSearch.controls.birthDate.value? toApiFormat(this.formSearch.controls.birthDate.value) : null,
				otherLastNames: this.formSearch.controls.otherLastNames.value,
				middleNames: this.formSearch.controls.middleNames.value,
				typeId: PATIENT_TYPE.PERMANENT_INVALID
			};
			this.goToNextState(this.searchPatient);
		}
	}

	private buildTable(data: PatientSearchDto[]): TableModel<PatientSearchDto> {
		return {
			columns: [
				{
					columnDef: 'patiendId',
					header: 'ID Paciente',
					text: (row) => row.idPatient
				},
				{
					columnDef: 'firstName',
					header: 'Nombre',
					text: (row) => this.patientNameService.getPatientName(row.person.firstName, row.nameSelfDetermination)
				},
				{
					columnDef: 'lastName',
					header: 'Apellido',
					text: (row) => row.person.lastName
				},
				{
					columnDef: 'gender',
					header: 'Sexo documento',
					text: (row) => this.genderOptionsViewTable[row.person.genderId]
				},
				{
					columnDef: 'birthDate',
					header: 'F. Nac',
					text: (row) => (row.person.birthDate === undefined) ? '' :
						this.dateFormatPipe.transform(row.person.birthDate, 'date')
				},
				{
					columnDef: 'numberDni',
					header: 'Nro. Documento',
					text: (row) => row.person.identificationNumber
				},
				{
					columnDef: 'state',
					header: 'Estado',
					text: (row) => (row.activo ? 'Activo' : 'Inactivo')
				},
				{
					columnDef: 'ranking',
					header: 'Coincidencia',
					text: (row) => row.ranking + ' %'
				},
				{
					columnDef: 'action',
					action: {
						displayType: ActionDisplays.BUTTON,
						display: 'Ver',
						matColor: 'primary',
						do: (patient) => {
							this.openDialog(patient);
						}
					}
				},
			],
			data,
			enableFilter: true
		};
	}

	private goToNextState(person) {
		this.patientService.getPatientByCMD(JSON.stringify(person)).subscribe(
			(patientsFound: PatientSearchDto[]) => {
				if (!patientsFound.length) {
					this.goToAddPatient(person);
				} else {
					this.featureFlagService.isActive(AppFeature.HABILITAR_VISUALIZACION_DE_CARDS).subscribe(isEnabled => {
						this.ffOfCardsIsOn = isEnabled;
						if (this.ffOfCardsIsOn)
							this.patientData = patientsFound;
						else
							this.matchingPatient = this.buildTable(patientsFound);
					});

					this.viewSearch = false;
				}
			}
		);
	}

	goToAddPatient(person) {
		if (this.noIdentity || this.identificationTypeId == IDENTIFICATION_TYPE_IDS.NO_POSEE) {
			this.route.queryParams.subscribe(params => {
				person.comments = params.comments;
				person.identityVerificationStatus = params.identityVerificationStatus;
				this.router.navigate([this.routePrefix + ROUTE_NEW_TEMPORARY], {
					queryParams: person
				});
			})

		} else {
			this.router.navigate([this.routePrefix + ROUTE_NEW], {
				queryParams: {
					...person,
					fromGuardModule: this.fromGuardModule
				}
			});
		}
	}

	goToNewPatient() {
		if (this.searchPatient) {
			this.goToAddPatient(this.searchPatient);
		} else {
			const patient = {
				identificationTypeId: this.identificationTypeId,
				identificationNumber: this.identificationNumber,
				genderId: this.genderId,
				typeId: PATIENT_TYPE.PERMANENT_INVALID
			};
			this.goToAddPatient(patient);
		}
	}

	viewSearchComponent(): boolean {
		return this.viewSearch;
	}

	showSpinner(): boolean {
		return this.isLoading;
	}

	private splitStringByFirstSpaceCharacter(text: string): any {
		const spaceIndex: number = text.indexOf(' ');
		if (spaceIndex === 0) {
			return this.splitStringByFirstSpaceCharacter(text.substr(1));
		} else {
			return (spaceIndex !== -1) ?
				{
					firstSubstring: text.substr(0, spaceIndex),
					secondSubstring: text.substr(spaceIndex + 1)
				}
				:
				{ firstSubstring: text };
		}
	}

	private mapToPerson(personData): any {
		const splitedFirstName = this.splitStringByFirstSpaceCharacter(personData.firstName);
		const splitedLastName = this.splitStringByFirstSpaceCharacter(personData.lastName);
		return {
			firstName: splitedFirstName.firstSubstring,
			middleNames: splitedFirstName.secondSubstring,
			lastName: splitedLastName.firstSubstring,
			otherLastNames: splitedLastName.secondSubstring,
			birthDate: personData.birthDate,
			photo: personData.photo,
			cuil: personData.cuil
		};
	}

	private callRenaperService(): void {
		if(this.identificationNumber && this.identificationTypeId && this.genderId){
			this.personService.getRenaperPersonData({
				identificationNumber: this.identificationNumber,
				genderId: this.genderId
			})
				.pipe(finalize(() => this.isLoading = false))
				.subscribe(
					personData => {
						if (personData && Object.keys(personData).length !== 0) {
							const personToAdd = this.mapToPerson(personData);
							personToAdd.identificationTypeId = this.identificationTypeId;
							personToAdd.identificationNumber = this.identificationNumber;
							personToAdd.genderId = this.genderId;
							personToAdd.typeId = PATIENT_TYPE.VALID;
							this.goToAddPatient(personToAdd);
						}
					}, () => {
						this.snackBarService.showError('pacientes.search.RENAPER_TIMEOUT');
					});
		}else {
			this.isLoading = false;
		}
	}

}
