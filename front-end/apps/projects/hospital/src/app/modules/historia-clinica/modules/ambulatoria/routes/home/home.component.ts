import { Component, OnInit } from '@angular/core';
import { AbstractControl, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { PersonMasterDataService } from '@api-rest/services/person-master-data.service';
import { GenderDto, IdentificationTypeDto, LimitedPatientSearchDto, PatientSearchDto } from '@api-rest/api-model';
import { AppFeature } from '@api-rest/api-model';
import { atLeastOneValueInFormGroup, hasError, } from '@core/utils/form.utils';
import { Moment } from 'moment';
import { ActionDisplays, TableModel } from '@presentation/components/table/table.component';
import { DateFormat, momentFormat, momentParseDateTime, newMoment } from '@core/utils/moment.utils';
import { Router } from '@angular/router';
import { ContextService } from '@core/services/context.service';
import { PatientService, PersonInformationRequest } from '@api-rest/services/patient.service';
import { PERSON } from '@core/constants/validation-constants';
import { MIN_DATE } from "@core/utils/date.utils";
import { PatientNameService } from "@core/services/patient-name.service";
import { FeatureFlagService } from "@core/services/feature-flag.service";

@Component({
	selector: 'app-home',
	templateUrl: './home.component.html',
	styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {


  public personalInformationForm: FormGroup;
  public genders: GenderDto[];
  public identificationTypeList: IdentificationTypeDto[];
  public hasError = hasError;
  public today: Moment = newMoment();
  public tableModel: TableModel<PatientSearchDto>;
  public formSubmitted: boolean;
  public requiringValues: boolean;
  public patientResultsLength: number;
  public readonly validations = PERSON;
  readonly MAX_RESULT_SIZE: number = 150;
  nameSelfDeterminationEnabled: boolean;

  private readonly routePrefix;
  private genderTableView: string[] = [];

  minDate = MIN_DATE;

  constructor(
	private readonly formBuilder: FormBuilder,
	private readonly personMasterDataService: PersonMasterDataService,
	private readonly patientService: PatientService,
	private readonly router: Router,
	private readonly contextService: ContextService,
	private readonly patientNameService: PatientNameService,
	private readonly featureFlagService: FeatureFlagService,
  ) {
  	this.routePrefix = `institucion/${this.contextService.institutionId}/`;
  	this.featureFlagService.isActive(AppFeature.HABILITAR_NOMBRE_AUTOPERCIBIDO).subscribe(isEnabled => this.nameSelfDeterminationEnabled = isEnabled);
  }

  ngOnInit(): void {
	  this.initPersonalInformationForm();
	  this.setMasterData();
  }

  private initPersonalInformationForm() {
	this.personalInformationForm = this.formBuilder.group({
			firstName: [null, Validators.maxLength(PERSON.MAX_LENGTH.firstName)],
			middleNames: [null, Validators.maxLength(PERSON.MAX_LENGTH.middleNames)],
			lastName: [null, Validators.maxLength(PERSON.MAX_LENGTH.lastName)],
			otherLastNames: [null, Validators.maxLength(PERSON.MAX_LENGTH.otherLastNames)],
			genderId: [],
			identificationNumber: [null, Validators.maxLength(PERSON.MAX_LENGTH.identificationNumber)],
			identificationTypeId: [],
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
		const atLeastOneValueSet: boolean = atLeastOneValueInFormGroup(this.personalInformationForm);
		if (!atLeastOneValueSet) {
			this.formSubmitted = false;
			this.requiringValues = true;
			return;
		}

		if ((this.personalInformationForm.valid)) {
			this.formSubmitted = true;
			this.requiringValues = false;
			const personalInformationReq: PersonInformationRequest = this.personalInformationForm.value;
			this.patientService.searchPatientOptionalFilters(personalInformationReq)
				.subscribe((data: LimitedPatientSearchDto) => {
					this.tableModel = this.buildTable(data.patientList);
					this.patientResultsLength = data.actualPatientSearchSize;
				});
		}

	}

  private buildTable(data: PatientSearchDto[]): TableModel<any> {
		if(this.nameSelfDeterminationEnabled) {
			return {
				columns: [
					{
						columnDef: 'patiendId',
						header: 'pacientes.search.ROW_TABLE',
						text: (row) => row.idPatient
					},
					{
						columnDef: 'numberDni',
						header: 'Nro. Documento',
						text: (row) => row.person.identificationNumber
					},
					{
						columnDef: 'firstName',
						header: 'Nombre',
						text: (row) => this.patientNameService.getPatientName(row.person.firstName, row.person.nameSelfDetermination)
					},
					{
						columnDef: 'lastName',
						header: 'Apellido',
						text: (row) => row.person.lastName
					},
					{
						columnDef: 'birthDate',
						header: 'F. Nacimiento',
						text: (row) => (row.person.birthDate === undefined) ? '' : momentFormat(momentParseDateTime(String(row.person.birthDate)), DateFormat.VIEW_DATE)
					},
					{
						columnDef: 'action',
						action: {
							displayType: ActionDisplays.BUTTON,
							display: 'Ver',
							matColor: 'primary',
							do: (row) => {
								const url = `${this.routePrefix}ambulatoria/paciente/${row.idPatient}`;
								this.router.navigateByUrl(url);
							}
						}
					},
				],
				data,
				enablePagination: true
			}
		} else {
			return {
				columns: [
					{
						columnDef: 'patiendId',
						header: 'pacientes.search.ROW_TABLE',
						text: (row) => row.idPatient
					},
					{
						columnDef: 'numberDni',
						header: 'Nro. Documento',
						text: (row) => row.person.identificationNumber
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
						columnDef: 'birthDate',
						header: 'F. Nac',
						text: (row) => (row.person.birthDate === undefined) ? '' : momentFormat(momentParseDateTime(String(row.person.birthDate)), DateFormat.VIEW_DATE)
					},
					{
						columnDef: 'gender',
						header: 'Sexo',
						text: (row) => this.genderTableView[row.person.genderId]
					},
					{
						columnDef: 'action',
						action: {
							displayType: ActionDisplays.BUTTON,
							display: 'Ver',
							matColor: 'primary',
							do: (row) => {
								const url = `${this.routePrefix}ambulatoria/paciente/${row.idPatient}`;
								this.router.navigateByUrl(url);
							}
						}
					},
				],
				data,
				enablePagination: true
			};
		}
	}

	clear(control: AbstractControl): void {
		control.reset();
	}

}
