import { Component, EventEmitter, Input, OnDestroy, OnInit, Output } from '@angular/core';
import { Episode } from '../../routes/home/home.component';
import { EstadosEpisodio } from '../../constants/masterdata';
import { FeatureFlagService } from '@core/services/feature-flag.service';
import { AppFeature, ERole } from '@api-rest/api-model';
import { PermissionsService } from '@core/services/permissions.service';
import { anyMatch } from '@core/utils/array.utils';
import { AttentionPlace, PatientType } from '@historia-clinica/constants/summaries';
import { ContextService } from '@core/services/context.service';
import { Router } from '@angular/router';
import { TriageDefinitionsService } from '../../services/triage-definitions.service';
import { MatDialog } from '@angular/material/dialog';
import { EmergencyCareEpisodeAttendService } from '@historia-clinica/services/emergency-care-episode-attend.service';
import { Subscription } from 'rxjs';
import { EmergencyCareTemporaryPatientService } from '../../services/emergency-care-temporary-patient.service';
import { EmergencyCareEpisodeService } from '@api-rest/services/emergency-care-episode.service';

@Component({
  selector: 'app-emergency-care-dashboard-actions',
  templateUrl: './emergency-care-dashboard-actions.component.html',
  styleUrls: ['./emergency-care-dashboard-actions.component.scss']
})
export class EmergencyCareDashboardActionsComponent implements OnInit, OnDestroy {

	@Input() episode: Episode;
	@Output() triageDialogClosed = new EventEmitter<number>();
	@Output() patientDescriptionUpdate = new EventEmitter<PatientDescriptionUpdate>();

	readonly estadosEpisodio = EstadosEpisodio;
	readonly EMERGENCY_CARE_TEMPORARY = PatientType.EMERGENCY_CARE_TEMPORARY;

	private hasAdministrativeRole: boolean;
	hasProffesionalRole: boolean;
	isAdministrativeAndHasTriageFFInFalse: boolean;

	private patientDescriptionSubscription: Subscription;

	constructor(
		private router: Router,
		private contextService: ContextService,
		private readonly permissionsService: PermissionsService,
		private readonly featureFlagService: FeatureFlagService,
		private readonly triageDefinitionsService: TriageDefinitionsService,
		private readonly dialog: MatDialog,
		private readonly emergencyCareEpisodeAttend: EmergencyCareEpisodeAttendService,
		private readonly emergencyCareTemporaryPatientService: EmergencyCareTemporaryPatientService,
		private emergencyCareEpisodeService: EmergencyCareEpisodeService,
	) { }

	ngOnInit(): void {
		this.setRoles();
		this.checkAdministrativeFF();
	}

	ngOnDestroy(): void {
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
			this.hasProffesionalRole
			? this.isAdministrativeAndHasTriageFFInFalse = false
			: this.isAdministrativeAndHasTriageFFInFalse = (!isEnabled && this.hasAdministrativeRole)
		)
	}

	showActionsButton(episode : Episode): boolean{
		return !this.isAdministrativeAndHasTriageFFInFalse ||
		episode.patient?.typeId === this.EMERGENCY_CARE_TEMPORARY;
	}

	openTriageDialog() {
		this.triageDefinitionsService.getTriagePath(this.episode.type?.id)
		  .subscribe(({ component }) => {
			const dialogRef = this.dialog.open(component, { data: this.episode.id });
			dialogRef.afterClosed().subscribe(idReturned => {
			  this.triageDialogClosed.emit(idReturned);
			});
		  });
	}

	editPatientEpisode(episodeId: number) {
		this.router.navigate([`/institucion/${this.contextService.institutionId}/guardia/episodio/${episodeId}/edit`]);
	}

	atender(episodeId: number) {
		this.emergencyCareEpisodeAttend.attend(episodeId, true);
	}

	editPatientDescription(episodeId: number, preloadedReason: string) {
		this.patientDescriptionSubscription = this.emergencyCareTemporaryPatientService.patientDescription$.subscribe(patientDescription => {
			if (patientDescription){
				this.updatePatientDescription(episodeId, patientDescription);}
		});
		this.emergencyCareTemporaryPatientService.openTemporaryPatient(preloadedReason);
	}

	private updatePatientDescription(episodeId: number, patientDescription: string) {
		this.emergencyCareEpisodeService.updatePatientDescription(episodeId, patientDescription).subscribe({
			next: () => {
				const patientUpdate: PatientDescriptionUpdate = {
					episodeId: this.episode.id,
					patientDescription: patientDescription
				};
				this.patientDescriptionUpdate.emit(patientUpdate);
			}
		});
	}
}

export interface AttendPlace {
	id: number,
	attentionPlace: AttentionPlace
}

export interface PatientDescriptionUpdate {
	episodeId: number,
	patientDescription: string
}
