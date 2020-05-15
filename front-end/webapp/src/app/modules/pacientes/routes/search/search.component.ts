import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { hasError, VALIDATIONS } from "@core/utils/form.utils";
import { ActivatedRoute, Router } from '@angular/router';
import { Moment } from 'moment';
import * as moment from 'moment';
import { PatientSearchDto, GenderDto, IdentificationTypeDto } from '@api-rest/api-model';
import { PatientService } from '@api-rest/services/patient.service';
import { PersonMasterDataService } from '@api-rest/services/person-master-data.service';
import { TableModel } from 'src/app/modules/presentation/components/table/table.component';
import { momentFormatDate, DateFormat } from '@core/utils/moment.utils';
import { PersonService } from '@api-rest/services/person.service';
import { finalize } from 'rxjs/operators';
import { MatDialog } from '@angular/material/dialog';
import { ViewPatientDetailComponent } from '../../component/view-patient-detail/view-patient-detail.component';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { ContextService } from "@core/services/context.service";

const ROUTE_NEW = 'pacientes/new';
const ROUTE_HOME = 'pacientes';

@Component({
	selector: 'app-search',
	templateUrl: './search.component.html',
	styleUrls: ['./search.component.scss']
})
export class SearchComponent implements OnInit {

	today: Moment = moment();
	public formSearchSubmitted: boolean = false;
	public formSearch: FormGroup;
	public identifyTypeArray: IdentificationTypeDto[];
	public identifyTypeViewPatientDetail = {};
	public genderOptions: GenderDto[];
	public genderOptionsViewTable = {};
	public viewSearch: boolean = true;
	public isLoading: boolean = true;
	public hasError = hasError;
	public identificationTypeId;
	public identificationNumber;
	public genderId;
	public matchingPatient: TableModel<PatientSearchDto>;
	public searchPatient;
	private readonly routePrefix;

	constructor(private formBuilder: FormBuilder,
				private patientService: PatientService,
				private personService: PersonService,
				private router: Router,
				private route: ActivatedRoute,
				private personMasterDataService: PersonMasterDataService,
				private snackBarService: SnackBarService,
				public dialog: MatDialog,
				private contextService: ContextService) {
		this.routePrefix = 'institucion/' + this.contextService.institutionId + '/';
	}

	ngOnInit(): void {
		this.route.queryParams.subscribe(params => {
			this.identificationTypeId = params['identificationTypeId'];
			this.identificationNumber = params['identificationNumber'];
			this.genderId = params['genderId'];

			this.buildFormSearch(params);

			this.personService.getRenaperPersonData({
				identificationNumber: this.identificationNumber,
				genderId: this.genderId
			})
				.pipe(finalize(() => this.isLoading = false))
				.subscribe(
					personData => {
						if (personData) {
							let personToAdd: any = {...personData};
							personToAdd.identificationTypeId = this.identificationTypeId;
							personToAdd.identificationNumber = this.identificationNumber;
							personToAdd.genderId = this.genderId;
							this.goToNextState(personToAdd);
						}
					}, () => {
						this.snackBarService.showError('pacientes.search.RENAPER_TIMEOUT')
					});

			this.personMasterDataService.getIdentificationTypes().subscribe(
				identificationTypes => {
					this.identifyTypeArray = identificationTypes;
					identificationTypes.forEach(identificationType => {
						this.identifyTypeViewPatientDetail[identificationType.id] = identificationType.description
					});
				});

			this.personMasterDataService.getGenders().subscribe(
				genders => {
					this.genderOptions = genders;
					genders.forEach(gender => {
						this.genderOptionsViewTable[gender.id] = gender.description
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
					text: (row) => momentFormatDate(new Date(row.person.birthDate), DateFormat.VIEW_DATE)
				},
				{
					columnDef: 'numberDni',
					header: 'Nro. Documento',
					text: (row) => row.person.identificationNumber
				},
				{
					columnDef: 'state',
					header: 'Estado',
					text: (row) => (row.activo ? "Activo" : "Inactivo")
				},
				{
					columnDef: 'ranking',
					header: 'Coincidencia',
					text: (row) => row.ranking + " %"
				},
				{
					columnDef: 'action',
					action: {
						text: 'Ver',
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
		const dialogRef = this.dialog.open(ViewPatientDetailComponent, {
			width: '450px',
			data: {
				id: patient.idPatient,
				firstName: patient.person.firstName,
				lastName: patient.person.lastName,
				age: this.CalculateAge(new Date(patient.person.birthDate)),
				gender: this.genderOptionsViewTable[patient.person.genderId],
				birthDate: momentFormatDate(new Date(patient.person.birthDate), DateFormat.VIEW_DATE),
				identificationNumber: patient.person.identificationNumber,
				identificationTypeId: this.identifyTypeViewPatientDetail[patient.person.identificationTypeId]
			}
		});
	}

	private buildFormSearch(params) {
		this.formSearch = this.formBuilder.group({
			identificationNumber: [params.identificationNumber, [Validators.required, Validators.maxLength(VALIDATIONS.MAX_LENGTH.identif_number)]],
			identificationTypeId: [Number(params.identificationTypeId), Validators.required],
			firstName: [params.firstName, Validators.required],
			middleNames: [params.middleNames],
			lastName: [params.lastName, Validators.required],
			otherLastNames: [params.otherLastNames],
			genderId: [Number(params.genderId), Validators.required],
			birthDate: [params.birthDate, Validators.required]
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
		if (params.firstName) {
			this.formSearch.controls.firstName.disable();
		}
		if (params.lastName) {
			this.formSearch.controls.lastName.disable();
		}

	}

	private CalculateAge(birthDate): number {
		const today: Date = new Date();
		const birth: Date = new Date(birthDate);
		let age: number = today.getFullYear() - birth.getFullYear();
		const month: number = today.getMonth() - birth.getMonth();
		if (month < 0 || (month === 0 && today.getDate() < birth.getDate())) {
			age--;
		}
		return age;
	}

	back() {
		this.router.navigate([this.routePrefix + ROUTE_HOME])
	}

	submit() {
		this.formSearchSubmitted = true;
		if (this.formSearch.valid) {
			this.searchPatient = {
				firstName: this.formSearch.controls.firstName.value,
				lastName: this.formSearch.controls.lastName.value,
				genderId: this.formSearch.controls.genderId.value,
				identificationTypeId: this.formSearch.controls.identificationTypeId.value,
				identificationNumber: this.formSearch.controls.identificationNumber.value,
				birthDate: this.formSearch.controls.birthDate.value.format(DateFormat.API_DATE),
				otherLastNames: this.formSearch.controls.otherLastNames.value,
				middleNames: this.formSearch.controls.middleNames.value
			}
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
		this.router.navigate([this.routePrefix + ROUTE_NEW], {
			queryParams: person
		});
	}

	goToNewPatient() {
		this.goToAddPatient(this.searchPatient)
	}

	viewSearchComponent(): boolean {
		return this.viewSearch;
	}

	showSpinner(): boolean {
		return this.isLoading;
	}

}
