import { Component, Input, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { AppFeature, VMedicalDischargeDto, ERole, ApiErrorMessageDto } from '@api-rest/api-model.d';
import { ResponseEmergencyCareDto, TriageListDto } from '@api-rest/api-model.d';
import { dateTimeDtoToDate } from '@api-rest/mapper/date-dto.mapper';
import { DischargeTypes } from '@api-rest/masterdata';
import { EmergencyCareEpisodeMedicalDischargeService } from '@api-rest/services/emergency-care-episode-medical-discharge.service';
import { EmergencyCareEpisodeStateService } from '@api-rest/services/emergency-care-episode-state.service';
import { EmergencyCareEpisodeService } from '@api-rest/services/emergency-care-episode.service';
import { TriageService } from '@api-rest/services/triage.service';
import { ContextService } from '@core/services/context.service';
import { FeatureFlagService } from '@core/services/feature-flag.service';
import { PatientNameService } from '@core/services/patient-name.service';
import { PermissionsService } from '@core/services/permissions.service';
import { anyMatch } from '@core/utils/array.utils';
import { GUARDIA, PatientType } from '@historia-clinica/constants/summaries';
import { EmergencyCareStateChangedService } from '@historia-clinica/modules/ambulatoria/services/emergency-care-state-changed.service';
import { TriageCategory } from '@historia-clinica/modules/guardia/components/triage-chip/triage-chip.component';
import { TriageDetails } from '@historia-clinica/modules/guardia/components/triage-details/triage-details.component';
import { EmergencyCareTypes, EstadosEpisodio } from '@historia-clinica/modules/guardia/constants/masterdata';
import { SelectConsultorioComponent } from '@historia-clinica/modules/guardia/dialogs/select-consultorio/select-consultorio.component';
import { EpisodeStateService } from '@historia-clinica/modules/guardia/services/episode-state.service';
import { GuardiaMapperService } from '@historia-clinica/modules/guardia/services/guardia-mapper.service';
import { NewEmergencyCareEvolutionNoteService } from '@historia-clinica/modules/guardia/services/new-emergency-care-evolution-note.service';
import { TriageDefinitionsService } from '@historia-clinica/modules/guardia/services/triage-definitions.service';
import { EmergencyCareEpisodeAttendService } from '@historia-clinica/services/emergency-care-episode-attend.service';
import { NewTriageService } from '@historia-clinica/services/new-triage.service';
import { TranslateService } from '@ngx-translate/core';
import { REGISTER_EDITOR_CASES, RegisterEditor } from '@presentation/components/register-editor-info/register-editor-info.component';
import { SummaryHeader } from '@presentation/components/summary-card/summary-card.component';
import { ConfirmDialogComponent } from '@presentation/dialogs/confirm-dialog/confirm-dialog.component';
import { SnackBarService } from '@presentation/services/snack-bar.service';

const TRANSLATE_KEY_PREFIX = 'guardia.home.episodes.episode.actions';

@Component({
	selector: 'app-resumen-de-guardia',
	templateUrl: './resumen-de-guardia.component.html',
	styleUrls: ['./resumen-de-guardia.component.scss'],
	providers: [TriageDefinitionsService]
})
export class ResumenDeGuardiaComponent implements OnInit {

	//En lugar de pasar el id puedo pasar el episodio entero porque ya lo voy a estar calculando desde antes en ambulatoria
	@Input() episodeId: number;
	@Input() showNewTriage: boolean = false;
	@Input() isEmergencyCareTemporalPatient: boolean = false;

	guardiaSummary: SummaryHeader = GUARDIA;
	readonly STATES = EstadosEpisodio;
	episodeState: EstadosEpisodio;
	withoutMedicalDischarge: boolean;

	hasMedicalDischarge = false;
	medicalDischargeData: VMedicalDischargeDto;
	problemDescriptionText: string[];
	dischargeTypeDescriptionText: string;

	registerEditor: RegisterEditor;
	registerEditorCasesDateHour = REGISTER_EDITOR_CASES.DATE_HOUR;

	responseEmergencyCare: ResponseEmergencyCareDto;
	emergencyCareType: EmergencyCareTypes;
	doctorsOfficeDescription: string;
	shockroomDescription: string;
	bedDescription: string;

	triagesHistory: TriageReduced[];
	fullNamesHistoryTriage: string[];
	lastTriage: TriageDetails;

	private hasEmergencyCareRelatedRole: boolean;
	private hasRoleAdministrative: boolean;
	private hasRoleAbleToSeeTriage: boolean;

	availableActions: ActionInfo[] = [];
	TEMPORARY_EMERGENCY_CARE = PatientType.EMERGENCY_CARE_TEMPORARY;

	isAdministrativeAndHasTriageFFInFalse: boolean;

	constructor(
		private readonly emergencyCareEpisodeService: EmergencyCareEpisodeService,
		private readonly triageService: TriageService,
		private readonly guardiaMapperService: GuardiaMapperService,
		private readonly patientNameService: PatientNameService,
		private readonly dialog: MatDialog,
		private readonly episodeStateService: EpisodeStateService,
		private snackBarService: SnackBarService,
		private readonly contextService: ContextService,
		private readonly router: Router,
		private readonly emergencyCareEpisodeStateService: EmergencyCareEpisodeStateService,
		private readonly permissionsService: PermissionsService,
		private readonly newTriageService: NewTriageService,
		private readonly emergencyCareEpisodeAttend: EmergencyCareEpisodeAttendService,
		private readonly triageDefinitionsService: TriageDefinitionsService,
		private readonly emergencyCareStateChangedService: EmergencyCareStateChangedService,
		private readonly newEmergencyCareEvolutionNoteService: NewEmergencyCareEvolutionNoteService,
		private readonly emergencyCareEpisodeMedicalDischargeService: EmergencyCareEpisodeMedicalDischargeService,
		private readonly translate: TranslateService,
		private readonly featureFlagService: FeatureFlagService
	) {}


	ngOnInit(): void {
		this.setRolesAndEpisodeState();

		this.checkAdministrativeFF();

		this.newEmergencyCareEvolutionNoteService.new$.subscribe(() => this.calculateAvailableActions());

		this.loadEpisode()

		this.newTriageService.newTriage$.subscribe(
			_ => this.loadTriages()
		)

		this.emergencyCareEpisodeAttend.loadEpisode$.subscribe((response: boolean) => {
			if (!response) return;

			this.loadEpisode();
			this.setEpisodeState();
		});

		this.emergencyCareEpisodeMedicalDischargeService.hasMedicalDischarge(this.episodeId).subscribe((hasMedicalDischarge) => {
			if (hasMedicalDischarge) {
				this.hasMedicalDischarge = true;
				this.loadPatientDischarge();
			}
		});
	}

	private setRolesAndEpisodeState(){
		this.permissionsService.contextAssignments$().subscribe((userRoles: ERole[]) => {
			this.hasEmergencyCareRelatedRole = anyMatch<ERole>(userRoles, [ERole.ESPECIALISTA_MEDICO, ERole.PROFESIONAL_DE_SALUD, ERole.ESPECIALISTA_EN_ODONTOLOGIA, ERole.ENFERMERO]);
			this.hasRoleAdministrative = anyMatch<ERole>(userRoles, [ERole.ADMINISTRATIVO, ERole.ADMINISTRATIVO_RED_DE_IMAGENES]);
			const proffesionalRoles: ERole[] = [ERole.ENFERMERO, ERole.PROFESIONAL_DE_SALUD, ERole.ESPECIALISTA_MEDICO, ERole.ESPECIALISTA_EN_ODONTOLOGIA];
       		this.hasRoleAbleToSeeTriage = userRoles.some(role => proffesionalRoles.includes(role));
			this.setEpisodeState();
		});
	}

	private checkAdministrativeFF(){
		this.featureFlagService.isActive(AppFeature.HABILITAR_TRIAGE_PARA_ADMINISTRATIVO).subscribe(isEnabled =>
			this.hasRoleAbleToSeeTriage
			? this.isAdministrativeAndHasTriageFFInFalse = false
			: this.isAdministrativeAndHasTriageFFInFalse = (!isEnabled && this.hasRoleAdministrative)
		)
	}

	newTriage() {
		this.triageDefinitionsService.getTriagePath(this.emergencyCareType)
			.subscribe(({ component }) => {
				const dialogRef = this.dialog.open(component, { data: this.episodeId });
				dialogRef.afterClosed().subscribe(idReturned => {
					if (idReturned) {
						this.loadTriages();
					}
				});
			});
	}

	toEnEspera(): void {
		const ref = this.dialog.open(ConfirmDialogComponent, {
			data: {
				title: `${TRANSLATE_KEY_PREFIX}.en_espera.TITLE`,
				content: `${TRANSLATE_KEY_PREFIX}.en_espera.CONFIRM`,
				okButtonLabel: 'Aceptar'
			}
		})

		ref.afterClosed().subscribe(
			closed => {
				if (closed) {
					this.episodeStateService.pasarAEspera(this.episodeId).subscribe({
						next: (changed) => {
							if (changed) {
								this.snackBarService.showSuccess(`${TRANSLATE_KEY_PREFIX}.en_espera.SUCCESS`);
								this.episodeState = EstadosEpisodio.EN_ESPERA;
								this.doctorsOfficeDescription = null;
								this.shockroomDescription = null;
								this.bedDescription = null;
								this.emergencyCareStateChangedService.emergencyCareStateChanged(EstadosEpisodio.EN_ESPERA)
								this.calculateAvailableActions();
							}
							else {
								this.snackBarService.showError(`${TRANSLATE_KEY_PREFIX}.en_espera.ERROR`);
							}
						},
						error: (err: ApiErrorMessageDto) => {
							this.snackBarService.showError(err.text);
						}
					})
					
				}
			}
		)
	}

	goToMedicalDischarge() {
		if (!this.responseEmergencyCare?.patient) {
			this.snackBarService.showError('ambulatoria.paciente.guardia.PATIENT_REQUIRED');
		} else {
			this.router.navigate([`/institucion/${this.contextService.institutionId}/guardia/episodio/${this.episodeId}/alta-medica`]);
		}
	}

	goToAdministrativeDischarge() {
		this.router.navigate([`/institucion/${this.contextService.institutionId}/guardia/episodio/${this.episodeId}/alta-administrativa`]);
	}

	goToEditEpisode() {
		this.router.navigate([`/institucion/${this.contextService.institutionId}/guardia/episodio/${this.episodeId}/patientClinicalHistory/edit`]);
	}

	atender(): void {

		const dialogRef = this.dialog.open(SelectConsultorioComponent, {
			width: '25%',
			data: { title: 'guardia.select_consultorio.ATENDER' }
		});

		dialogRef.afterClosed().subscribe(consultorio => {
			if (consultorio) {
				this.doctorsOfficeDescription = consultorio?.description;
				this.episodeStateService.atender(this.episodeId, consultorio.id).subscribe(changed => {
					if (changed) {
						this.snackBarService.showSuccess(`${TRANSLATE_KEY_PREFIX}.atender.SUCCESS`);
						this.episodeState = EstadosEpisodio.EN_ATENCION;
						this.calculateAvailableActions();
					}
					else {
						this.snackBarService.showError(`${TRANSLATE_KEY_PREFIX}.atender.ERROR`);
					}
				}, _ => this.snackBarService.showError(`${TRANSLATE_KEY_PREFIX}.atender.ERROR`)
				);
			}
		});
	}

	attend() {
		this.emergencyCareEpisodeAttend.attend(this.episodeId, false);
	}

	private setEpisodeState() {
		this.emergencyCareEpisodeStateService.getState(this.episodeId).subscribe(
			state => {
				this.episodeState = state.id;
				this.calculateAvailableActions();
				this.withoutMedicalDischarge = (this.episodeState !== this.STATES.CON_ALTA_MEDICA);
			}
		);
	}

	private loadEpisode() {
		this.emergencyCareEpisodeService.getAdministrative(this.episodeId)
			.subscribe((responseEmergencyCare: ResponseEmergencyCareDto) => {
				this.responseEmergencyCare = responseEmergencyCare;
				this.emergencyCareType = responseEmergencyCare.emergencyCareType?.id;
				this.doctorsOfficeDescription = responseEmergencyCare.doctorsOffice?.description;
				this.shockroomDescription = responseEmergencyCare.shockroom?.description;
				this.bedDescription = responseEmergencyCare.bed?.bedNumber;
			});

		this.loadTriages();
	}

	private calculateAvailableActions() {


		this.emergencyCareEpisodeService.hasEvolutionNote(this.episodeId).subscribe(
			hasEvolutionNote => {
				this.availableActions = [];
				// Following code within this function must be in this order

				if (this.hasEmergencyCareRelatedRole && this.episodeState === this.STATES.EN_ATENCION && hasEvolutionNote && !this.isEmergencyCareTemporalPatient) {
					let action: ActionInfo = {
						label: 'ambulatoria.paciente.guardia.PATIENT_DISCHARGE.TITLE',
						id: 'medical_discharge',
						callback: this.goToMedicalDischarge.bind(this)
					}
					this.availableActions.push(action);
				}

				if (this.hasRoleAdministrative && (this.episodeState === this.STATES.CON_ALTA_MEDICA || (this.episodeState === this.STATES.EN_ESPERA && !hasEvolutionNote)) && this.responseEmergencyCare.patient.typeId !== 8) {
					let action: ActionInfo = {
						label: 'ambulatoria.paciente.guardia.ADMINISTRATIVE_DISCHARGE_BUTTON',
						id: 'administrative_discharge',
						callback: this.goToAdministrativeDischarge.bind(this)
					}
					this.availableActions.push(action);
				}

				if (this.episodeState === this.STATES.EN_ATENCION || this.episodeState === this.STATES.EN_ESPERA) {
					let action: ActionInfo = {
						label: 'ambulatoria.paciente.guardia.EDIT_BUTTON',
						id: 'edit_episode',
						callback: this.goToEditEpisode.bind(this).bind(this)
					}
					this.availableActions.push(action);
				}

				if (this.hasEmergencyCareRelatedRole && this.episodeState === this.STATES.EN_ESPERA) {
					let action: ActionInfo = {
						label: 'guardia.home.episodes.episode.actions.atender.TITLE',
						id: 'attend',
						callback: this.attend.bind(this)
					}
					this.availableActions.push(action);
				}

				if (this.hasEmergencyCareRelatedRole && this.episodeState === this.STATES.EN_ATENCION) {
					let action: ActionInfo = {
						label: 'Pasar a espera',
						id: 'a-en-espera',
						callback: this.toEnEspera.bind(this)
					}
					this.availableActions.push(action);
				}
			}
		)
	}

	private loadFullNames() {
		this.fullNamesHistoryTriage = [];
		this.triagesHistory.forEach(
			(triage: TriageReduced) => {
				this.fullNamesHistoryTriage.push(this.getFullName(triage));
			}
		);
	}

	private getFullName(triage: TriageReduced): string {
		return `${this.patientNameService.getPatientName(triage.createdBy.firstName, triage.createdBy.nameSelfDetermination)}, ${triage.createdBy.lastName}`;
	}

	private loadTriages() {
		this.triageService.getAll(this.episodeId).subscribe((triages: TriageListDto[]) => {
			if (hasHistory(triages)) {
				this.lastTriage = this.guardiaMapperService.triageListDtoToTriage(triages[0]);
				this.triagesHistory = triages.map(this.guardiaMapperService.triageListDtoToTriageReduced);
				this.triagesHistory.shift();
				this.loadFullNames();
			}
		});

		function hasHistory(triages: TriageListDto[]) {
			return triages?.length > 0;
		}
	}

	private loadPatientDischarge(){
		this.emergencyCareEpisodeMedicalDischargeService.getMedicalDischarge(this.episodeId)
			.subscribe((data) => {
				this.medicalDischargeData = data;
				this.problemDescriptionText = data.snomedPtProblems;
				this.dischargeTypeDescriptionText = this.getDischargeTypeDescriptionText(data);
				this.registerEditor = {
					createdBy: `${data.medicalDischargeProfessionalName} ${data.medicalDischargeProfessionalLastName}`,
					date: dateTimeDtoToDate(data.medicalDischargeOn),
				};
			}
		);
	}

	private getDischargeTypeDescriptionText(data: any): string {
		const { dischargeType, otherDischargeDescription, autopsy } = data;
		const { description, id } = dischargeType;

		if (id === DischargeTypes.OTRO) {
			const otherDischargeTranslation = this.translate.instant('ambulatoria.paciente.guardia.PATIENT_DISCHARGE.DISCHARGE_TYPE_OTHER');
			return `${otherDischargeDescription} ${otherDischargeTranslation}`;
		}

		if (id === DischargeTypes.DEFUNCION) {
			const deceasedTranslation = this.translate.instant('ambulatoria.paciente.guardia.PATIENT_DISCHARGE.DECEASED');
			const autopsyTranslation = autopsy
			? this.translate.instant('ambulatoria.paciente.guardia.PATIENT_DISCHARGE.AUTOPSY_REQUESTED')
			: this.translate.instant('ambulatoria.paciente.guardia.PATIENT_DISCHARGE.AUTOPSY_NOT_REQUESTED');

			return `${deceasedTranslation}${autopsyTranslation}`;
		}

		return description;
	}

}

export interface TriageReduced {
	creationDate: Date,
	category: TriageCategory,
	createdBy: {
		firstName: string,
		lastName: string,
		nameSelfDetermination: string
	},
	doctorsOfficeDescription: string
}

interface ActionInfo {
	label: string,
	id: string,
	callback: Function
}
