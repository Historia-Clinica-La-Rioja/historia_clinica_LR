import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { Router, ActivatedRoute } from '@angular/router';
import { PatientPersonalInfoDto, IdentificationTypeDto, PatientType, PatientToMergeDto, AppFeature } from '@api-rest/api-model';
import { AuditPatientService } from '@api-rest/services/audit-patient.service';
import { PatientMasterDataService } from '@api-rest/services/patient-master-data.service';
import { PatientMergeService } from '@api-rest/services/patient-merge.service';
import { PersonMasterDataService } from '@api-rest/services/person-master-data.service';
import { ContextService } from '@core/services/context.service';
import { FeatureFlagService } from '@core/services/feature-flag.service';
import { PAGE_SIZE_OPTIONS, PAGE_MIN_SIZE } from '@historia-clinica/modules/ambulatoria/modules/indicacion/constants/internment-indications';
import { Observable, of } from 'rxjs';
import { ConfirmedFusionComponent } from '../../dialogs/confirmed-fusion/confirmed-fusion.component';
import { WarningFusionComponent } from '../../dialogs/warning-fusion/warning-fusion.component';
import { ROUTE_UNLINK_PATIENT } from '../home/home.component';

const REJECTTED = "Rechazado";

@Component({
  selector: 'app-unmerge-patient',
  templateUrl: './unmerge-patient.component.html',
  styleUrls: ['./unmerge-patient.component.scss']
})
export class UnmergePatientComponent implements OnInit {
	private readonly routePrefix;
	readonly pageSizeOptions: number[] = PAGE_SIZE_OPTIONS;
	listPatientData$: Observable<PatientPersonalInfoDto[]>;
	listPatientData: PatientPersonalInfoDto[];
	identificationTypeList: IdentificationTypeDto[];
	patientToAudit: PatientPersonalInfoDto;
	patientsTypes: PatientType[];
	oldPatientsIds: number[] = [];
	pageSliceObs$: Observable<PatientPersonalInfoDto[]>;
	numberOfPatients = 0;
	pageSlice: PatientPersonalInfoDto[];
	initialSize: Observable<any>;
	infoPatientToAudit: string;
	nameSelfDeterminationFF: boolean;
	rejectedId: number;
	patientId: number;
	patientToMergeAuxKeyId: any = {
		names: null,
		identification: null,
		lastNames: null,
		birthDate: null,
		nameSelfDetermination: null
	}
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
	patientToUnlink: any = this.patientToMerge;
	validationPatientToUnlink: boolean = false;

	constructor(private router: Router, private contextService: ContextService, private personMasterDataService: PersonMasterDataService,
		private auditPatientService: AuditPatientService,
		private patientMasterDataService: PatientMasterDataService, private patientMergeService: PatientMergeService, private dialog: MatDialog,
		private readonly featureFlagService: FeatureFlagService,
		private route: ActivatedRoute) {
		this.routePrefix = `institucion/${this.contextService.institutionId}/`;

	}

	ngOnInit(): void {
		this.route.paramMap.subscribe(
			(params) => {
				this.patientId = Number(params.get('id'));
			})

		this.personMasterDataService.getIdentificationTypes()
			.subscribe(identificationTypes => {
				this.identificationTypeList = identificationTypes;

			});

		this.patientMasterDataService.getTypesPatient().subscribe((patientsTypes: PatientType[]) => {
			this.patientsTypes = patientsTypes;
			this.rejectedId = this.patientsTypes.find(type => type.description === REJECTTED).id;
		})

		this.getListPatientData();

		this.featureFlagService.isActive(AppFeature.HABILITAR_DATOS_AUTOPERCIBIDOS).subscribe(isOn => {
			this.nameSelfDeterminationFF = isOn
		});
	}

	getListPatientData() {
			this.auditPatientService.getMergedPatientPersonalInfo(this.patientId).subscribe((patientPersonalData: PatientPersonalInfoDto[]) => {
				if(patientPersonalData.length === 1){
					this.goToBack();
				}
				this.listPatientData = patientPersonalData.reverse();
				this.listPatientData$ = of(this.listPatientData);
				this.patientToAudit = this.listPatientData[0];
				this.setInitPage();
				this.setInfo();
			})
	}

	setInitPage() {
		this.pageSlice = this.listPatientData.slice(0, PAGE_MIN_SIZE);
		this.numberOfPatients = this.listPatientData.length || 0;
		this.initialSize = of(PAGE_MIN_SIZE);
	}

	setInfo() {
		this.infoPatientToAudit = this.patientToAudit?.firstName + " ";
		if (this.patientToAudit?.middleNames) {
			this.infoPatientToAudit += this.patientToAudit.middleNames + " ";
		}
		this.infoPatientToAudit += this.patientToAudit.lastName + " ";
		if (this.patientToAudit?.otherLastNames) {
			this.infoPatientToAudit += this.patientToAudit.otherLastNames;
		}
		this.infoPatientToAudit += " | " + this.getIdentificationType(this.patientToAudit?.identificationTypeId) + " " + this.patientToAudit.identificationNumber;
	}

	goToBack() {
			this.router.navigate([this.routePrefix + ROUTE_UNLINK_PATIENT])
	}

	getIdentificationType(value: number) {
		return this.identificationTypeList?.find(type => type.id === value).description
	}

	getPatientType(value: number) {
		return this.patientsTypes?.find(type => type.id === value).description
	}

	unlink() {
		if (this.patientToUnlink.patientId) {
			this.validationPatientToUnlink = false;
			const dialogRef = this.dialog.open(WarningFusionComponent, {
				data: {
					title: 'Se desvinculará de ' + this.infoPatientToAudit + ' ID ' + this.patientToAudit.patientId + ' ' + this.getIdentificationType(this.patientToAudit.identificationTypeId) + ' ' + this.patientToAudit.identificationNumber + ' (estado ' + this.getPatientType(this.patientToAudit.typeId) + ') la siguiente información:',
					cant: this.oldPatientsIds.length,
					fullName: '-' + (this.patientToUnlink.firstName) + " " + (this.patientToUnlink.middleNames ? this.patientToUnlink.middleNames : '') + ' ' + (this.patientToUnlink.lastName) + " " + (this.patientToUnlink.otherLastNames ? this.patientToUnlink.otherLastNames : ''),
					identification: '-' + this.getIdentificationType(this.patientToUnlink.identificationTypeId) + ' ' + this.patientToUnlink.identificationNumber,
					birthDate: '- Fecha Nac. ' + this.patientToUnlink.birthdate,
					idPatient: '- ID ' + this.patientToUnlink.activePatientId,
					nameSelfDetermination: this.nameSelfDeterminationFF ? this.patientToUnlink.nameSelfDetermination ? '- ' + this.patientToUnlink.nameSelfDetermination : "-" : null,
					labelButtonConfirm: 'pacientes.audit.BUTTON_YES_UNLINK'

				},
				disableClose: true,
				width: '35%',
				autoFocus: false
			})
			dialogRef.afterClosed().subscribe(confirmed => {
				if (confirmed) {
					this.patientToUnlink = this.preparePatientToUnlink(this.patientToUnlink);
					this.patientMergeService.unmerge(this.patientToUnlink).subscribe(res => {
						const dialogRef2 = this.dialog.open(ConfirmedFusionComponent, {
							data: {
								idPatients: [this.patientToUnlink.oldPatientsIds[0],this.patientToUnlink.activePatientId]
							},
							disableClose: true,
							width: '35%',
							autoFocus: false
						})
						dialogRef2.afterClosed().subscribe(close => {
							this.getListPatientData();
						})
					})
				} else {
					this.patientToUnlink = this.patientToMerge;
				}
			});
		} else {
			this.validationPatientToUnlink = true;
		}

	}

	preparePatientToUnlink(infoPatient: any):PatientToMergeDto  {
		let patientToUnlink: PatientToMergeDto = this.patientToMerge;
		patientToUnlink.activePatientId = this.patientToAudit.patientId;
		patientToUnlink.oldPatientsIds = [infoPatient.patientId]
		patientToUnlink.registrationDataPerson.genderId = infoPatient.genderId;
		patientToUnlink.registrationDataPerson.nameSelfDetermination = infoPatient.nameSelfDetermination;
		patientToUnlink.registrationDataPerson.birthDate = infoPatient.birthdate;
		patientToUnlink.registrationDataPerson.firstName = infoPatient.firstName;
		patientToUnlink.registrationDataPerson.lastName = infoPatient.lastName;
		patientToUnlink.registrationDataPerson.middleNames = infoPatient.middleNames;
		patientToUnlink.registrationDataPerson.otherLastNames = infoPatient.otherLastNames;
		patientToUnlink.registrationDataPerson.identificationNumber = infoPatient.identificationNumber;
		patientToUnlink.registrationDataPerson.identificationTypeId = infoPatient.identificationTypeId;
		return patientToUnlink;
	}

	onPageChange($event: any): void {
		const page = $event;
		const startPage = page.pageIndex * page.pageSize;
		this.pageSlice = this.listPatientData.slice(startPage, $event.pageSize + startPage);
	}

}
