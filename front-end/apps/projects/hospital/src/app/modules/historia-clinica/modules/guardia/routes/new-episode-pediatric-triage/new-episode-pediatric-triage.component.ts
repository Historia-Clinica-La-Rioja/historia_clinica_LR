import { Component, OnDestroy } from '@angular/core';
import { Router } from '@angular/router';
import { ECPediatricDto, ResponseEmergencyCareDto } from '@api-rest/api-model';
import { EmergencyCareEpisodeService } from '@api-rest/services/emergency-care-episode.service';
import { ContextService } from '@core/services/context.service';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { GuardiaRouterService } from '../../services/guardia-router.service';
import { NewEpisodeService } from '../../services/new-episode.service';
import { ROUTE_EMERGENCY_CARE } from '../../services/triage-definitions.service';
import { ButtonType } from '@presentation/components/button/button.component';
import { TriageActionsService } from '../../services/triage-actions.service';
import { Subscription, take } from 'rxjs';

@Component({
	selector: 'app-new-episode-pediatric-triage',
	templateUrl: './new-episode-pediatric-triage.component.html',
	styleUrls: ['./new-episode-pediatric-triage.component.scss']
})
export class NewEpisodePediatricTriageComponent implements OnDestroy {

	private readonly routePrefix = 'institucion/' + this.contextService.institutionId;
	private persistSuscription: Subscription;
	readonly RAISED = ButtonType.RAISED;
	readonly NOT_DEFINED_TRIAGE_LEVEL_AVAILABLE = true;
	isLoading = false;

	constructor(
		private readonly newEpisodeService: NewEpisodeService,
		private readonly emergercyCareEpisodeService: EmergencyCareEpisodeService,
		private readonly router: Router,
		private readonly contextService: ContextService,
		private readonly snackBarService: SnackBarService,
		private readonly guardiaRouterService: GuardiaRouterService,
		readonly triageActionsService: TriageActionsService,
	) {
		this.persistSuscription = this.triageActionsService.persist$.pipe(take(1)).subscribe(_ => this.confirmEvent());
	}

	ngOnDestroy(): void {
		this.persistSuscription.unsubscribe();
	}

	confirmEvent(): void {
		this.isLoading = true;
		const administrative = this.newEpisodeService.getAdministrativeAdmissionDto();
		const body: ECPediatricDto = {
			administrative,
			triage: this.triageActionsService.pediatricTriage
		};

		this.emergercyCareEpisodeService.createPediatric(body).subscribe(
			episodeId => {
				this.emergercyCareEpisodeService.getAdministrative(episodeId).subscribe((dto: ResponseEmergencyCareDto) => {
					this.guardiaRouterService.goToEpisode(episodeId, { typeId: dto.patient.typeId, id: dto.patient.id });
					this.snackBarService.showSuccess('guardia.new-episode.SUCCESS');
					this.isLoading = false;
				})
			}, error => {
				error?.text ?
					this.snackBarService.showError(error.text) : this.snackBarService.showError('guardia.new-episode.ERROR');
					this.isLoading = false;
			}
		)
	}

	cancelEvent(): void {
		this.router.navigate([this.routePrefix + ROUTE_EMERGENCY_CARE + '/nuevo-episodio/administrativa'],
			{ state: { commingBack: true } });
	}
}
