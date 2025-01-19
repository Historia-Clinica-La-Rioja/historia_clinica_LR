import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { MatDialog } from "@angular/material/dialog";
import { Router } from "@angular/router";
import { AnamnesisSummaryDto, EpicrisisSummaryDto, EpisodeDocumentResponseDto, EvaluationNoteSummaryDto } from '@api-rest/api-model';
import { ERole } from '@api-rest/api-model';
import { InternmentEpisodeDocumentService } from '@api-rest/services/internment-episode-document.service';
import { InternmentEpisodeService } from "@api-rest/services/internment-episode.service";
import { ContextService } from "@core/services/context.service";
import { PermissionsService } from "@core/services/permissions.service";
import { anyMatch } from "@core/utils/array.utils";
import { ConfirmDialogComponent } from "@presentation/dialogs/confirm-dialog/confirm-dialog.component";
import { SnackBarService } from "@presentation/services/snack-bar.service";
import { ROLES_FOR_ACCESS_EPISODE_DOCUMENTS } from '../../constants/permissions';
import { InternmentSummaryFacadeService } from '../../services/internment-summary-facade.service';
import { DateFormatPipe } from '@presentation/pipes/date-format.pipe';

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
	currentUserHasPermissionToAccessDocuments = false;
	physicalDischargeDate: string;
	probableDischargeDate: string;
	_internmentEpisode: InternmentEpisodeSummary
	@Input() set internmentEpisode(e: InternmentEpisodeSummary) {
		this._internmentEpisode = e;
		this.probableDischargeDate = e.probableDischargeDate ? this.dateFormatPipe.transform(e.probableDischargeDate,'date') : 'Sin fecha definida'
	}
	@Input() canLoadProbableDischargeDate: boolean;
	@Input() patientId: number;
	@Input() showDischarge: boolean;
	@Input() showChangeDate: boolean;
	@Input() patientDocuments: InternmentDocuments;
	@Output() openInNew = new EventEmitter();

	private readonly routePrefix;
	anamnesisDoc: AnamnesisSummaryDto;
	epicrisisDoc: EpicrisisSummaryDto;
	lastEvolutionNoteDoc: EvaluationNoteSummaryDto;
	hasMedicalDischarge: boolean;
	hasAdministrativeDischarge = false;

	documents: EpisodeDocumentResponseDto[];
	ROLES_FOR_ACCESS_EPISODE_DOCUMENTS: ERole[] = ROLES_FOR_ACCESS_EPISODE_DOCUMENTS;

	constructor(
		private router: Router,
		private contextService: ContextService,
		private readonly snackBarService: SnackBarService,
		private readonly dialog: MatDialog,
		private readonly permissionsService: PermissionsService,
		private readonly internmentService: InternmentEpisodeService,
		private internmentEpisodeDocumentService: InternmentEpisodeDocumentService,
		readonly internmentSummaryFacadeService: InternmentSummaryFacadeService,
		private readonly dateFormatPipe: DateFormatPipe
	) {
		this.routePrefix = 'institucion/' + this.contextService.institutionId + '/';
	}
	ngOnInit(): void {
		this.permissionsService.contextAssignments$().subscribe((userRoles: ERole[]) => {
			this.currentUserHasPermissionToAccessDocuments = anyMatch<ERole>(userRoles, [ERole.ADMINISTRATIVO, ERole.PROFESIONAL_DE_SALUD, ERole.ESPECIALISTA_MEDICO, ERole.ENFERMERO]);
			this.currentUserIsAllowToDoAPhysicalDischarge = (anyMatch<ERole>(userRoles, [ERole.ADMINISTRADOR_DE_CAMAS]) &&
				(anyMatch<ERole>(userRoles, [ERole.ADMINISTRATIVO, ERole.ENFERMERO, ERole.ADMINISTRATIVO_RED_DE_IMAGENES])));
		});
		this.loadPhysicalDischarge();
		this.internmentSummaryFacadeService.setInternmentEpisodeId(this._internmentEpisode.id);
		this.internmentSummaryFacadeService.anamnesis$.subscribe(a => this.anamnesisDoc = a);
		this.internmentSummaryFacadeService.epicrisis$.subscribe(e => this.epicrisisDoc = e);
		this.internmentSummaryFacadeService.evolutionNote$.subscribe(evolutionNote => this.lastEvolutionNoteDoc = evolutionNote);
		this.internmentSummaryFacadeService.hasMedicalDischarge$.subscribe(h => this.hasMedicalDischarge = h);

		if (this.currentUserHasPermissionToAccessDocuments)
			this.setDocuments();
	}
	setDocuments() {
		this.internmentEpisodeDocumentService.getInternmentEpisodeDocuments(this._internmentEpisode.id)
			.subscribe((response: EpisodeDocumentResponseDto[]) => {
				if (response)
					this.documents = response
			});
	}

	goToPaseCama(): void {
		this.router.navigate([this.routePrefix + ROUTE_INTERNMENT_EPISODE_PREFIX + this._internmentEpisode.id + ROUTE_PATIENT_SUFIX + this.patientId + ROUTE_RELOCATE_PATIENT_BED_PREFIX]);
	}

	goToAdministrativeDischarge(): void {
		this.router.navigate([this.routePrefix + ROUTE_INTERNMENT_EPISODE_PREFIX + this._internmentEpisode.id + ROUTE_PATIENT_SUFIX + this.patientId + ROUTE_ADMINISTRATIVE_DISCHARGE_PREFIX]);
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
				this.internmentService.physicalDischargeInternmentEpisode(this._internmentEpisode.id).subscribe(
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
		this.internmentService.getPatientDischarge(this._internmentEpisode.id).subscribe(patientDischarge => {
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
	admissionDatetime: Date;
	responsibleContact?: {
		fullName: string;
		relationship: string;
		phoneNumber: string;
	};
	probableDischargeDate: Date;
}

export interface InternmentDocuments {
	hasAnamnesis?: boolean;
	hasEpicrisis?: boolean;
}
