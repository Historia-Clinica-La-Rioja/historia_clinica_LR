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
	public genderOptions: GenderDto[];
	public genderOptionsViewTable = {};
	public viewSearch: boolean = true;
	public isLoading: boolean = true;
	public hasError = hasError;
	public identificationTypeId;
	public identificationNumber;
	public genderId;
	public matchingPatient: TableModel<PatientSearchDto>;

	constructor(private formBuilder: FormBuilder,
		private patientService: PatientService,
		private personService: PersonService,
		private router: Router,
		private route: ActivatedRoute,
		private personMasterDataService: PersonMasterDataService
	) {
	}

	ngOnInit(): void {
		this.route.queryParams.subscribe(params => {
			this.identificationTypeId = params['identificationTypeId'];
			this.identificationNumber = params['identificationNumber'];
			this.genderId = params['genderId'];

			this.buildFormSearch();

			this.personService.getRenaperPersonData({ identificationNumber: this.identificationNumber, genderId: this.genderId })
				.pipe(finalize(() => this.isLoading = false))
				.subscribe(
					personData => {
						if (personData) {
							let personToAdd: any = { ...personData };
							personToAdd.identificationType = this.identificationTypeId;
							personToAdd.identificationNumber = this.identificationNumber;
							personToAdd.genderId = this.genderId;
							this.goToNextState(personToAdd);
						}
					});
			let startDate = params['birthDate'] ? moment(params['birthDate']) : null;
			this.formSearch = this.formBuilder.group({
				identificationNumber: [this.identificationNumber, [Validators.required, Validators.maxLength(VALIDATIONS.MAX_LENGTH.identif_number)]],
				identificationType: [Number(this.identificationTypeId), Validators.required],
				firstName: [params['firstName'], Validators.required],
				middleNames: [params['middleNames']],
				lastName: [params['lastName'], Validators.required],
				otherLastNames: params['otherLastNames'],
				gender: [Number(this.genderId), Validators.required],
				birthDate: [startDate, Validators.required]
			});

			this.personMasterDataService.getIdentificationTypes().subscribe(
				identificationTypes => { this.identifyTypeArray = identificationTypes; });

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
						do: (internacion) => {
							let url = `pacientes`;
							this.router.navigate([url]);
						}
					}
				},
			],
			data,
			enableFilter: true
		};
	}


	private buildFormSearch() {
		this.formSearch = this.formBuilder.group({
			identificationNumber: [this.identificationNumber, [Validators.required, Validators.maxLength(VALIDATIONS.MAX_LENGTH.identif_number)]],
			identificationType: [Number(this.identificationTypeId), Validators.required],
			firstName: [null, Validators.required],
			middleNames: [null],
			lastName: [null, Validators.required],
			otherLastNames: [null],
			gender: [Number(this.genderId), Validators.required],
			birthDate: [null, Validators.required]
		});
	}

	back() {
		this.router.navigate([ROUTE_HOME])
	}

	submit() {
		this.formSearchSubmitted = true;
		if (this.formSearch.valid) {
			let searchRequest = {
				firstName: this.formSearch.controls.firstName.value,
				lastName: this.formSearch.controls.lastName.value,
				genderId: this.formSearch.controls.gender.value,
				identificationTypeId: this.formSearch.controls.identificationType.value,
				identificationNumber: this.formSearch.controls.identificationNumber.value,
				birthDate: this.formSearch.controls.birthDate.value.format(DateFormat.API_DATE),
				otherLastNames: this.formSearch.controls.otherLastNames.value,
				middleNames: this.formSearch.controls.middleNames.value
			}
			this.goToNextState(searchRequest);
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

	private goToAddPatient(personToSearch: any) {
		this.router.navigate([ROUTE_NEW], {
			queryParams: {
				identificationTypeId: personToSearch.identificationTypeId,
				identificationNumber: personToSearch.identificationNumber,
				genderId: personToSearch.genderId,
				firstName: personToSearch.firstName,
				lastName: personToSearch.lastName,
				middleNames: personToSearch.middleNames,
				birthDate: personToSearch.birthDate,
				otherLastNames: personToSearch.otherLastNames
			}
		});
	}

	viewSearchComponent(): boolean {
		return this.viewSearch;
	}

	showSpinner(): boolean {
		return this.isLoading;
	}

}
