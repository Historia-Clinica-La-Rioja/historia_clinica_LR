import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ContextService } from '@core/services/context.service';
import { AdministrativeAdmission, NewEpisodeService } from '../../services/new-episode.service';
import { TriageDefinitionsService } from '../../services/triage-definitions.service';
import { FeatureFlagService } from '@core/services/feature-flag.service';
import { AppFeature, ECAdministrativeDto, ERole, ResponseEmergencyCareDto, TriageDto } from '@api-rest/api-model';
import { PermissionsService } from '@core/services/permissions.service';
import { anyMatch } from '@core/utils/array.utils';
import { EmergencyCareEpisodeService } from '@api-rest/services/emergency-care-episode.service';
import { GuardiaRouterService } from '../../services/guardia-router.service';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { Triages } from '../../constants/masterdata';
import { map, switchMap } from 'rxjs';

@Component({
	selector: 'app-new-episode-admission',
	templateUrl: './new-episode-admission.component.html',
	styleUrls: ['./new-episode-admission.component.scss'],
	providers: [TriageDefinitionsService]
})
export class NewEpisodeAdmissionComponent implements OnInit {

	initData: AdministrativeAdmission;

	private readonly routePrefix = 'institucion/' + this.contextService.institutionId;
	private isAdministrativeAndHasTriageFFInFalse : boolean;
	private hasAdministrativeRole: boolean;
	private hasRoleAbleToSeeTriage: boolean;

	constructor(private readonly newEpisodeService: NewEpisodeService,
		private readonly triageDefinitionsService: TriageDefinitionsService,
		private readonly router: Router,
		private readonly contextService: ContextService,
		private readonly permissionsService: PermissionsService,
		private readonly featureFlagService: FeatureFlagService,
		private readonly emergencyCareEpisodeService: EmergencyCareEpisodeService,
		private readonly guardiaRouterService: GuardiaRouterService,
		private snackBarService: SnackBarService,
	) { }

	ngOnInit(): void {

		if (window.history.state.commingBack) {
			this.initData = this.newEpisodeService.getAdministrativeAdmission();
		}

		this.setRoles();

		this.checkAdministrativeFF();
	}

	private setRoles(){
		this.permissionsService.contextAssignments$().subscribe((userRoles: ERole[]) => {
			const administrativeRoles = [ERole.ADMINISTRATIVO, ERole.ADMINISTRATIVO_RED_DE_IMAGENES];
			this.hasAdministrativeRole = anyMatch<ERole>(userRoles, administrativeRoles);
			const proffesionalRoles: ERole[] = [ERole.ENFERMERO, ERole.PROFESIONAL_DE_SALUD, ERole.ESPECIALISTA_MEDICO, ERole.ESPECIALISTA_EN_ODONTOLOGIA];
       		this.hasRoleAbleToSeeTriage = userRoles.some(role => proffesionalRoles.includes(role));
		});
	}

	private checkAdministrativeFF(){
		this.featureFlagService.isActive(AppFeature.HABILITAR_TRIAGE_PARA_ADMINISTRATIVO).subscribe(isEnabled =>
			this.hasRoleAbleToSeeTriage
			? this.isAdministrativeAndHasTriageFFInFalse = false
			: this.isAdministrativeAndHasTriageFFInFalse = (!isEnabled && this.hasAdministrativeRole)
		)
	}

	confirm(administrativeAdmission: AdministrativeAdmission): void {
		this.newEpisodeService.setAdministrativeAdmission(administrativeAdmission);

		!this.isAdministrativeAndHasTriageFFInFalse
			? this.goToTriage(administrativeAdmission)
			: this.goToHCWithUndefinedTriage();
	}

	goToTriage(administrativeAdmission: AdministrativeAdmission): void {
		this.newEpisodeService.setAdministrativeAdmission(administrativeAdmission);
		this.triageDefinitionsService.getTriagePath(administrativeAdmission?.emergencyCareTypeId)
			.subscribe(({ url }) => this.router.navigate([url], {
				queryParams: {
					patientId: administrativeAdmission.patientId,
					patientDescription: administrativeAdmission.patientDescription
				}
			}));
	}

	private goToHCWithUndefinedTriage() {
	    const undefinedTriage: TriageDto = {categoryId: Triages.GRIS_SIN_TRIAGE};
		const emergencyCareDto: ECAdministrativeDto = {
			administrative: this.newEpisodeService.getAdministrativeAdmissionDto(),
			triage: undefinedTriage
		};

		this.emergencyCareEpisodeService.createAdministrative(emergencyCareDto).pipe(
			switchMap(emergencyCareId =>
				this.emergencyCareEpisodeService.getAdministrative(emergencyCareId).pipe(
					map((dto: ResponseEmergencyCareDto) => ({
						emergencyCareStateId: dto.emergencyCareState.id,
						patient: dto.patient
					}))
				)
			)
		).subscribe(
			({ emergencyCareStateId, patient }) => {
				this.guardiaRouterService.goToEpisode(emergencyCareStateId, patient);
				this.snackBarService.showSuccess('guardia.new-episode.SUCCESS');
			},
			error =>
				error?.text
					? this.snackBarService.showError(error.text)
					: this.snackBarService.showError('guardia.new-episode.ERROR')
		);
	}

	goBack(): void {
		const url = `${this.routePrefix}/guardia`;
		this.router.navigateByUrl(url);
	}
}
