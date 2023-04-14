import { Component, OnDestroy, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { DuplicatePatientDto, IdentificationTypeDto, PatientPersonalInfoDto, PatientToMergeDto, PatientType } from '@api-rest/api-model';
import { PersonMasterDataService } from '@api-rest/services/person-master-data.service';
import { ContextService } from '@core/services/context.service';
import { PatientAuditService } from '../../services/patient-audit.service';
import { AuditPatientService } from '@api-rest/services/audit-patient.service';
import { Observable, of } from 'rxjs';
import { PatientMasterDataService } from '@api-rest/services/patient-master-data.service';
import { PatientMergeService } from '@api-rest/services/patient-merge.service';
import { MatDialog } from '@angular/material/dialog';
import { WarningFusionComponent } from '../../dialogs/warning-fusion/warning-fusion.component';
import { ConfirmedFusionComponent } from '../../dialogs/confirmed-fusion/confirmed-fusion.component';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { PAGE_MIN_SIZE } from '@historia-clinica/modules/ambulatoria/modules/indicacion/constants/internment-indications';
import { PAGE_SIZE_OPTIONS } from '@historia-clinica/modules/ambulatoria/modules/indicacion/constants/internment-indications';
import { Filters } from '../control-patient-duplicate/control-patient-duplicate.component';

const ROUTE_CONTROL_PATIENT_DUPLICATE = "auditoria/control-pacientes-duplicados"

@Component({
	selector: 'app-patient-fusion',
	templateUrl: './patient-fusion.component.html',
	styleUrls: ['./patient-fusion.component.scss']
})
export class PatientFusionComponent implements OnInit {
	private readonly routePrefix;
	readonly pageSizeOptions: number[] = PAGE_SIZE_OPTIONS;
	listPatientData$: Observable<PatientPersonalInfoDto[]>;
	listPatientData: PatientPersonalInfoDto[];
	identificationTypeList: IdentificationTypeDto[];
	patientToAudit: DuplicatePatientDto;
	patientsTypes: PatientType[];
	keyAttributes = KeyAttributes;
	oldPatientsIds: number[] = [];
	pageSliceObs$: Observable<PatientPersonalInfoDto[]>;
	numberOfPatients = 0;
	pageSlice: PatientPersonalInfoDto[];
	initialSize: Observable<any>;
	filterBy: Filters;
	infoPatientToAudit: string;
	filters = Filters;
	validationTwoSelectedPatients: boolean = false;
	validationColumns: boolean = false;
	patientToMerge: PatientToMergeDto = {
		activePatientId: null,
		oldPatientsIds: null,
		registrationDataPerson: {
			genderId: null,
			nameSelfDetermination: null,
			phonePrefix: null,
			birthDate: null,
			firstName: null,
			identificationNumber: null,
			identificationTypeId: null,
			lastName: null,
			middleNames: null,
			otherLastNames: null,
			phoneNumber: null,
		},
	}

	constructor(private router: Router, private contextService: ContextService, private personMasterDataService: PersonMasterDataService,
		private patientAuditService: PatientAuditService, private auditPatientService: AuditPatientService,
		private patientMasterDataService: PatientMasterDataService, private patientMergeService: PatientMergeService, private dialog: MatDialog,
		private readonly snackBarService: SnackBarService) {
		this.routePrefix = `institucion/${this.contextService.institutionId}/`;

	}

	ngOnInit(): void {
		this.patientAuditService.patientToAudit$.subscribe(patientToAudit => {
			this.patientToAudit = patientToAudit
		});

		this.personMasterDataService.getIdentificationTypes()
			.subscribe(identificationTypes => {
				this.identificationTypeList = identificationTypes;
				this.setInfo();
			});

		this.patientMasterDataService.getTypesPatient().subscribe((patientsTypes: PatientType[]) => {
			this.patientsTypes = patientsTypes;
		})

		this.patientAuditService.filterBySubject$.subscribe(filter => {
			this.filterBy = filter;
		})

		this.auditPatientService.getPatientPersonalInfo(this.patientToAudit).subscribe((patientPersonalData: PatientPersonalInfoDto[]) => {
			this.listPatientData = this.setListPatientData(patientPersonalData);
			this.listPatientData$ = of(this.listPatientData);

			this.pageSlice = this.listPatientData.slice(0, PAGE_MIN_SIZE);
			this.numberOfPatients = this.listPatientData.length || 0;
			this.initialSize = of(PAGE_MIN_SIZE);
		})


	}

	setInfo() {
		this.infoPatientToAudit = 'Filtrado por ' + this.patientToAudit.firstName + " ";
		if (this.patientToAudit?.middleNames) {
			this.infoPatientToAudit += this.patientToAudit.middleNames + " ";
		}
		this.infoPatientToAudit += this.patientToAudit.lastName + " ";
		if (this.patientToAudit?.otherLastNames) {
			this.infoPatientToAudit += this.patientToAudit.otherLastNames;
		}
		switch (this.filterBy) {
			case this.filters.FILTER_FULLNAME_DNI:
				this.infoPatientToAudit += " | " + this.getIdentificationType(this.patientToAudit?.identificationTypeId) + " " + this.patientToAudit.identificationNumber;
				break;
			case this.filters.FILTER_FULLNAME_BIRTHDATE:
				this.infoPatientToAudit += " | Fecha Nac. " + this.patientToAudit?.birthdate;
				break;
			case this.filters.FILTER_FULLNAME_BIRTHDATE_DNI:
				this.infoPatientToAudit += " | " + this.getIdentificationType(this.patientToAudit?.identificationTypeId) + " " + this.patientToAudit.identificationNumber + " | Fecha Nac. " + this.patientToAudit?.birthdate;
				break;
			case this.filters.FILTER_DNI:
				this.infoPatientToAudit = " ";
				this.infoPatientToAudit = this.getIdentificationType(this.patientToAudit?.identificationTypeId) + this.patientToAudit.identificationNumber;
				break;
		}
	}

	setListPatientData(patientPersonalData): PatientPersonalInfoDto[] {
		patientPersonalData.forEach(data => {
			data.selected = false;
		})
		return patientPersonalData
	}

	goToBack() {
		this.router.navigate([this.routePrefix + ROUTE_CONTROL_PATIENT_DUPLICATE])
	}

	getIdentificationType(value: number) {
		return this.identificationTypeList?.find(type => type.id === value).description
	}

	getPatientType(value: number) {
		return this.patientsTypes?.find(type => type.id === value).description
	}

	setSelectedPatient(patient: any) {
		patient.selected = !patient.selected;
		let index = this.oldPatientsIds?.indexOf(patient.patientId)
		if (index !== -1) {
			this.oldPatientsIds.splice(index, 1);
		} else {
			this.oldPatientsIds.push(patient.patientId);
		}
	}

	setValuesPatientFusion(key: any, value1: any, value2?: any) {
		switch (key) {
			case this.keyAttributes.PATIENT_ID:
				this.patientToMerge.activePatientId = value1;
				break;
			case this.keyAttributes.BIRTHDATE:
				this.patientToMerge.registrationDataPerson.birthDate = value1;
				break;
			case this.keyAttributes.NAMES:
				this.patientToMerge.registrationDataPerson.firstName = value1;
				this.patientToMerge.registrationDataPerson.middleNames = value2;
				break;
			case this.keyAttributes.LASTNAMES:
				this.patientToMerge.registrationDataPerson.lastName = value1;
				this.patientToMerge.registrationDataPerson.otherLastNames = value2;
				break;
			case this.keyAttributes.IDENTIFICATION:
				this.patientToMerge.registrationDataPerson.identificationTypeId = value1;
				this.patientToMerge.registrationDataPerson.identificationNumber = value2;
				break;
		}
	}

	merge() {
		this.validateForm();
		if (!this.validationColumns && !this.validationTwoSelectedPatients) {
			this.completePatientDataToMerge();
			const dialogRef = this.dialog.open(WarningFusionComponent, {
				data: {
					cant: this.oldPatientsIds.length + 1,
					fullName: '-' + (this.patientToMerge.registrationDataPerson.firstName) + " " + (this.patientToMerge.registrationDataPerson.middleNames ? this.patientToMerge.registrationDataPerson.middleNames : '') + ' ' + (this.patientToMerge.registrationDataPerson.lastName) + " " + (this.patientToMerge.registrationDataPerson.otherLastNames ? this.patientToMerge.registrationDataPerson.otherLastNames : ''),
					identification: '-' + this.getIdentificationType(this.patientToMerge.registrationDataPerson.identificationTypeId) + ' ' + this.patientToMerge.registrationDataPerson.identificationNumber,
					birthDate: '- Fecha Nac. ' + this.patientToMerge.registrationDataPerson.birthDate,
					idPatient: '- ID ' + this.patientToMerge.activePatientId,

				},
				disableClose: true,
				width: '35%',
				autoFocus: false
			})
			dialogRef.afterClosed().subscribe(confirmed => {
				if (confirmed) {
					this.patientMergeService.merge(this.patientToMerge).subscribe(res => {
						const dialogRef2 = this.dialog.open(ConfirmedFusionComponent, {
							data: {
								idPatient: this.patientToMerge.activePatientId
							},
							disableClose: true,
							width: '35%',
							autoFocus: false
						})
						dialogRef2.afterClosed().subscribe(close => {
							this.goToBack();
						})
					}, error => {
						this.snackBarService.showError(error.text);
					})
				}
			});
		}
	}

	validateForm() {
		if (this.oldPatientsIds.length >= 2) {
			this.validationTwoSelectedPatients = false;
		} else {
			this.validationTwoSelectedPatients = true;
		}
		if (this.patientToMerge.activePatientId === null || this.patientToMerge.registrationDataPerson.birthDate === null || this.patientToMerge.registrationDataPerson.firstName === null
			|| this.patientToMerge.registrationDataPerson.identificationNumber === null || this.patientToMerge.registrationDataPerson.lastName === null) {
			this.validationColumns = true;
		} else {
			this.validationColumns = false;
		}

	}

	completePatientDataToMerge() {
		let auxiliaryPatientList: PatientPersonalInfoDto[];
		this.listPatientData$.subscribe(list => {
			auxiliaryPatientList = list;
		})
		this.patientToMerge.registrationDataPerson.genderId = auxiliaryPatientList.find(patient => patient.patientId === this.patientToMerge.activePatientId).genderId;
		this.patientToMerge.registrationDataPerson.phonePrefix = auxiliaryPatientList.find(patient => patient.patientId === this.patientToMerge.activePatientId).phonePrefix;
		this.patientToMerge.registrationDataPerson.phoneNumber = auxiliaryPatientList.find(patient => patient.patientId === this.patientToMerge.activePatientId).phoneNumber;
		this.patientToMerge.registrationDataPerson.nameSelfDetermination = auxiliaryPatientList.find(patient => patient.patientId === this.patientToMerge.activePatientId).nameSelfDetermination;
		this.patientToMerge.oldPatientsIds = this.oldPatientsIds;

		this.oldPatientsIds.splice(this.oldPatientsIds?.indexOf(this.patientToMerge.activePatientId), 1);
	}


	onPageChange($event: any): void {
		const page = $event;
		const startPage = page.pageIndex * page.pageSize;
		this.pageSlice = this.listPatientData.slice(startPage, $event.pageSize + startPage);
	}

}
export enum KeyAttributes {
	BIRTHDATE,
	NAMES,
	LASTNAMES,
	PATIENT_ID,
	IDENTIFICATION,
}
