import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { AppFeature, ERole, ApiErrorMessageDto } from '@api-rest/api-model.d';
import { ResponseEmergencyCareDto, TriageListDto } from '@api-rest/api-model.d';
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
import { EpisodeStateService } from '@historia-clinica/modules/guardia/services/episode-state.service';
import { GuardiaMapperService } from '@historia-clinica/modules/guardia/services/guardia-mapper.service';
import { NewEmergencyCareEvolutionNoteService } from '@historia-clinica/modules/guardia/services/new-emergency-care-evolution-note.service';
import { TriageDefinitionsService } from '@historia-clinica/modules/guardia/services/triage-definitions.service';
import { EmergencyCareEpisodeCallOrAttendService } from '@historia-clinica/services/emergency-care-episode-call-or-attend.service';
import { NewTriageService } from '@historia-clinica/services/new-triage.service';
import { EmergencyCareStatusLabels } from '@hsi-components/emergency-care-status-labels/emergency-care-status-labels.component';
import { TranslateService } from '@ngx-translate/core';
import { ButtonType } from '@presentation/components/button/button.component';
import { SummaryHeader } from '@presentation/components/summary-card/summary-card.component';
import { ConfirmDialogData, ConfirmDialogV2Component } from '@presentation/dialogs/confirm-dialog-v2/confirm-dialog-v2.component';
import { DialogConfiguration, DialogService, DialogWidth } from '@presentation/services/dialog.service';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { map, Observable, Subscription, switchMap, take, tap } from 'rxjs';

const TRANSLATE_KEY_PREFIX = 'guardia.home.episodes.episode.actions';
const MIN_ACTIONS = 3;
const BACK_TO_WAITING_STATES = [EstadosEpisodio.EN_ATENCION, EstadosEpisodio.LLAMADO, EstadosEpisodio.AUSENTE];
const STATES_TO_CALL_EPISODE = [EstadosEpisodio.EN_ESPERA, EstadosEpisodio.LLAMADO, EstadosEpisodio.AUSENTE];
const STATES_TO_ATTEND_EPISODE = [EstadosEpisodio.EN_ESPERA, EstadosEpisodio.LLAMADO, EstadosEpisodio.AUSENTE];
const STATES_TO_EDIT_EPISODE = [EstadosEpisodio.EN_ATENCION, EstadosEpisodio.EN_ESPERA, EstadosEpisodio.AUSENTE];

@Component({
	selector: 'app-resumen-de-guardia',
	templateUrl: './resumen-de-guardia.component.html',
	styleUrls: ['./resumen-de-guardia.component.scss'],
	providers: [TriageDefinitionsService]
})
export class ResumenDeGuardiaComponent implements OnInit, OnDestroy {

	//En lugar de pasar el id puedo pasar el episodio entero porque ya lo voy a estar calculando desde antes en ambulatoria
	@Input() episodeId: number;
	@Input() showNewTriage: boolean = false;
	@Input() isEmergencyCareTemporalPatient: boolean = false;

	private subscriptionToAvailableActions: Subscription;

	guardiaSummary: SummaryHeader = GUARDIA;
	readonly STATES = EstadosEpisodio;
	episodeState: EstadosEpisodio;
	withoutMedicalDischarge: boolean;

	hasMedicalDischarge = false;

	responseEmergencyCare: ResponseEmergencyCareDto;
	emergencyCareType: EmergencyCareTypes;
	doctorsOfficeDescription: string;
	shockroomDescription: string;
	bedDescription: string;

	triagesHistory: TriageReduced[];
	fullNamesHistoryTriage: string[];
	lastTriage: TriageDetails;

	private canBeAbsent: boolean;
	private hasEmergencyCareRelatedRole: boolean;
	private hasRoleAdministrative: boolean;
	private hasRoleAbleToSeeTriage: boolean;

	availableActions: ActionInfo[] = [];
	sliceActions: ActionInfo[] = [];
	TEMPORARY_EMERGENCY_CARE = PatientType.EMERGENCY_CARE_TEMPORARY;
	ButtonType = ButtonType;

	isAdministrativeAndHasTriageFFInFalse: boolean;
	statusLabel: EmergencyCareStatusLabels;

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
		private readonly emergencyCareEpisodeAttend: EmergencyCareEpisodeCallOrAttendService,
		private readonly triageDefinitionsService: TriageDefinitionsService,
		private readonly emergencyCareStateChangedService: EmergencyCareStateChangedService,
		private readonly newEmergencyCareEvolutionNoteService: NewEmergencyCareEvolutionNoteService,
		private readonly emergencyCareEpisodeMedicalDischargeService: EmergencyCareEpisodeMedicalDischargeService,
		private readonly translate: TranslateService,
		private readonly featureFlagService: FeatureFlagService,
		private readonly dialogService: DialogService<ConfirmDialogV2Component>,
	) {}


	ngOnInit(): void {
		this.setRolesAndEpisodeState();

		this.checkAdministrativeFF();

		this.newEmergencyCareEvolutionNoteService.new$.subscribe(() => this.calculateAvailableActions());

		this.loadEpisode();

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
			}
		});
	}

	ngOnDestroy() {
		if (this.subscriptionToAvailableActions) {
			this.subscriptionToAvailableActions.unsubscribe();
		}
	}

	private setRolesAndEpisodeState(){
		this.permissionsService.contextAssignments$().subscribe((userRoles: ERole[]) => {
			this.hasEmergencyCareRelatedRole = anyMatch<ERole>(userRoles, [ERole.ESPECIALISTA_MEDICO, ERole.PROFESIONAL_DE_SALUD, ERole.ESPECIALISTA_EN_ODONTOLOGIA, ERole.ENFERMERO]);
			this.hasRoleAdministrative = anyMatch<ERole>(userRoles, [ERole.ADMINISTRATIVO, ERole.ADMINISTRATIVO_RED_DE_IMAGENES]);
			const proffesionalRoles: ERole[] = [ERole.ENFERMERO, ERole.PROFESIONAL_DE_SALUD, ERole.ESPECIALISTA_MEDICO, ERole.ESPECIALISTA_EN_ODONTOLOGIA];
       		this.hasRoleAbleToSeeTriage = userRoles.some(role => proffesionalRoles.includes(role));
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
				const dialogRef = this.dialog.open(component, {
					autoFocus: false,
					disableClose: true,
					data: this.episodeId,
				});
				dialogRef.afterClosed().subscribe(idReturned => {
					if (idReturned) {
						this.loadTriages();
					}
				});
			});
	}

	toEnEspera(): void {
		const dialogData: ConfirmDialogData = {
			title: `${TRANSLATE_KEY_PREFIX}.en_espera.TITLE`,
			hasIcon: false,
			content: `${TRANSLATE_KEY_PREFIX}.en_espera.CONFIRM`,
			okButtonLabel: `${TRANSLATE_KEY_PREFIX}.en_espera.buttons.CONFIRM`,
			cancelButtonLabel: 'buttons.NO_CANCEL',
			buttonClose: true,
		}
		const dialogConfig: DialogConfiguration = { dialogWidth: DialogWidth.SMALL }
		const ref = this.dialogService.open(ConfirmDialogV2Component, dialogConfig, dialogData);

		ref.afterClosed().subscribe(
			closed => {
				if (closed) {
					this.episodeStateService.pasarAEspera(this.episodeId).subscribe({
						next: (changed) => {
							if (changed) {
								this.snackBarService.showSuccess(`${TRANSLATE_KEY_PREFIX}.en_espera.SUCCESS`);
								this.doctorsOfficeDescription = null;
								this.shockroomDescription = null;
								this.bedDescription = null;
								this.emergencyCareStateChangedService.emergencyCareStateChanged(EstadosEpisodio.EN_ESPERA)
								this.setEpisodeState();
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
		this.router.navigate([`/institucion/${this.contextService.institutionId}/guardia/episodio/${this.episodeId}/paciente/edit`]);
	}

	attend() {
		this.emergencyCareEpisodeAttend.callOrAttendPatient(this.episodeId, false, false);
		this.setEpisodeState();
	}

	call(){
		this.emergencyCareEpisodeAttend.callOrAttendPatient(this.episodeId, false, true);
		this.setEpisodeState();
	}

	markAsAbsent(){
		const dialogData: ConfirmDialogData = {
			title: 'guardia.home.episodes.episode.actions.mark_as_absent.TITLE',
			hasIcon: false,
			content: 'guardia.home.episodes.episode.actions.mark_as_absent.CONFIRM',
			okButtonLabel: 'guardia.home.episodes.episode.actions.mark_as_absent.buttons.CONFIRM',
			cancelButtonLabel: 'buttons.NO_CANCEL',
			buttonClose: true
		};
		const dialogConfig: DialogConfiguration = { dialogWidth: DialogWidth.SMALL };
        const dialog = this.dialogService.open(ConfirmDialogV2Component, dialogConfig, dialogData);
        dialog.afterClosed().pipe(
            take(1),
            switchMap(closed => {
                if (closed) {
                    return this.episodeStateService.markAsAbsent(this.episodeId);
                }
            }))
            .subscribe({
				next: (changed) => {
					if (changed) {
						this.snackBarService.showSuccess(this.translate.instant('guardia.home.episodes.episode.actions.mark_as_absent.SUCCESS'));
						this.episodeState = EstadosEpisodio.AUSENTE;
						this.emergencyCareStateChangedService.emergencyCareStateChanged(EstadosEpisodio.AUSENTE);
						this.setEpisodeState();
					}
					else {
						this.snackBarService.showError(this.translate.instant('guardia.home.episodes.episode.actions.mark_as_absent.ERROR'));
					}
				},
				error: () => {}
            }
        );
	}

	private setEpisodeState() {
		this.emergencyCareEpisodeStateService.getState(this.episodeId).subscribe(
			state => {
				this.episodeState = state.id;
				this.withoutMedicalDischarge = (this.episodeState !== this.STATES.CON_ALTA_MEDICA);
				this.buildStatusLabel(state.description);
				this.calculateAvailableActions();
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
				this.canBeAbsent = responseEmergencyCare.canBeAbsent;
			});

		this.loadTriages();
	}

	private calculateAvailableActions() {
		this.subscriptionToAvailableActions = this.loadCanBeAbsentCondition().pipe(
			switchMap(() => this.emergencyCareEpisodeService.hasEvolutionNote(this.episodeId)),
			tap(hasEvolutionNote => {
				this.availableActions = [];
				this.sliceActions = [];
				// Following code within this function must be in this order

				const canDoMedicalDischarge = this.hasEmergencyCareRelatedRole && hasEvolutionNote && !this.isEmergencyCareTemporalPatient && this.episodeState === EstadosEpisodio.EN_ATENCION
				if (canDoMedicalDischarge) {
					const action: ActionInfo = {
						label: 'ambulatoria.paciente.guardia.PATIENT_DISCHARGE.TITLE',
						id: 'medical_discharge',
						callback: this.goToMedicalDischarge.bind(this)
					}
					this.availableActions.push(action);
				}

				if (this.showAdministrativeDischargeButton(hasEvolutionNote)) {
					const action: ActionInfo = {
						label: 'ambulatoria.paciente.guardia.ADMINISTRATIVE_DISCHARGE_BUTTON',
						id: 'administrative_discharge',
						callback: this.goToAdministrativeDischarge.bind(this)
					}
					this.availableActions.push(action);
				}

				if (STATES_TO_EDIT_EPISODE.includes(this.episodeState)) {
					const action: ActionInfo = {
						label: 'ambulatoria.paciente.guardia.EDIT_BUTTON',
						id: 'edit_episode',
						callback: this.goToEditEpisode.bind(this).bind(this)
					}
					this.availableActions.push(action);
				}

				if (this.hasEmergencyCareRelatedRole && STATES_TO_CALL_EPISODE.includes(this.episodeState)) {
					let action: ActionInfo = {
					   label: 'guardia.home.episodes.episode.actions.call.TITLE',
					   id: 'call',
					   callback: this.call.bind(this)
					}
					this.availableActions.push(action);
				}

				if (this.hasEmergencyCareRelatedRole && STATES_TO_ATTEND_EPISODE.includes(this.episodeState)) {
					let action: ActionInfo = {
						label: 'guardia.home.episodes.episode.actions.atender.TITLE',
						id: 'attend',
						callback: this.attend.bind(this)
					}
					this.availableActions.push(action);
				}

				if (this.hasEmergencyCareRelatedRole && BACK_TO_WAITING_STATES.includes(this.episodeState)) {
					const action: ActionInfo = {
						label: 'Pasar a espera',
						id: 'a-en-espera',
						callback: this.toEnEspera.bind(this)
					}
					this.availableActions.push(action);
				}

				if (this.canBeAbsent) {
					const action: ActionInfo = {
						label: 'guardia.home.episodes.episode.actions.mark_as_absent.TITLE',
						id: 'markAsAbsent',
						callback: this.markAsAbsent.bind(this)
					}
					this.availableActions.push(action);
				}

				if (this.availableActions.length > MIN_ACTIONS) {
					const max = this.availableActions.length;
					this.sliceActions = this.availableActions.splice(3, max);
				}
			})
		).subscribe();
	}

	private showAdministrativeDischargeButton(hasEvolutionNote: boolean): boolean{
		return (
			this.hasRoleAdministrative
			&& ((this.episodeState === this.STATES.AUSENTE && !hasEvolutionNote )
				|| ((this.episodeState === this.STATES.CON_ALTA_MEDICA
					|| (this.episodeState === this.STATES.EN_ESPERA && !hasEvolutionNote))
					&& this.responseEmergencyCare.patient.typeId !== 8
				)
			)
		)
	}

	private loadCanBeAbsentCondition(): Observable<boolean> {
		return this.emergencyCareEpisodeService.getAdministrative(this.episodeId)
			.pipe(
				tap((responseEmergencyCare) => {
					this.canBeAbsent = responseEmergencyCare.canBeAbsent;
				}),
				map(() => this.canBeAbsent)
			);
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
				this.setEpisodeState();
			}
		});

		function hasHistory(triages: TriageListDto[]) {
			return triages?.length > 0;
		}
	}

	private buildStatusLabel(stateDescription: string) {
		const description = {
			[EstadosEpisodio.EN_ESPERA]: 'guardia.home.episodes.episode.status.WAITING',
			[EstadosEpisodio.EN_ATENCION]: 'guardia.home.episodes.episode.status.IN_ATTETION',
			[EstadosEpisodio.AUSENTE]: 'guardia.home.episodes.episode.status.ABSENT',
			[EstadosEpisodio.LLAMADO]: stateDescription,
			[EstadosEpisodio.CON_ALTA_MEDICA]: 'guardia.home.episodes.episode.status.PATIENT_DISCHARGE'
		}

		this.statusLabel = {stateId: this.episodeState, description: description[this.episodeState]}
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
