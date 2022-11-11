import { Component, OnInit } from '@angular/core';
import { AbstractControl, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { PersonMasterDataService } from '@api-rest/services/person-master-data.service';
import { GenderDto, IdentificationTypeDto, LimitedPatientSearchDto, PatientSearchDto } from '@api-rest/api-model';
import { AppFeature } from '@api-rest/api-model';
import { atLeastOneValueInFormGroup, hasError, } from '@core/utils/form.utils';
import { Moment } from 'moment';
import { DateFormat, momentFormat, momentParseDateTime, newMoment } from '@core/utils/moment.utils';
import { Router } from '@angular/router';
import { ContextService } from '@core/services/context.service';
import { PatientService, PersonInformationRequest } from '@api-rest/services/patient.service';
import { PERSON, REMOVE_SUBSTRING_DNI } from '@core/constants/validation-constants';
import { MIN_DATE } from "@core/utils/date.utils";
import { FeatureFlagService } from "@core/services/feature-flag.service";
import { IDENTIFICATION_TYPE_IDS } from '@core/utils/patient.utils';
import { TableModel, ActionDisplays } from '@presentation/components/table/table.component';
import { PatientNameService } from '@core/services/patient-name.service';

@Component({
	selector: 'app-home',
	templateUrl: './home.component.html',
	styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {
	patientData: PatientSearchDto[] = [];
	genderTableView: string[] = [];
	public personalInformationForm: FormGroup;
	public genders: GenderDto[];
	public identificationTypeList: IdentificationTypeDto[];
	public hasError = hasError;
	public today: Moment = newMoment();
	public formSubmitted: boolean;
	public requiringValues: boolean;
	public patientResultsLength: number;
	public readonly validations = PERSON;
	readonly MAX_RESULT_SIZE: number = 150;
	nameSelfDeterminationEnabled: boolean;
	minDate = MIN_DATE;
	requiringAtLeastOneMoreValue: boolean;
	ffOfCardsIsOn: boolean;
	public tableModel: TableModel<PatientSearchDto>;

	private readonly routePrefix;

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
		this.featureFlagService.isActive(AppFeature.HABILITAR_DATOS_AUTOPERCIBIDOS).subscribe(isEnabled => {
			this.nameSelfDeterminationEnabled = isEnabled
		});
	}

	ngOnInit(): void {
		this.initPersonalInformationForm();
		this.setMasterData();
	}

	private initPersonalInformationForm() {
		this.personalInformationForm = this.formBuilder.group({
			firstName: [null, [Validators.maxLength(PERSON.MAX_LENGTH.firstName), Validators.pattern(/^\S*$/)]],
			middleNames: [null, [Validators.maxLength(PERSON.MAX_LENGTH.middleNames), Validators.pattern(/^\S*$/)]],
			lastName: [null, [Validators.maxLength(PERSON.MAX_LENGTH.lastName), Validators.pattern(/^\S*$/)]],
			otherLastNames: [null, [Validators.maxLength(PERSON.MAX_LENGTH.otherLastNames), Validators.pattern(/^\S*$/)]],
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
			this.personalInformationForm.value.identificationNumber = this.personalInformationForm.value.identificationNumber ? +this.personalInformationForm.value.identificationNumber.replace(REMOVE_SUBSTRING_DNI,'') : null;
			const personalInformationReq: PersonInformationRequest = this.personalInformationForm.value;
			this.patientService.searchPatientOptionalFilters(personalInformationReq)
				.subscribe((data: LimitedPatientSearchDto) => {
					this.featureFlagService.isActive(AppFeature.HABILITAR_VISUALIZACION_DE_CARDS).subscribe(isEnabled => {
						this.ffOfCardsIsOn = isEnabled;
						if (this.ffOfCardsIsOn)
							this.patientData = data.patientList;
						else
							this.tableModel = this.buildTable(data.patientList);
						this.patientResultsLength = data.actualPatientSearchSize;
					});
				});
		}
	}

	private buildTable(data: PatientSearchDto[]): TableModel<any> {
		if (this.nameSelfDeterminationEnabled) {
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

}
