import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { PersonMasterDataService } from '@api-rest/services/person-master-data.service';
import { GenderDto, IdentificationTypeDto, PatientSearchDto } from '@api-rest/api-model';
import { atLeastOneValueInFormGroup, hasError, } from '@core/utils/form.utils';
import { Moment } from 'moment';
import { ActionDisplays, TableModel } from '@presentation/components/table/table.component';
import { DateFormat, momentFormatDate, newMoment } from '@core/utils/moment.utils';
import { Router } from '@angular/router';
import { ContextService } from '@core/services/context.service';
import { PatientService, PersonInformationRequest } from '@api-rest/services/patient.service';
import { PERSON } from '@core/constants/validation-constants';

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
  public readonly validations = PERSON;

  private readonly routePrefix;
  private genderTableView: string[] = [];

  constructor(
	private readonly formBuilder: FormBuilder,
	private readonly personMasterDataService: PersonMasterDataService,
	private readonly patientService: PatientService,
	private readonly router: Router,
	private readonly contextService: ContextService,
  ) {
  	this.routePrefix = `institucion/${this.contextService.institutionId}/`;
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
			birthDate: []});
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
			.subscribe( data =>
					this.tableModel = this.buildTable(data));
	}

  }

  private buildTable(data: PatientSearchDto[]): TableModel<any> {
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
					text: (row) => (row.person.birthDate === undefined) ? '' : momentFormatDate(new Date(row.person.birthDate), DateFormat.VIEW_DATE)
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
							const url = `${this.routePrefix}ambulatoria/paciente/${row.idPatient}/profile`;
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
