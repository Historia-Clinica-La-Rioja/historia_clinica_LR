import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { Router } from "@angular/router";
import { AnamnesisSummaryDto, EpicrisisSummaryDto, EvaluationNoteSummaryDto } from '@api-rest/api-model';
import { ContextService } from "@core/services/context.service";
import { FeatureFlagService } from "@core/services/feature-flag.service";
import { SnackBarService } from "@presentation/services/snack-bar.service";
import { MatDialog } from "@angular/material/dialog";
import { ConfirmDialogComponent } from "@presentation/dialogs/confirm-dialog/confirm-dialog.component";
import { PermissionsService } from "@core/services/permissions.service";
import { InternmentEpisodeService } from "@api-rest/services/internment-episode.service";
import { anyMatch } from "@core/utils/array.utils";
import { ERole } from "@api-rest/api-model";
import { InternmentSummaryFacadeService } from '../../services/internment-summary-facade.service';

const ROUTE_INTERNMENT_EPISODE_PREFIX = 'internaciones/internacion/';
const ROUTE_RELOCATE_PATIENT_BED_PREFIX = '/pase-cama';
const ROUTE_PATIENT_SUFIX = '/paciente/';
const ROUTE_ADMINISTRATIVE_DISCHARGE_PREFIX = '/alta';


@Component({
	selector: 'app-internment-episode-summary',
	templateUrl: './internment-episode-summary.component.html',
	styleUrls: ['./internment-episode-summary.component.scss']
})
export class InternmentEpisodeSummaryComponent implements OnInit {

	currentUserIsAllowToDoAPhysicalDischarge = false;
	physicalDischargeDate: string;
	@Input() internmentEpisode: InternmentEpisodeSummary;
	@Input() canLoadProbableDischargeDate: boolean;
	@Input() patientId: number;
	@Input() showDischarge: boolean;
	@Input() showChangeDate: boolean;
	@Input() patientDocuments: InternmentDocuments;
	@Output() openInNew = new EventEmitter();

	private readonly routePrefix;
	private readonly ff: FeatureFlagService;
	anamnesisDoc: AnamnesisSummaryDto;
	epicrisisDoc: EpicrisisSummaryDto;
	lastEvolutionNoteDoc: EvaluationNoteSummaryDto;
	hasMedicalDischarge: boolean;
	hasAdministrativeDischarge = false;

	constructor(
		private router: Router,
		private contextService: ContextService,
		private readonly snackBarService: SnackBarService,
		private readonly dialog: MatDialog,
		private readonly permissionsService: PermissionsService,
		private readonly internmentService: InternmentEpisodeService,
		readonly internmentSummaryFacadeService: InternmentSummaryFacadeService,
	) {
		this.routePrefix = 'institucion/' + this.contextService.institutionId + '/';
	}
	ngOnInit(): void {
		this.permissionsService.contextAssignments$().subscribe((userRoles: ERole[]) => {
			this.currentUserIsAllowToDoAPhysicalDischarge = (anyMatch<ERole>(userRoles, [ERole.ADMINISTRADOR_DE_CAMAS]) &&
				(anyMatch<ERole>(userRoles, [ERole.ADMINISTRATIVO, ERole.ENFERMERO])));
		});
		this.loadPhysicalDischarge();
		this.internmentSummaryFacadeService.setInternmentEpisodeId(this.internmentEpisode.id);
		this.internmentSummaryFacadeService.anamnesis$.subscribe(a => this.anamnesisDoc = a);
		this.internmentSummaryFacadeService.epicrisis$.subscribe(e => this.epicrisisDoc = e);
		this.internmentSummaryFacadeService.evolutionNote$.subscribe(evolutionNote => this.lastEvolutionNoteDoc = evolutionNote);
		this.internmentSummaryFacadeService.hasMedicalDischarge$.subscribe(h => this.hasMedicalDischarge = h);
	}

	goToPaseCama(): void {
		this.router.navigate([this.routePrefix + ROUTE_INTERNMENT_EPISODE_PREFIX + this.internmentEpisode.id + ROUTE_PATIENT_SUFIX + this.patientId + ROUTE_RELOCATE_PATIENT_BED_PREFIX]);
	}

	goToAdministrativeDischarge(): void {
		this.router.navigate([this.routePrefix + ROUTE_INTERNMENT_EPISODE_PREFIX + this.internmentEpisode.id + ROUTE_PATIENT_SUFIX + this.patientId + ROUTE_ADMINISTRATIVE_DISCHARGE_PREFIX]);
	}

	physicalDischarge() {
		const messageHTML = `<strong>¿Desea confirmar el Alta Física?</strong>`;
		const dialogRef = this.dialog.open(ConfirmDialogComponent, {
			data: {
				content: `Si realiza el Alta Física la cama quedará Libre y el episodio continuará abierto hasta que se completen los documentos necesarios. ${messageHTML}`,
				okButtonLabel: `buttons.CONFIRM`,
				cancelButtonLabel: `buttons.CANCEL`,
				showMatIconError: true,
			},
			width: "25%",
			autoFocus: false,
			disableClose: true,
		});
		dialogRef.afterClosed().subscribe(confirm => {
			if (confirm) {
				this.internmentService.physicalDischargeInternmentEpisode(this.internmentEpisode.id).subscribe(
					success => {
						this.snackBarService.showSuccess('internaciones.discharge.messages.PHYSICAL_DISCHARGE_SUCCESS');
						this.loadPhysicalDischarge();
					},
					error => this.snackBarService.showError('internaciones.discharge.messages.PHYSICAL_DISCHARGE_ERROR')
				);
			}
		})
	}

	loadPhysicalDischarge() {
		this.internmentService.getPatientDischarge(this.internmentEpisode.id).subscribe(patientDischarge => {
			this.hasAdministrativeDischarge = !!patientDischarge.administrativeDischargeDate;
			if (patientDischarge.physicalDischargeDate && !(patientDischarge?.administrativeDischargeDate)) {
				const date = new Date(patientDischarge.physicalDischargeDate);
				let minutes: number | string = date.getMinutes();
				if (minutes < 10) {
					minutes = minutes.toString();
					minutes = `0${minutes}`;
				}
				this.physicalDischargeDate = `${date.getDate()}/${date.getMonth() + 1}/${date.getFullYear()} - ${date.getHours()}:${minutes}hs`;
			}
		})
	}
}

export interface InternmentEpisodeSummary {
	id: number;
	roomNumber: string;
	bedNumber: string;
	sectorDescription: string;
	doctor: {
		firstName: string;
		lastName: string;
		licenses: string[];
	};
	totalInternmentDays: number;
	admissionDatetime: string;
	responsibleContact?: {
		fullName: string;
		relationship: string;
		phoneNumber: string;
	};
	probableDischargeDate: string;
}

export interface InternmentDocuments {
	hasAnamnesis?: boolean;
	hasEpicrisis?: boolean;
}
