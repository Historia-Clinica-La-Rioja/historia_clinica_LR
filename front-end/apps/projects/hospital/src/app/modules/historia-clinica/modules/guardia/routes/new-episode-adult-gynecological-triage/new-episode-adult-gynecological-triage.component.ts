import { Component, OnDestroy } from '@angular/core';
import { Router } from '@angular/router';
import { ECAdultGynecologicalDto, NewEmergencyCareDto, ResponseEmergencyCareDto } from '@api-rest/api-model';
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
	selector: 'app-new-episode-adult-gynecological-triage',
	templateUrl: './new-episode-adult-gynecological-triage.component.html',
	styleUrls: ['./new-episode-adult-gynecological-triage.component.scss']
})
export class NewEpisodeAdultGynecologicalTriageComponent implements OnDestroy {

	private readonly routePrefix = 'institucion/' + this.contextService.institutionId;
	private persistSubscription: Subscription;
	readonly NOT_DEFINED_TRIAGE_LEVEL_AVAILABLE = true;
	readonly RAISED = ButtonType.RAISED;
	isLoading = false;

	constructor(
		private readonly newEpisodeService: NewEpisodeService,
		private readonly emergencyCareEpisodeService: EmergencyCareEpisodeService,
		private readonly router: Router,
		private readonly contextService: ContextService,
		private readonly snackBarService: SnackBarService,
		private readonly guardiaRouterService: GuardiaRouterService,
		readonly triageActionsService: TriageActionsService,
	) {
		this.persistSubscription = this.triageActionsService.persist$.pipe(take(1)).subscribe(_ => this.confirmEvent());
	}

	ngOnDestroy(): void {
		this.persistSubscription.unsubscribe();
	}

	confirmEvent(): void {
		this.isLoading = true;
		const administrative: NewEmergencyCareDto = this.newEpisodeService.getAdministrativeAdmissionDto();
		const dto: ECAdultGynecologicalDto = {
			administrative,
			triage: this.triageActionsService.triageAdultGynecological
		};
		this.emergencyCareEpisodeService.createAdult(dto)
			.subscribe((episodeId: number) => {
				this.emergencyCareEpisodeService.getAdministrative(episodeId).subscribe((dto: ResponseEmergencyCareDto) => {
					this.isLoading = false;
					this.guardiaRouterService.goToEpisode(episodeId, { typeId: dto.patient.typeId, id: dto.patient.id })
					this.snackBarService.showSuccess('guardia.new-episode.SUCCESS');
				})
			}, error => {
				error?.text ?
					this.snackBarService.showError(error.text) : this.snackBarService.showError('guardia.new-episode.ERROR');
					this.isLoading = false;
				}
			);
	}

	back(): void {
		this.router.navigate([this.routePrefix + ROUTE_EMERGENCY_CARE + '/nuevo-episodio/administrativa'],
			{ state: { commingBack: true } });
	}

}
