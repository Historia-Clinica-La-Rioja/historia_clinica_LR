import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { ECPediatricDto, TriagePediatricDto } from '@api-rest/api-model';
import { ERole } from '@api-rest/api-model';
import { EmergencyCareEpisodeService } from '@api-rest/services/emergency-care-episode.service';
import { ContextService } from '@core/services/context.service';
import { PermissionsService } from '@core/services/permissions.service';
import { anyMatch } from '@core/utils/array.utils';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { NewEpisodeService } from '../../services/new-episode.service';
import { ROUTE_EMERGENCY_CARE } from '../../services/triage-definitions.service';

@Component({
	selector: 'app-new-episode-pediatric-triage',
	templateUrl: './new-episode-pediatric-triage.component.html',
	styleUrls: ['./new-episode-pediatric-triage.component.scss']
})
export class NewEpisodePediatricTriageComponent {

	private readonly routePrefix = 'institucion/' + this.contextService.institutionId;
	private hasEmergencyCareRelatedRole: boolean;

	constructor(
		private readonly newEpisodeService: NewEpisodeService,
		private readonly emergercyCareEpisodeService: EmergencyCareEpisodeService,
		private readonly router: Router,
		private readonly contextService: ContextService,
		private readonly snackBarService: SnackBarService,
		private readonly permissionsService: PermissionsService,
	) {
		this.permissionsService.contextAssignments$().subscribe((userRoles: ERole[]) => {
			this.hasEmergencyCareRelatedRole = anyMatch<ERole>(userRoles, [ERole.ESPECIALISTA_MEDICO, ERole.ENFERMERO, ERole.PROFESIONAL_DE_SALUD]);
		});
	}

	confirmEvent(triage: TriagePediatricDto): void {

		const administrative = this.newEpisodeService.getAdministrativeAdmissionDto();
		const body: ECPediatricDto = {
			administrative,
			triage
		};

		this.emergercyCareEpisodeService.createPediatric(body).subscribe(
			episodeId => {
				const patientId = body.administrative?.patient?.id ? body.administrative.patient.id : null;
				if (patientId && this.hasEmergencyCareRelatedRole) {
					this.router.navigate([this.routePrefix + "/ambulatoria/paciente/" + patientId], { state: { toEmergencyCareTab: true } })
				}
				else if (patientId) {
					const url = `${this.routePrefix}/pacientes/profile/${patientId}`;
					this.router.navigateByUrl(url);
				}
				else {
					this.router.navigate([this.routePrefix + ROUTE_EMERGENCY_CARE + '/episodio/' + episodeId]);
				}
				this.snackBarService.showSuccess('guardia.new-episode.SUCCESS');
			},
			error =>
				error?.text ?
					this.snackBarService.showError(error.text) : this.snackBarService.showError('guardia.new-episode.ERROR')
		);

	}

	cancelEvent(): void {
		this.router.navigate([this.routePrefix + ROUTE_EMERGENCY_CARE + '/nuevo-episodio/administrativa'],
			{ state: { commingBack: true } });
	}
}
