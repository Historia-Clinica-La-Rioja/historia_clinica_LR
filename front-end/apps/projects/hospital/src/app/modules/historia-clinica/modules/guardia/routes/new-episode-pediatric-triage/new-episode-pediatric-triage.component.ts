import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { ECPediatricDto, TriagePediatricDto } from '@api-rest/api-model';
import { EmergencyCareEpisodeService } from '@api-rest/services/emergency-care-episode.service';
import { ContextService } from '@core/services/context.service';
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
	constructor(
		private readonly newEpisodeService: NewEpisodeService,
		private readonly emergercyCareEpisodeService: EmergencyCareEpisodeService,
		private readonly router: Router,
		private readonly contextService: ContextService,
		private readonly snackBarService: SnackBarService,

	) {
	}

	confirmEvent(triage: TriagePediatricDto): void {

		const administrative = this.newEpisodeService.getAdministrativeAdmissionDto();
		const body: ECPediatricDto = {
			administrative,
			triage
		};

		this.emergercyCareEpisodeService.createPediatric(body).subscribe(
			episodeId => {
				this.router.navigate([this.routePrefix + ROUTE_EMERGENCY_CARE + '/episodio/' + episodeId]);
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
