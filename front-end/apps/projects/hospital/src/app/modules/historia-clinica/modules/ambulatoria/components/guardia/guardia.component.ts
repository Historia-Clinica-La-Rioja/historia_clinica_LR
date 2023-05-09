import { Component, Input, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { EmergencyCareEpisodeInProgressDto } from '@api-rest/api-model';
import { EmergencyCareEpisodeStateService } from '@api-rest/services/emergency-care-episode-state.service';
import { ContextService } from '@core/services/context.service';
import { FACTORES_DE_RIESGO } from '@historia-clinica/constants/summaries';
import { RiskFactorFull } from '@historia-clinica/modules/guardia/components/triage-details/triage-details.component';
import { Triages, EstadosEpisodio } from '@historia-clinica/modules/guardia/constants/masterdata';
import { SummaryHeader } from '@presentation/components/summary-card/summary-card.component';
import { SnackBarService } from '@presentation/services/snack-bar.service';

@Component({
    selector: 'app-guardia',
    templateUrl: './guardia.component.html',
    styleUrls: ['./guardia.component.scss']
})
export class GuardiaComponent implements OnInit {

    @Input() emergencyCareEpisodeInProgress: EmergencyCareEpisodeInProgressDto;
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

    updateRiskFactors(triageRiskFactors: RiskFactorFull[]): void {
        this.riskFactors = triageRiskFactors;
    }

    private init() {
        this.emergencyCareEpisodeStateService.getState(this.episodeId).subscribe(
            state => {
                this.episodeState = state.id;

                this.withoutMedicalDischarge = (this.episodeState !== this.STATES.CON_ALTA_MEDICA);
				this.showEmergencyCareSummary = true;
            }, error => {
                this.snackBarService.showError(error.text);
                this.goToEmergencyCareHome();
            }
        );
    }


    private goToEmergencyCareHome() {
        this.router.navigateByUrl(this.routePrefix + '/guardia');
    }

}
