import { Component, Input } from '@angular/core';
import { MatDialog } from "@angular/material/dialog";
import { Router } from '@angular/router';
import { AppFeature, EAuditType, ERole } from '@api-rest/api-model';
import { IdentificationTypeDto, MasterDataDto, PatientSearchDto, PatientType } from '@api-rest/api-model';
import { PatientMasterDataService } from '@api-rest/services/patient-master-data.service';
import { PersonMasterDataService } from '@api-rest/services/person-master-data.service';
import { ContextService } from '@core/services/context.service';
import { FeatureFlagService } from '@core/services/feature-flag.service';
import { PatientNameService } from "@core/services/patient-name.service";
import { PermissionsService } from '@core/services/permissions.service';
import { anyMatch } from '@core/utils/array.utils';
import { CardModel, ValueAction } from '@presentation/components/card/card.component';
import { Observable, of } from 'rxjs';
import { PatientProfilePopupComponent } from '../../../auditoria/dialogs/patient-profile-popup/patient-profile-popup.component';
import { ROUTE_EMPADRONAMIENTO, ROUTE_UNLINK_PATIENT } from '../../../auditoria/routes/home/home.component';
import { ViewPatientDetailComponent } from '../view-patient-detail/view-patient-detail.component';
import { dateISOParseDate, newDate } from '@core/utils/moment.utils';
import { differenceInYears } from 'date-fns';

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
		private readonly contextService: ContextService,
		private readonly permissionsService: PermissionsService,
		private readonly featureFlagService: FeatureFlagService,
		private readonly patientMasterDataService: PatientMasterDataService,
		public readonly router: Router,
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

	private mapToPatientContent(): CardPatient[] {
		let medicalSpecialist = false;
		let legalPerson = false;
		let auditor = false;
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
					ERole.ABORDAJE_VIOLENCIAS
				]);
			legalPerson = anyMatch<ERole>(userRoles, [ERole.PERSONAL_DE_LEGALES]);
			auditor = anyMatch<ERole>(userRoles, [ERole.AUDITOR_MPI]);
		});

		return this.patientData?.map((patient: any) => {
			let header;
			if ((this.router.url.includes(ROUTE_EMPADRONAMIENTO) || this.router.url.includes(ROUTE_UNLINK_PATIENT)) && this.nameSelfDeterminationFF) {
				header = [{ title: this.patientNameService.getFullName(patient.person.firstName, null, patient.person?.middleNames) + ' ' + this.getLastNames(patient), value: patient.nameSelfDetermination ? patient.nameSelfDetermination + " (autopercibido)" : " " }];
			} else {
				header = [{ title: " ", value: this.patientNameService.getFullName(patient.person.firstName, patient.person.nameSelfDetermination, patient.person?.middleNames) + ' ' + this.getLastNames(patient) }];
			}

			return {
				cardModel: {
					header: header,
					id: patient.idPatient,
					identificationTypeId: patient.person.identificationTypeId,
					dni: patient.person.identificationNumber || "Sin Información",
					gender: this.genderTableView.find(p => p?.id === patient.person.genderId)?.description,
					date: new Date(patient.person.birthDate),
					ranking: patient?.ranking,
					patientTypeId: patient?.patientTypeId,
					auditType: patient?.auditType,
					actions: this.setActionsByRole(medicalSpecialist, legalPerson, auditor, patient.idPatient),
				},
				numberOfMergedPatients: patient.numberOfMergedPatients,
			}
		})
	}
	private getLastNames(patient: PatientSearchDto): string {
		const lastName = patient.person?.lastName ? patient.person.lastName : '';
		const otherLastNames = patient.person?.otherLastNames ? patient.person.otherLastNames : '';
		return `${lastName} ${otherLastNames}`;
	}

	onPageChange($event: any): void {
		const page = $event;
		const startPage = page.pageIndex * page.pageSize;
		this.pageSlice = this.patientContent.slice(startPage, $event.pageSize + startPage);
	}

	private setActionsByRole(medicalSpecialist: boolean, legalPerson: boolean, auditor: boolean, idPatient: number): ValueAction[] {
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
		if (!legalPerson && !medicalSpecialist && !auditor)
			valueActions.push({
				display: 'ambulatoria.card-patient.VIEW_BUTTON',
				do: `${this.routePrefix}paciente/profile/${idPatient}`
			});
		if (auditor && !this.router.url.includes(ROUTE_EMPADRONAMIENTO))
			valueActions.push({
				display: 'pacientes.audit.LABEL_BUTTON_AUDIT',
				do: `home/auditoria/desvincular-pacientes/${idPatient}`
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
					birthDate: patient.person.birthDate,
					identificationNumber: patient.person.identificationNumber,
					identificationTypeId: this.identificationTypes.find(i => i.id === patient.person.identificationTypeId)?.description,
					personAge: patient.person.personAge
				}

			})

			function calculateAge(birthDate: string): number {
				const today = newDate();
				const birth = dateISOParseDate(birthDate)
				const result = differenceInYears(today, birth)
				return result;
			}

		} else {
			this.dialog.open(PatientProfilePopupComponent, {
				data: {
					patientId: idPatient,
					viewCardToAudit: this.viewCardToAudit
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

interface CardPatient {
	cardModel: CardModel,
	numberOfMergedPatients: number,
}
