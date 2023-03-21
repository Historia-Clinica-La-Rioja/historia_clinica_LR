import { Component } from '@angular/core';
import { ECAdministrativeDto, ResponseEmergencyCareDto, TriageAdministrativeDto } from '@api-rest/api-model';
import { ERole } from '@api-rest/api-model';
import { NewEpisodeService } from '../../services/new-episode.service';
import { EmergencyCareEpisodeService } from '@api-rest/services/emergency-care-episode.service';
import { Router } from '@angular/router';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { ContextService } from '@core/services/context.service';
import { ROUTE_EMERGENCY_CARE } from '../../services/triage-definitions.service';
import { anyMatch } from '@core/utils/array.utils';
import { PermissionsService } from '@core/services/permissions.service';

@Component({
	selector: 'app-new-episode-admin-triage',
	templateUrl: './new-episode-admin-triage.component.html',
	styleUrls: ['./new-episode-admin-triage.component.scss']
})
export class NewEpisodeAdminTriageComponent {

	private triage: TriageAdministrativeDto;
	private emergencyCareDto = {} as ECAdministrativeDto;
	private readonly routePrefix;
	private hasRoleAdministrative: boolean;

	constructor(
		private readonly newEpisodeService: NewEpisodeService,
		private readonly emergencyCareEpisodeService: EmergencyCareEpisodeService,
		private router: Router,
		private snackBarService: SnackBarService,
		private contextService: ContextService,
		private readonly permissionsService: PermissionsService,
	) {
		this.routePrefix = 'institucion/' + this.contextService.institutionId;
		this.permissionsService.contextAssignments$().subscribe((userRoles: ERole[]) => {
			this.hasRoleAdministrative = anyMatch<ERole>(userRoles, [ERole.ADMINISTRATIVO]);
		});
	}

	confirmEvent(triage: TriageAdministrativeDto): void {
		this.triage = triage;
		this.emergencyCareDto.triage = this.triage;
		this.emergencyCareDto.administrative = this.newEpisodeService.getAdministrativeAdmissionDto();
		this.emergencyCareEpisodeService.createAdministrative(this.emergencyCareDto).subscribe(
			emergencyCareId => {
				this.emergencyCareEpisodeService.getAdministrative(emergencyCareId).subscribe((dto: ResponseEmergencyCareDto) => {
					const patientId = dto.patient ? dto.patient.id : null;
					if (patientId && !this.hasRoleAdministrative) {
						this.router.navigate([this.routePrefix + "/ambulatoria/paciente/" + patientId])
					}
					else {
						this.router.navigate([this.routePrefix + ROUTE_EMERGENCY_CARE + '/episodio/' + emergencyCareId]);
					}
					this.snackBarService.showSuccess('guardia.new-episode.SUCCESS');
				});
			},
			error =>
				error?.text ?
					this.snackBarService.showError(error.text) : this.snackBarService.showError('guardia.new-episode.ERROR')
		);
	}

	cancelEvent(): void {
		this.router.navigate([this.routePrefix + ROUTE_EMERGENCY_CARE + '/nuevo-episodio/administrativa'], { state: { commingBack: true } });
	}

}
