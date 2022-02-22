import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Observable } from 'rxjs';
import { map, tap } from 'rxjs/operators';
import { MatDialog } from '@angular/material/dialog';

import { FeatureFlagService } from '@core/services/feature-flag.service';
import { PermissionsService } from '@core/services/permissions.service';

import { InternmentEpisodeSummary } from '@presentation/components/internment-episode-summary/internment-episode-summary.component';
import { MapperService } from '@presentation/services/mapper.service';
import { PatientBasicData } from '@presentation/components/patient-card/patient-card.component';

import { PatientService } from '@api-rest/services/patient.service';
import {
	BasicPatientDto,
	InternmentSummaryDto,
	AnamnesisSummaryDto,
	EpicrisisSummaryDto,
	EvaluationNoteSummaryDto,
	PatientDischargeDto,
	PersonPhotoDto
} from '@api-rest/api-model';
import { AppFeature } from '@api-rest/api-model';
import { InternacionService } from '@api-rest/services/internacion.service';
import { InternmentEpisodeService } from '@api-rest/services/internment-episode.service';

import { INTERNACION, ANTECEDENTES_FAMILIARES, ANTECEDENTES_PERSONALES, MEDICACION } from '../../../../../../constants/summaries';
import { ROLES_FOR_EDIT_DIAGNOSIS } from '../../../internacion/constants/permissions';
import { ProbableDischargeDialogComponent } from '../../../../../../dialogs/probable-discharge-dialog/probable-discharge-dialog.component';
import { momentParseDateTime } from '@core/utils/moment.utils';
import { Moment } from 'moment';
import { ContextService } from '@core/services/context.service';
import { InternmentFields, InternmentSummaryFacadeService } from "@historia-clinica/modules/ambulatoria/modules/internacion/services/internment-summary-facade.service";
import { DockPopupRef } from '@presentation/services/dock-popup-ref';
import { EvolutionNoteDockPopupComponent } from '../../dialogs/evolution-note-dock-popup/evolution-note-dock-popup.component';
import { DockPopupService } from '@presentation/services/dock-popup.service';
import { AnamnesisDockPopupComponent } from "@historia-clinica/modules/ambulatoria/modules/internacion/dialogs/anamnesis-dock-popup/anamnesis-dock-popup.component";
import {
	EpicrisisDockPopupComponent
} from "@historia-clinica/modules/ambulatoria/modules/internacion/dialogs/epicrisis-dock-popup/epicrisis-dock-popup.component";

const ROUTE_EDIT_PATIENT = 'pacientes/edit';

@Component({
	selector: 'app-internacion-paciente',
	templateUrl: './internacion-paciente.component.html',
	styleUrls: ['./internacion-paciente.component.scss']
})
export class InternacionPacienteComponent implements OnInit {

	public patient$: Observable<PatientBasicData>;

	public personPhoto: PersonPhotoDto;
	public internacionSummary = INTERNACION;
	public anamnesisDoc: AnamnesisSummaryDto;
	public epicrisisDoc: EpicrisisSummaryDto;
	public lastEvolutionNoteDoc: EvaluationNoteSummaryDto;
	public lastProbableDischargeDate: Moment;
	public internmentEpisodeSummary$: Observable<InternmentEpisodeSummary>;
	public showDischarge: boolean;
	public editDiagnosisSummary$: boolean;
	public hasMedicalDischarge: boolean;
	public canLoadProbableDischargeDate: boolean;
	public showPatientCard = false;
	public readonly familyHistoriesHeader = ANTECEDENTES_FAMILIARES;
	public readonly personalHistoriesHeader = ANTECEDENTES_PERSONALES;
	public readonly medicationsHeader = MEDICACION;
	dialogRef: DockPopupRef;
	private routePrefix;
	private patientId: number;
	@Input() internmentEpisodeId: number;
	@Output() anamnesisDocEmmiter = new EventEmitter<AnamnesisSummaryDto>();
	@Output() epicrisisDocEmmiter = new EventEmitter<EpicrisisSummaryDto>();
	@Output() lastEvolutionNoteDocEmmiter = new EventEmitter<EvaluationNoteSummaryDto>();
	@Output() hasMedicalDischargeEmmiter = new EventEmitter<boolean>();

	constructor(
		private patientService: PatientService,
		private internmentService: InternacionService,
		private mapperService: MapperService,
		private route: ActivatedRoute,
		private router: Router,
		private featureFlagService: FeatureFlagService,
		private readonly permissionService: PermissionsService,
		private internmentEpisodeService: InternmentEpisodeService,
		public dialog: MatDialog,
		private contextService: ContextService,
		public readonly internmentSummaryFacadeService: InternmentSummaryFacadeService,
		private readonly dockPopupService: DockPopupService,
	) {
	}

	ngOnInit(): void {
		this.route.paramMap.subscribe(
			(params) => {
				this.patientId = Number(params.get('idPaciente'));
				if (params.get('idInternacion')) {
					this.internmentEpisodeId = Number(params.get('idInternacion'));
					this.showPatientCard = true;
				}
				this.routePrefix = 'institucion/' + this.contextService.institutionId + '/internaciones/internacion/' + this.internmentEpisodeId + '/paciente/' + this.patientId;

				this.patient$ = this.patientService.getPatientBasicData<BasicPatientDto>(this.patientId).pipe(
					map(patient => this.mapperService.toPatientBasicData(patient))
				);

				this.patientService.getPatientPhoto(this.patientId)
					.subscribe((personPhotoDto: PersonPhotoDto) => { this.personPhoto = personPhotoDto; });

				this.internmentEpisodeSummary$ = this.internmentService.getInternmentEpisodeSummary(this.internmentEpisodeId).pipe(
					tap((internmentEpisode: InternmentSummaryDto) => {
						this.anamnesisDoc = internmentEpisode.documents?.anamnesis;
						this.epicrisisDoc = internmentEpisode.documents?.epicrisis;
						this.lastEvolutionNoteDoc = internmentEpisode.documents?.lastEvaluationNote;
						this.anamnesisDocEmmiter.emit(this.anamnesisDoc);
						this.epicrisisDocEmmiter.emit(this.epicrisisDoc);
						this.lastEvolutionNoteDocEmmiter.emit(this.lastEvolutionNoteDoc);
						this.lastProbableDischargeDate = internmentEpisode.probableDischargeDate ? momentParseDateTime(internmentEpisode.probableDischargeDate) : undefined;
						// La alta administrativa está disponible cuando existe el alta medica
						// o el flag de alta sin epicrisis está activa
						this.featureFlagService.isActive(AppFeature.HABILITAR_ALTA_SIN_EPICRISIS).subscribe(isOn => {
							this.showDischarge = isOn || (this.hasMedicalDischarge === true);
						});

					}),
					map((internmentEpisode: InternmentSummaryDto) => this.mapperService.toInternmentEpisodeSummary(internmentEpisode))
				);

				this.internmentEpisodeService.getPatientDischarge(this.internmentEpisodeId)
					.subscribe((patientDischarge: PatientDischargeDto) => {
						this.hasMedicalDischarge = patientDischarge.dischargeTypeId !== 0;
						this.hasMedicalDischargeEmmiter.emit(this.hasMedicalDischarge);
					});

				this.featureFlagService.isActive(AppFeature.HABILITAR_CARGA_FECHA_PROBABLE_ALTA).subscribe(isOn => {
					this.canLoadProbableDischargeDate = isOn;
				});

			}
		);
		this.permissionService.hasContextAssignments$(ROLES_FOR_EDIT_DIAGNOSIS).subscribe(
			hasRole => this.editDiagnosisSummary$ = hasRole
		);

		this.internmentSummaryFacadeService.setInternmentEpisodeId(this.internmentEpisodeId);
	}


	goToAdministrativeDischarge(): void {
		this.router.navigate([`${this.routePrefix}/alta`]);
	}

	openDialog() {
		const dialogRef = this.dialog.open(ProbableDischargeDialogComponent, {
			disableClose: true,
			width: '35%',
			data: {
				internmentEpisodeId: this.internmentEpisodeId,
				lastProbableDischargeDate: this.lastProbableDischargeDate
			}
		});

		dialogRef.afterClosed().subscribe(submitted => {
			if (submitted) {
				this.internmentEpisodeSummary$ = this.internmentService.getInternmentEpisodeSummary(this.internmentEpisodeId).pipe(
					map((internmentEpisode: InternmentSummaryDto) => this.mapperService.toInternmentEpisodeSummary(internmentEpisode))
				);
			}
		}
		);
	}

	goToEditPatient(): void {
		const person = {
			id: this.patientId,
		};
		const url = 'institucion/' + this.contextService.institutionId + '/' + ROUTE_EDIT_PATIENT
		this.router.navigate([url], {
			queryParams: person
		});
	}

	openAnamnesis() {
		if (!this.dialogRef) {
			this.dialogRef = this.dockPopupService.open(AnamnesisDockPopupComponent, {
				patientInfo: {
					patientId: this.patientId,
					internmentEpisodeId: this.internmentEpisodeId,
					anamnesisId: this.anamnesisDoc?.id
				},
				autoFocus: false,
				disableClose: true,
			});
			this.dialogRef.afterClosed().subscribe((fieldsToUpdate: InternmentFields) => {
				delete this.dialogRef;
				if (fieldsToUpdate) {
					this.internmentSummaryFacadeService.setFieldsToUpdate(fieldsToUpdate);
				}
			});
		} else {
			if (this.dialogRef.isMinimized()) {
				this.dialogRef.maximize();
			}
		}
	}

	openEvolutionNote() {
		if (!this.dialogRef) {
			this.dialogRef = this.dockPopupService.open(EvolutionNoteDockPopupComponent, {
				internmentEpisodeId: this.internmentEpisodeId,
				autoFocus: false,
				disableClose: true,
			});
			this.dialogRef.afterClosed().subscribe((fieldsToUpdate: InternmentFields) => {
				delete this.dialogRef;
				if (fieldsToUpdate) {
					this.internmentSummaryFacadeService.setFieldsToUpdate(fieldsToUpdate);
				}
			});
		} else {
			if (this.dialogRef.isMinimized()) {
				this.dialogRef.maximize();
			}
		}
	}

	openEpicrisis() {
		if (!this.dialogRef) {
			this.dialogRef = this.dockPopupService.open(EpicrisisDockPopupComponent, {
				patientInfo: {
					patientId: this.patientId,
					internmentEpisodeId: this.internmentEpisodeId,
				},
				autoFocus: false,
				disableClose: true,
			});
			this.dialogRef.afterClosed().subscribe((fieldsToUpdate: InternmentFields) => {
				delete this.dialogRef;
				if (fieldsToUpdate) {
					this.internmentSummaryFacadeService.setFieldsToUpdate(fieldsToUpdate);
				}
			});
		} else {
			if (this.dialogRef.isMinimized()) {
				this.dialogRef.maximize();
			}
		}
	}

}
