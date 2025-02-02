import { AdministrativeTriageDialogComponent } from '../../dialogs/administrative-triage-dialog/administrative-triage-dialog.component';
import { AdultGynecologicalTriageDialogComponent } from '../../dialogs/adult-gynecological-triage-dialog/adult-gynecological-triage-dialog.component';
import { anyMatch } from '@core/utils/array.utils';
import { AppFeature, ERole } from '@api-rest/api-model';
import { AttentionPlace, PatientType } from '@historia-clinica/constants/summaries';
import { Component, EventEmitter, Input, OnDestroy, OnInit, Output } from '@angular/core';
import { ConfirmDialogData, ConfirmDialogV2Component } from '@presentation/dialogs/confirm-dialog-v2/confirm-dialog-v2.component';
import { ContextService } from '@core/services/context.service';
import { DialogConfiguration, DialogService, DialogWidth } from '@presentation/services/dialog.service';
import { EmergencyCareEpisodeCallOrAttendService } from '@historia-clinica/services/emergency-care-episode-call-or-attend.service';
import { EmergencyCareEpisodeService } from '@api-rest/services/emergency-care-episode.service';
import { EmergencyCareTemporaryPatientService } from '../../services/emergency-care-temporary-patient.service';
import { Episode } from '../emergency-care-episodes-summary/emergency-care-episodes-summary.component';
import { EpisodeStateService } from '@historia-clinica/modules/guardia/services/episode-state.service';
import { EstadosEpisodio } from '../../constants/masterdata';
import { FeatureFlagService } from '@core/services/feature-flag.service';
import { PediatricTriageDialogComponent } from '../../dialogs/pediatric-triage-dialog/pediatric-triage-dialog.component';
import { PermissionsService } from '@core/services/permissions.service';
import { Router } from '@angular/router';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { Subscription, switchMap, take } from 'rxjs';
import { TranslateService } from '@ngx-translate/core';
import { TriageDefinitionsService } from '../../services/triage-definitions.service';

@Component({
	selector: 'app-emergency-care-dashboard-actions',
	templateUrl: './emergency-care-dashboard-actions.component.html',
	styleUrls: ['./emergency-care-dashboard-actions.component.scss'],
})
export class EmergencyCareDashboardActionsComponent implements OnInit, OnDestroy {

	@Input() episode: Episode;
	@Output() triageDialogClosed = new EventEmitter<number>();
	@Output() patientDescriptionUpdate = new EventEmitter<PatientDescriptionUpdate>();
	@Output() markedAsAbsent = new EventEmitter<boolean>();

	readonly estadosEpisodio = EstadosEpisodio;
	readonly EMERGENCY_CARE_TEMPORARY = PatientType.EMERGENCY_CARE_TEMPORARY;

	private hasAdministrativeRole: boolean;
	private hasProffesionalRole: boolean;
	private isAdministrativeAndHasTriageFFInFalse: boolean;

	private patientDescriptionSubscription: Subscription;

	constructor(
		private router: Router,
		private contextService: ContextService,
		private readonly permissionsService: PermissionsService,
		private readonly featureFlagService: FeatureFlagService,
		private readonly triageDefinitionsService: TriageDefinitionsService,
		private readonly dialogTriageService: DialogService<AdministrativeTriageDialogComponent|PediatricTriageDialogComponent|AdultGynecologicalTriageDialogComponent>,
		private readonly emergencyCareEpisodeAttend: EmergencyCareEpisodeCallOrAttendService,
		private readonly emergencyCareTemporaryPatientService: EmergencyCareTemporaryPatientService,
		private emergencyCareEpisodeService: EmergencyCareEpisodeService,
		private readonly translate: TranslateService,
		private readonly episodeStateService: EpisodeStateService,
		private snackBarService: SnackBarService,
		private readonly dialogService: DialogService<ConfirmDialogV2Component>,
	) { }

	ngOnInit() {
		this.setRoles();
		this.checkAdministrativeFF();
	}

	ngOnDestroy() {
		this.patientDescriptionSubscription?.unsubscribe();
	}

	private setRoles() {
		this.permissionsService.contextAssignments$().subscribe((userRoles: ERole[]) => {
			this.hasAdministrativeRole = anyMatch<ERole>(userRoles, [ERole.ADMINISTRATIVO, ERole.ADMINISTRATIVO_RED_DE_IMAGENES]);
			this.hasProffesionalRole = anyMatch<ERole>(userRoles, [ERole.ENFERMERO, ERole.PROFESIONAL_DE_SALUD, ERole.ESPECIALISTA_MEDICO, ERole.ESPECIALISTA_EN_ODONTOLOGIA]);
		});
	}

	private checkAdministrativeFF() {
		this.featureFlagService.isActive(AppFeature.HABILITAR_TRIAGE_PARA_ADMINISTRATIVO).subscribe(isEnabled =>
			this.isAdministrativeAndHasTriageFFInFalse = this.hasProffesionalRole
				? false
				: (!isEnabled && this.hasAdministrativeRole)
		)
	}

	private getActionsButtonsConditions(episode: Episode): EpisodeConditions {
		const showCall =
			this.hasProffesionalRole &&
			episode.state.id !== this.estadosEpisodio.EN_ATENCION;

		const showNewTriage = !this.isAdministrativeAndHasTriageFFInFalse;

		const showEditPatientEpisode =
			([this.estadosEpisodio.EN_ESPERA, this.estadosEpisodio.EN_ATENCION, this.estadosEpisodio.AUSENTE].includes(episode.state.id ))

		const showEditPatientDescription =
			episode.patient?.typeId === this.EMERGENCY_CARE_TEMPORARY;

		const showAttend =
			([this.estadosEpisodio.EN_ESPERA,this.estadosEpisodio.AUSENTE, this.estadosEpisodio.LLAMADO].includes(episode.state.id ))
			&& this.hasProffesionalRole;

		const showMarkAsAbsent = episode?.canBeAbsent;

		return {
			call: showCall,
			newTriage: showNewTriage,
			attend: showAttend,
			editPatientEpisode: showEditPatientEpisode,
			editPatientDescription: showEditPatientDescription,
			markAsAbsent: showMarkAsAbsent
		};
	}

	private openTriageDialog() {
		this.triageDefinitionsService.getTriagePath(this.episode.type?.id)
			.subscribe(({ component }) => {
				const dialogRef = this.dialogTriageService.open(component,
					{dialogWidth: DialogWidth.SMALL},
					this.episode.id
				);
				dialogRef.afterClosed().subscribe(idReturned => {
					this.triageDialogClosed.emit(idReturned);
				});
			}
		);
	}

	private editPatientEpisode() {
		this.router.navigate([`/institucion/${this.contextService.institutionId}/guardia/episodio/${this.episode.id}/dashboard/edit`]);
	}

	private call() {
		this.emergencyCareEpisodeAttend.callOrAttendPatient(this.episode.id, true, true);
	}

	private attend() {
		this.emergencyCareEpisodeAttend.callOrAttendPatient(this.episode.id, true, false);
	}

	private editPatientDescription() {
		this.patientDescriptionSubscription = this.emergencyCareTemporaryPatientService.patientDescription$.subscribe(
			patientDescription => {
				if (patientDescription)
					this.updatePatientDescription(patientDescription);
			}
		);
		this.emergencyCareTemporaryPatientService.openTemporaryPatient(this.episode.patient.patientDescription);
	}

	private updatePatientDescription(patientDescription: string) {
		this.emergencyCareEpisodeService.updatePatientDescription(this.episode.id, patientDescription).subscribe({
			next: () => {
				const patientUpdate: PatientDescriptionUpdate = {
					episodeId: this.episode.id,
					patientDescription: patientDescription
				};
				this.patientDescriptionUpdate.emit(patientUpdate);
			}
		});
	}

	private markPatientAsAbscent(){
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
                    return this.episodeStateService.markAsAbsent(this.episode.id);
                }
            }))
            .subscribe({
                next: (changed) => {
                    if (changed) {
                        this.snackBarService.showSuccess(this.translate.instant('guardia.home.episodes.episode.actions.mark_as_absent.SUCCESS'));
                        this.markedAsAbsent.emit(true);
                    }
                    else
                        this.snackBarService.showError(this.translate.instant('guardia.home.episodes.episode.actions.mark_as_absent.ERROR'));
                },
                error: () => {}
            }
        );
	}

	showActionsButton(episode: Episode): boolean {
		const conditions = this.getActionsButtonsConditions(episode);
		return Object.values(conditions).some(condition => condition);
	}

	getEpisodeActions(episode: Episode): CategorizedAction[] {
		const conditions = this.getActionsButtonsConditions(episode);
		const actions = [
			{
				id: 'call',
				category: 'call-related',
				icon: 'call',
				condition: conditions.call,
				label: this.translate.instant('guardia.home.episodes.episode.actions.call.TITLE'),
				callback: () => this.call()
			},
			{
				id: 'attend',
				category: 'call-related',
				icon: 'check_circle',
				condition: conditions.attend,
				label: this.translate.instant('guardia.home.episodes.episode.actions.atender.TITLE'),
				callback: () => this.attend(),
			},
			{
				id: 'nuevo-triage',
				category: 'professional-actions',
				icon: 'add',
				condition: conditions.newTriage,
				label: this.translate.instant('guardia.home.episodes.episode.actions.NUEVO_TRIAGE'),
				callback: () => this.openTriageDialog()

			},
			{
				id: 'editPatientEpisode',
				category: 'edits',
				icon: 'edit',
				condition: conditions.editPatientEpisode,
				label: this.translate.instant('guardia.home.episodes.episode.actions.edit_patient_episode.TITLE'),
				callback: () => this.editPatientEpisode()
			},
			{
				id: 'editPatientDescription',
				category: 'edits',
				icon: 'edit',
				condition: conditions.editPatientDescription,
				label: this.translate.instant('guardia.home.episodes.episode.actions.edit_patient_description.TITLE'),
				callback: () => this.editPatientDescription()
			},
			{
				id: 'markAsAbsent',
				category: 'final-state',
				icon: 'person_off',
				condition: conditions.markAsAbsent,
				label: this.translate.instant('guardia.home.episodes.episode.actions.mark_as_absent.TITLE'),
				callback: () => this.markPatientAsAbscent()
			}
		];

		return actions.filter(action => action.condition)
			.map(action => ({
				category: action.category,
				episodeAction: action
			})
		);
	}

	trackbyFn(index: number) {
		return index;
	}

	showDivider(categorizedActions: CategorizedAction[], index: number): boolean {
		if (index === 0) {
			return false;
		}
		return categorizedActions[index].category !== categorizedActions[index - 1].category;
	}
}

interface CategorizedAction {
	category: string;
	episodeAction: EpisodeAction;
}

interface EpisodeAction {
	id: string;
	category: string;
	icon: string;
	condition: boolean;
	label: string;
	callback: Function;
}

interface EpisodeConditions {
	call: boolean;
	newTriage: boolean;
	attend: boolean;
	editPatientEpisode: boolean;
	editPatientDescription: boolean;
	markAsAbsent: boolean;
}

export interface AttendPlace {
	id: number,
	attentionPlace: AttentionPlace
}

export interface PatientDescriptionUpdate {
	episodeId: number,
	patientDescription: string
}
