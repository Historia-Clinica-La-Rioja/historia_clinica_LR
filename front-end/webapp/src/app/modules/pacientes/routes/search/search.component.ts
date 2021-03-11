import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { hasError, VALIDATIONS } from '@core/utils/form.utils';
import { IDENTIFICATION_TYPE_IDS, PATIENT_TYPE } from '@core/utils/patient.utils';
import { ActivatedRoute, Router } from '@angular/router';
import { Moment } from 'moment';
import { GenderDto, IdentificationTypeDto, PatientSearchDto } from '@api-rest/api-model';
import { PatientService } from '@api-rest/services/patient.service';
import { PersonMasterDataService } from '@api-rest/services/person-master-data.service';
import { ActionDisplays, TableModel } from 'src/app/modules/presentation/components/table/table.component';
import { DateFormat, momentFormat, momentParseDate, momentParseDateTime, newMoment } from '@core/utils/moment.utils';
import { PersonService } from '@api-rest/services/person.service';
import { finalize } from 'rxjs/operators';
import { MatDialog } from '@angular/material/dialog';
import { ViewPatientDetailComponent } from '../../component/view-patient-detail/view-patient-detail.component';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { ContextService } from '@core/services/context.service';
import { FeatureFlagService } from '@core/services/feature-flag.service';
import { PERSON } from '@core/constants/validation-constants';

const ROUTE_NEW = 'pacientes/new';
const ROUTE_NEW_TEMPORARY = 'pacientes/temporary';
const ROUTE_HOME = 'pacientes';
const RENAPER_FFLAG = 'habilitarServicioRenaper';

@Component({
	selector: 'app-search',
	templateUrl: './search.component.html',
	styleUrls: ['./search.component.scss']
})
export class SearchComponent implements OnInit {

	readonly PERSON_MAX_LENGHT = PERSON;

	today: Moment = newMoment();
	public formSearchSubmitted = false;
	public formSearch: FormGroup;
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
	public matchingPatient: TableModel<PatientSearchDto>;
	public searchPatient;
	public noIdentity: boolean;
	private readonly routePrefix;

	constructor(
		private formBuilder: FormBuilder,
		private patientService: PatientService,
		private personService: PersonService,
		private router: Router,
		private route: ActivatedRoute,
		private personMasterDataService: PersonMasterDataService,
		private snackBarService: SnackBarService,
		public dialog: MatDialog,
		private contextService: ContextService,
		private featureFlagService: FeatureFlagService
	) {
		this.routePrefix = 'institucion/' + this.contextService.institutionId + '/';
	}

	ngOnInit(): void {
		this.route.queryParams.subscribe(params => {
			this.identificationTypeId = params.identificationTypeId;
			this.identificationNumber = params.identificationNumber;
			this.genderId = params.genderId;
			this.noIdentity = params.noIdentity === 'true';
			if (!this.noIdentity) {
				this.buildFormSearchWithValidations(params);
				this.featureFlagService.isOn(RENAPER_FFLAG)
					.subscribe(result => {
						if (result && Number(this.identificationTypeId) === IDENTIFICATION_TYPE_IDS.DNI) {
							this.callRenaperService();
						} else {
							this.isLoading = false;
						}
					});
			} else {
				this.buildFormSearchWithoutValidations(params);
				this.isLoading = false;
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
					text: (row) => row.person.firstName
				},
				{
					columnDef: 'lastName',
					header: 'Apellido',
					text: (row) => row.person.lastName
				},
				{
					columnDef: 'gender',
					header: 'Sexo',
					text: (row) => this.genderOptionsViewTable[row.person.genderId]
				},
				{
					columnDef: 'birthDate',
					header: 'F. Nac',
					text: (row) => (row.person.birthDate === undefined) ? '' :
						momentFormat(momentParseDateTime(String(row.person.birthDate)), DateFormat.VIEW_DATE)
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
					momentFormat(momentParseDateTime(String(patient.person.birthDate)), DateFormat.VIEW_DATE),
				identificationNumber: patient.person.identificationNumber,
				identificationTypeId: this.identifyTypeViewPatientDetail[patient.person.identificationTypeId]
			}
		});

		function calculateAge(birthDate: string): number {
			const today: Moment = newMoment();
			const birth: Moment = momentParseDateTime(birthDate);

			return today.diff(birth, 'years');
		}
	}

	private buildFormSearchWithValidations(params) {
		this.formSearch = this.formBuilder.group({
			identificationNumber: [params.identificationNumber, this.identificationTypeId == IDENTIFICATION_TYPE_IDS.NO_POSEE ? [] :
				[Validators.required, Validators.maxLength(VALIDATIONS.MAX_LENGTH.identif_number)]],
			identificationTypeId: [params.identificationTypeId ? Number(params.identificationTypeId) : null, Validators.required],
			firstName: [params.firstName, Validators.required],
			middleNames: [params.middleNames],
			lastName: [params.lastName, Validators.required],
			otherLastNames: [params.otherLastNames],
			genderId: [Number(params.genderId), Validators.required],
			birthDate: [params.birthDate ? momentParseDate(params.birthDate) : undefined, Validators.required]
		});
		this.lockFormField(params);
	}

	private buildFormSearchWithoutValidations(params) {
		this.formSearch = this.formBuilder.group({
			identificationNumber: [params.identificationNumber, [Validators.maxLength(VALIDATIONS.MAX_LENGTH.identif_number)]],
			identificationTypeId: [Number(params.identificationTypeId)],
			firstName: [params.firstName],
			middleNames: [params.middleNames],
			lastName: [params.lastName],
			otherLastNames: [params.otherLastNames],
			genderId: [Number(params.genderId)],
			birthDate: [params.birthDate ? momentParseDate(params.birthDate) : undefined]
		});
		this.lockFormField(params);
	}

	private lockFormField(params) {

		if (params.identificationNumber) {
			this.formSearch.controls.identificationNumber.disable();
		}
		if (params.identificationTypeId) {
			this.formSearch.controls.identificationTypeId.disable();
		}
		if (params.genderId) {
			this.formSearch.controls.genderId.disable();
		}

	}

	back() {
		this.router.navigate([this.routePrefix + ROUTE_HOME]);
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
				birthDate: this.formSearch.controls.birthDate.value?.format(DateFormat.API_DATE),
				otherLastNames: this.formSearch.controls.otherLastNames.value,
				middleNames: this.formSearch.controls.middleNames.value,
				typeId: PATIENT_TYPE.PERMANENT_INVALID
			};
			this.goToNextState(this.searchPatient);
		}
	}

	private goToNextState(person) {
		this.patientService.getPatientByCMD(JSON.stringify(person)).subscribe(
			patientsFound => {
				if (!patientsFound.length) {
					this.goToAddPatient(person);
				} else {
					this.matchingPatient = this.buildTable(patientsFound);
					this.viewSearch = false;
				}
			}
		);
	}

	goToAddPatient(person) {
		if (this.noIdentity || this.identificationTypeId == IDENTIFICATION_TYPE_IDS.NO_POSEE) {
			this.router.navigate([this.routePrefix + ROUTE_NEW_TEMPORARY], {
				queryParams: person
			});
		} else {
			this.router.navigate([this.routePrefix + ROUTE_NEW], {
				queryParams: person
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
				{firstSubstring: text};
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
			photo: personData.photo
		};
	}

	private callRenaperService(): void {
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
	}

}
