import { Component, Input, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { EmergencyCareEpisodeInProgressDto } from '@api-rest/api-model';
import { EmergencyCareEpisodeStateService } from '@api-rest/services/emergency-care-episode-state.service';
import { EmergencyCareEpisodeService } from '@api-rest/services/emergency-care-episode.service';
import { ContextService } from '@core/services/context.service';
import { FACTORES_DE_RIESGO } from '@historia-clinica/constants/summaries';
import { RiskFactorFull } from '@historia-clinica/modules/guardia/components/triage-details/triage-details.component';
import { Triages, EstadosEpisodio } from '@historia-clinica/modules/guardia/constants/masterdata';
import { mapToRiskFactorFull } from '@historia-clinica/modules/guardia/utils/riskFactors.utils';
import { NewRiskFactorsService } from '@historia-clinica/modules/guardia/services/new-risk-factors.service';
import { SummaryHeader } from '@presentation/components/summary-card/summary-card.component';
import { SnackBarService } from '@presentation/services/snack-bar.service';

@Component({
	selector: 'app-guardia',
	templateUrl: './guardia.component.html',
	styleUrls: ['./guardia.component.scss']
})
export class GuardiaComponent implements OnInit {

	@Input() emergencyCareEpisodeInProgress: EmergencyCareEpisodeInProgressDto;
	@Input() isEmergencyCareTemporalPatient: boolean = false;
	factoresDeRiesgoSummary: SummaryHeader = FACTORES_DE_RIESGO;

	readonly triages = Triages;
	readonly STATES = EstadosEpisodio;
	private readonly routePrefix;

	episodeId: number;
	episodeState: EstadosEpisodio;

	riskFactors: RiskFactorFull[];

	showEmergencyCareSummary = false;
	withoutMedicalDischarge: boolean;

	constructor(
		private readonly router: Router,
		private readonly route: ActivatedRoute,
		private snackBarService: SnackBarService,
		private readonly emergencyCareEpisodeStateService: EmergencyCareEpisodeStateService,
		private readonly contextService: ContextService,
		private readonly newRiskFactorsService: NewRiskFactorsService,
		private readonly emergencyCareEpisodeService: EmergencyCareEpisodeService,
	) {
		this.routePrefix = 'institucion/' + this.contextService.institutionId;
	}

	ngOnInit() {
		this.route.paramMap.subscribe(
			(params) => {
				this.episodeId = Number(params.get('episodeId'));

				if (params.get('episodeId')) {
					this.episodeId = Number(params.get('episodeId'));
				} else {
					this.episodeId = this.emergencyCareEpisodeInProgress?.id;
				}
				this.init();
			}
		);
	}

	private init() {
		this.emergencyCareEpisodeStateService.getState(this.episodeId).subscribe(
			state => {
				this.episodeState = state.id;

				this.withoutMedicalDischarge = (this.episodeState !== this.STATES.CON_ALTA_MEDICA);
				this.showEmergencyCareSummary = true;

				this.setRiskFactorEpisode();
				this.subscribeToUpdatesRiskFactor();
			}, error => {
				this.snackBarService.showError(error.text);
				this.goToEmergencyCareHome();
			}
		);
	}

	private goToEmergencyCareHome() {
		this.router.navigateByUrl(this.routePrefix + '/guardia');
	}

	private subscribeToUpdatesRiskFactor() {
		this.newRiskFactorsService.newRiskFactors$.subscribe(_ => this.setRiskFactorEpisode());
	}

	private setRiskFactorEpisode() {
		this.emergencyCareEpisodeService.getRiskFactorsGeneralState(this.episodeId).subscribe(newRiskFactors => {
			const riskFactors = mapToRiskFactorFull(newRiskFactors);
			this.updateRiskFactors(riskFactors);
		});
	}

	private updateRiskFactors(riskFactors: RiskFactorFull[]): void {
		this.riskFactors = riskFactors;
	}

}
