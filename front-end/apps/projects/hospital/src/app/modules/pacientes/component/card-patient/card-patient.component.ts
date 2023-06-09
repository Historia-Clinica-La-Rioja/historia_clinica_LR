import { DatePipe } from '@angular/common';
import { Component, Input } from '@angular/core';
import { EAuditType, IdentificationTypeDto, MasterDataDto, PatientSearchDto, PatientType } from '@api-rest/api-model';
import { AppFeature } from '@api-rest/api-model';
import { ERole } from '@api-rest/api-model';
import { ContextService } from '@core/services/context.service';
import { PatientNameService } from "@core/services/patient-name.service";
import { PermissionsService } from '@core/services/permissions.service';
import { anyMatch } from '@core/utils/array.utils';
import { DateFormat } from '@core/utils/date.utils';
import { CardModel, ValueAction } from '@presentation/components/card/card.component';
import { ViewPatientDetailComponent } from '../view-patient-detail/view-patient-detail.component';
import { MatDialog } from "@angular/material/dialog";
import { FeatureFlagService } from '@core/services/feature-flag.service';
import { PatientMasterDataService } from '@api-rest/services/patient-master-data.service';
import { PatientProfilePopupComponent } from '../../../auditoria/dialogs/patient-profile-popup/patient-profile-popup.component';
import { Router } from '@angular/router';
import { Observable, of } from 'rxjs';
import { PersonMasterDataService } from '@api-rest/services/person-master-data.service';
import { ROUTE_EMPADRONAMIENTO } from '../../../auditoria/routes/home/home.component';

const PAGE_SIZE_OPTIONS = [5, 10, 25];
const PAGE_MIN_SIZE = 5;

@Component({
	selector: 'app-card-patient',
	templateUrl: './card-patient.component.html',
	styleUrls: ['./card-patient.component.scss']
})
export class CardPatientComponent {
	pageSizeOptions = PAGE_SIZE_OPTIONS;
	patientContent = [];
	pageSlice = [];
	numberOfPatients = 0;
	printClinicalHistoryFFIsOn = false;
	patientsTypes: PatientType[];
	initialSize: Observable<any>;
	identificationTypeList: IdentificationTypeDto[];
	UNAUDITED = EAuditType.UNAUDITED;
	TO_AUDIT = EAuditType.TO_AUDIT;
	AUDITED = EAuditType.AUDITED;
	nameSelfDeterminationFF: boolean;
	@Input() viewCardToAudit?: boolean;
	@Input() patientData: any[] = [];
	@Input() identificationTypes: MasterDataDto[] = [];
	@Input() genderTableView: MasterDataDto[] = [];

	private readonly routePrefix;

	constructor(
		public dialog: MatDialog,
		private readonly patientNameService: PatientNameService,
		private readonly datePipe: DatePipe,
		private readonly contextService: ContextService,
		private readonly permissionsService: PermissionsService,
		private readonly featureFlagService: FeatureFlagService,
		private readonly patientMasterDataService: PatientMasterDataService,
		private readonly router: Router,
		private personMasterDataService: PersonMasterDataService
	) {
		this.routePrefix = `institucion/${this.contextService.institutionId}/`;
		this.featureFlagService.isActive(AppFeature.HABILITAR_IMPRESION_HISTORIA_CLINICA_EN_DESARROLLO).subscribe(isOn => {
			this.printClinicalHistoryFFIsOn = isOn;
		});
		this.patientMasterDataService.getTypesPatient().subscribe((patientsTypes: PatientType[]) => {
			this.patientsTypes = patientsTypes;
		})

		this.personMasterDataService.getIdentificationTypes().subscribe(identificationTypes => {
			this.identificationTypeList = identificationTypes;
		});
		this.featureFlagService.isActive(AppFeature.HABILITAR_DATOS_AUTOPERCIBIDOS).subscribe(isOn => {
			this.nameSelfDeterminationFF = isOn
		});

	}

	ngOnChanges() {
		this.patientContent = this.mapToPatientContent();
		this.numberOfPatients = this.patientContent?.length;
		this.pageSlice = this.patientContent.slice(0, PAGE_MIN_SIZE);
		this.initialSize = of(PAGE_MIN_SIZE);
	}

	private mapToPatientContent(): CardModel[] {
		let medicalSpecialist = false;
		let legalPerson = false;
		this.permissionsService.contextAssignments$().subscribe((userRoles: ERole[]) => {
			medicalSpecialist = anyMatch<ERole>(userRoles,
				[
					ERole.ESPECIALISTA_MEDICO,
					ERole.PROFESIONAL_DE_SALUD,
					ERole.ENFERMERO,
					ERole.ESPECIALISTA_EN_ODONTOLOGIA,
					ERole.PERSONAL_DE_LABORATORIO,
					ERole.PERSONAL_DE_IMAGENES,
					ERole.PERSONAL_DE_FARMACIA,
					ERole.PRESCRIPTOR,
					ERole.AUDITOR_MPI,
				]);
			legalPerson = anyMatch<ERole>(userRoles, [ERole.PERSONAL_DE_LEGALES]);
		});

		return this.patientData?.map((patient: any) => {
			let header: any = " ";
			if (this.router.url.includes(ROUTE_EMPADRONAMIENTO) && this.nameSelfDeterminationFF) {
				header = [{ title: this.patientNameService.getFullName(patient.person.firstName, null, patient.person?.middleNames) + ' ' + this.getLastNames(patient), value: patient.nameSelfDetermination? patient.nameSelfDetermination +" (autopercibido)" : " "}];
			} else {
				header = [{ title: " ", value: this.patientNameService.getFullName(patient.person.firstName, patient.person.nameSelfDetermination, patient.person?.middleNames) + ' ' + this.getLastNames(patient) }];
			}

			return {
				header: header,
				id: patient.idPatient,
				identificationTypeId: patient.person.identificationTypeId,
				dni: patient.person.identificationNumber || "-",
				gender: this.genderTableView.find(p => p?.id === patient.person.genderId)?.description,
				date: patient.person.birthDate ? this.datePipe.transform(patient.person.birthDate, DateFormat.VIEW_DATE) : '',
				ranking: patient?.ranking,
				patientTypeId: patient?.patientTypeId,
				auditType: patient?.auditType,
				actions: this.setActionsByRole(medicalSpecialist, legalPerson, patient.idPatient)
			}
		});
	}
	private getLastNames(patient: PatientSearchDto): string {
		return patient.person?.otherLastNames ? patient.person?.lastName + ' ' + patient.person?.otherLastNames : patient.person?.lastName;
	}
	onPageChange($event: any): void {
		const page = $event;
		const startPage = page.pageIndex * page.pageSize;
		this.pageSlice = this.patientContent.slice(startPage, $event.pageSize + startPage);
	}

	private setActionsByRole(medicalSpecialist: boolean, legalPerson: boolean, idPatient: number): ValueAction[] {
		const valueActions: ValueAction[] = [];
		if (legalPerson && this.printClinicalHistoryFFIsOn)
			valueActions.push({
				display: 'ambulatoria.card-patient.PRINT_HC_BUTTON',
				do: `${this.routePrefix}ambulatoria/paciente/${idPatient}/print`
			});
		if (medicalSpecialist)
			valueActions.push({
				display: 'ambulatoria.card-patient.VIEW_BUTTON',
				do: `${this.routePrefix}ambulatoria/paciente/${idPatient}`
			});
		if (!legalPerson && !medicalSpecialist)
			valueActions.push({
				display: 'ambulatoria.card-patient.VIEW_BUTTON',
				do: `${this.routePrefix}paciente/profile/${idPatient}`
			});
		return valueActions;
	}

	openDialog(idPatient: number) {
		if (this.router.url.includes('pacientes/search')) {
			const patient = this.patientData.find((p: PatientSearchDto) => p.idPatient === idPatient);
			this.dialog.open(ViewPatientDetailComponent, {
				width: '450px',
				data: {
					id: idPatient,
					firstName: this.patientNameService.getFullName(patient.person.firstName, patient.person.nameSelfDetermination, patient.person?.middleNames) + ' ' + this.getLastNames(patient),
					lastName: '',
					age: calculateAge(String(patient.person.birthDate)),
					gender: this.genderTableView.find(p => p.id === patient.person.genderId)?.description,
					birthDate: patient.person.birthDate ? this.datePipe.transform(patient.person.birthDate, DateFormat.VIEW_DATE) : '',
					identificationNumber: patient.person.identificationNumber,
					identificationTypeId: this.identificationTypes.find(i => i.id === patient.person.identificationTypeId)?.description,
				}

			})

		function calculateAge(birthDate: string): number {
				const todayDate: Date = new Date();
				const birthDateDate: Date = new Date(birthDate);
				return todayDate.getFullYear() - birthDateDate.getFullYear();
			}

		} else {
			this.dialog.open(PatientProfilePopupComponent, {
				data: {
					patientId: idPatient,
				},
				height: "600px",
				width: '30%',
				disableClose: true,
				autoFocus: false,
				panelClass: 'mat-dialog-container-fusion'
			})
		}

	}

	getIdentificationType(value: number) {
		return this.identificationTypeList?.find(type => type.id === value).description
	}

	getPatientType(value: number) {
		return this.patientsTypes?.find(type => type.id === value).description
	}


}
