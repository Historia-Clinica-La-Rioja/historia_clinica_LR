import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { Router, ActivatedRoute } from '@angular/router';
import { PatientPersonalInfoDto, IdentificationTypeDto, PatientType, PatientToMergeDto, AppFeature } from '@api-rest/api-model';
import { AuditPatientService } from '@api-rest/services/audit-patient.service';
import { PatientMasterDataService } from '@api-rest/services/patient-master-data.service';
import { PatientMergeService } from '@api-rest/services/patient-merge.service';
import { PersonMasterDataService } from '@api-rest/services/person-master-data.service';
import { FeatureFlagService } from '@core/services/feature-flag.service';
import { PAGE_SIZE_OPTIONS, PAGE_MIN_SIZE } from '@historia-clinica/modules/ambulatoria/modules/indicacion/constants/internment-indications';
import { SnackBarService } from "@presentation/services/snack-bar.service";
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
	readonly pageSizeOptions: number[] = PAGE_SIZE_OPTIONS;
	listPatientData$: Observable<PatientPersonalInfoDto[]>;
	listPatientData: PatientPersonalInfoDto[];
	identificationTypeList: IdentificationTypeDto[];
	patientToAudit: PatientPersonalInfoDto;
	patientsTypes: PatientType[];
	oldPatientsIds: number[] = [];
	numberOfPatients = 0;
	pageSlice: PatientPersonalInfoDto[];
	initialSize: Observable<any>;
	infoPatientToAudit: string;
	nameSelfDeterminationFF: boolean;
	rejectedId: number;
	patientId: number;
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
	isLoadingRequestUnmerge: boolean = false;

	constructor(private router: Router, private personMasterDataService: PersonMasterDataService,
		private auditPatientService: AuditPatientService,
		private patientMasterDataService: PatientMasterDataService, private patientMergeService: PatientMergeService, private dialog: MatDialog,
		private readonly snackBarService: SnackBarService,
		private readonly featureFlagService: FeatureFlagService,
		private route: ActivatedRoute) {

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
			this.router.navigate([ROUTE_UNLINK_PATIENT])
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
					this.isLoadingRequestUnmerge = true;
					this.patientToUnlink = this.preparePatientToUnlink(this.patientToUnlink);
					this.patientMergeService.unmerge(this.patientToUnlink).subscribe(res => {
						this.isLoadingRequestUnmerge = false;
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
					}, error => {
						this.snackBarService.showError(error.text);
						this.isLoadingRequestUnmerge = false;
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
		patientToUnlink.registrationDataPerson.genderId = this.patientToAudit.genderId;
		patientToUnlink.registrationDataPerson.nameSelfDetermination = this.patientToAudit.nameSelfDetermination;
		patientToUnlink.registrationDataPerson.birthDate = this.patientToAudit.birthdate;
		patientToUnlink.registrationDataPerson.firstName = this.patientToAudit.firstName;
		patientToUnlink.registrationDataPerson.lastName = this.patientToAudit.lastName;
		patientToUnlink.registrationDataPerson.middleNames = this.patientToAudit.middleNames;
		patientToUnlink.registrationDataPerson.otherLastNames = this.patientToAudit.otherLastNames;
		patientToUnlink.registrationDataPerson.identificationNumber = this.patientToAudit.identificationNumber;
		patientToUnlink.registrationDataPerson.identificationTypeId = this.patientToAudit.identificationTypeId;
		return patientToUnlink;
	}

	onPageChange($event: any): void {
		const page = $event;
		const startPage = page.pageIndex * page.pageSize;
		this.pageSlice = this.listPatientData.slice(startPage, $event.pageSize + startPage);
	}

}
