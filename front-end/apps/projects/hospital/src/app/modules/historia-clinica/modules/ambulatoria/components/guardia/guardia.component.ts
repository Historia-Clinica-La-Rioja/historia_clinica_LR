import { Component, Input, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { Router, ActivatedRoute } from '@angular/router';
import { EmergencyCareEpisodeInProgressDto, ResponseEmergencyCareDto } from '@api-rest/api-model';
import { EmergencyCareEpisodeStateService } from '@api-rest/services/emergency-care-episode-state.service';
import { ContextService } from '@core/services/context.service';
import { FACTORES_DE_RIESGO } from '@historia-clinica/constants/summaries';
import { RiskFactorFull } from '@historia-clinica/modules/guardia/components/triage-details/triage-details.component';
import { Triages, EstadosEpisodio } from '@historia-clinica/modules/guardia/constants/masterdata';
import { SelectConsultorioComponent } from '@historia-clinica/modules/guardia/dialogs/select-consultorio/select-consultorio.component';
import { EpisodeStateService } from '@historia-clinica/modules/guardia/services/episode-state.service';
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
    responseEmergencyCare: ResponseEmergencyCareDto;

    episodeState: EstadosEpisodio;

    riskFactors: RiskFactorFull[];

    showEmergencyCareSummary = false;
    withoutMedicalDischarge: boolean;

    constructor(
        private readonly router: Router,
        private readonly route: ActivatedRoute,
        private readonly dialog: MatDialog,
        private snackBarService: SnackBarService,
        private readonly episodeStateService: EpisodeStateService,
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

    cancelAttention() {
        const dialogRef = this.dialog.open(SelectConsultorioComponent, {
            width: '25%',
            data: { title: 'ambulatoria.paciente.guardia.CANCEL_BUTTON' }
        });

        dialogRef.afterClosed().subscribe(consultorio => {
            if (consultorio) {
                this.episodeStateService.cancelar(this.episodeId, consultorio.id).subscribe(changed => {
                    if (changed) {
                        this.snackBarService.showSuccess('ambulatoria.paciente.guardia.CANCEL_ATTENTION_SUCCESS');
                        this.episodeState = EstadosEpisodio.EN_ESPERA;
                    } else {
                        this.snackBarService.showError('ambulatoria.paciente.guardia.CANCEL_ATTENTION_ERROR');
                    }
                }, _ => this.snackBarService.showError('ambulatoria.paciente.guardia.CANCEL_ATTENTION_ERROR')
                );
            }
        });
    }

    goToMedicalDischarge() {
        if (!this.responseEmergencyCare?.patient) {
            this.snackBarService.showError('ambulatoria.paciente.guardia.PATIENT_REQUIRED');
        } else {
            this.router.navigate([`/institucion/${this.contextService.institutionId}/guardia/episodio/${this.episodeId}/alta-medica`]);
        }
    }

    goToAdministrativeDischarge() {
        this.router.navigate([`/institucion/${this.contextService.institutionId}/guardia/episodio/${this.episodeId}/alta-administrativa`]);
    }

    goToEditEpisode() {
        this.router.navigate([`/institucion/${this.contextService.institutionId}/guardia/episodio/${this.episodeId}/edit`]);
    }

    updateRiskFactors(triageRiskFactors: RiskFactorFull[]): void {
        this.riskFactors = triageRiskFactors;
    }

    private init() {
        this.emergencyCareEpisodeStateService.getState(this.episodeId).subscribe(
            state => {
                this.episodeState = state.id;

                this.withoutMedicalDischarge = (this.episodeState !== this.STATES.CON_ALTA_MEDICA);

                if (this.isActive(this.episodeState)) {
                    this.showEmergencyCareSummary = true;
                } else {
                    this.snackBarService.showError('ambulatoria.paciente.guardia.NOT_ACTIVE');
                    this.goToEmergencyCareHome();
                }
            }, error => {
                this.snackBarService.showError(error.text);
                this.goToEmergencyCareHome();
            }
        );
    }

    private isActive(episodeStateId: number): boolean {
        return episodeStateId === this.STATES.EN_ATENCION
            || episodeStateId === this.STATES.EN_ESPERA
            || episodeStateId === this.STATES.CON_ALTA_MEDICA;
    }

    private goToEmergencyCareHome() {
        this.router.navigateByUrl(this.routePrefix + '/guardia');
    }

}
