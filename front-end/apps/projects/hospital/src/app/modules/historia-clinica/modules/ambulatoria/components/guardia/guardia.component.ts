import { Component, Input, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { Router, ActivatedRoute } from '@angular/router';
import { EmergencyCareEpisodeInProgressDto, ResponseEmergencyCareDto, TriageListDto } from '@api-rest/api-model';
import { EmergencyCareEpisodeStateService } from '@api-rest/services/emergency-care-episode-state.service';
import { EmergencyCareEpisodeService } from '@api-rest/services/emergency-care-episode.service';
import { TriageService } from '@api-rest/services/triage.service';
import { ContextService } from '@core/services/context.service';
import { PatientNameService } from '@core/services/patient-name.service';
import { GUARDIA } from '@historia-clinica/constants/summaries';
import { TriageCategory } from '@historia-clinica/modules/guardia/components/triage-chip/triage-chip.component';
import { Triage } from '@historia-clinica/modules/guardia/components/triage-details/triage-details.component';
import { Triages, EstadosEpisodio, EmergencyCareTypes } from '@historia-clinica/modules/guardia/constants/masterdata';
import { SelectConsultorioComponent } from '@historia-clinica/modules/guardia/dialogs/select-consultorio/select-consultorio.component';
import { EpisodeStateService } from '@historia-clinica/modules/guardia/services/episode-state.service';
import { GuardiaMapperService } from '@historia-clinica/modules/guardia/services/guardia-mapper.service';
import { TriageDefinitionsService } from '@historia-clinica/modules/guardia/services/triage-definitions.service';
import { SearchPatientComponent } from '@pacientes/component/search-patient/search-patient.component';
import { SummaryHeader } from '@presentation/components/summary-card/summary-card.component';
import { ConfirmDialogComponent } from '@presentation/dialogs/confirm-dialog/confirm-dialog.component';
import { SnackBarService } from '@presentation/services/snack-bar.service';

@Component({
  selector: 'app-guardia',
  templateUrl: './guardia.component.html',
  styleUrls: ['./guardia.component.scss']
})
export class GuardiaComponent implements OnInit {

  @Input() emergencyCareEpisodeInProgress: EmergencyCareEpisodeInProgressDto;
  guardiaSummary: SummaryHeader = GUARDIA;

  readonly triages = Triages;
  readonly STATES = EstadosEpisodio;
  private readonly routePrefix;

  episodeId: number;
  responseEmergencyCare: ResponseEmergencyCareDto;
  doctorsOfficeDescription: string;

  emergencyCareType: EmergencyCareTypes;
  lastTriage: Triage;
  triagesHistory: TriageReduced[];
  episodeState: EstadosEpisodio;

  constructor(
    private readonly router: Router,
    private readonly route: ActivatedRoute,
    private readonly emergencyCareEpisodeService: EmergencyCareEpisodeService,
    private readonly dialog: MatDialog,
    private readonly triageService: TriageService,
    private readonly guardiaMapperService: GuardiaMapperService,
    private snackBarService: SnackBarService,
    private readonly triageDefinitionsService: TriageDefinitionsService,
    private readonly episodeStateService: EpisodeStateService,
    private readonly emergencyCareEpisodeStateService: EmergencyCareEpisodeStateService,
    private readonly contextService: ContextService,
    private readonly patientNameService: PatientNameService,
  ) {
    this.routePrefix = 'institucion/' + this.contextService.institutionId;
  }

  ngOnInit(): void {
    this.route.paramMap.subscribe(
      (params) => {
        this.episodeId = Number(params.get('episodeId'));

        if (params.get('episodeId')) {
          this.episodeId = Number(params.get('episodeId'));
        } else {
          this.episodeId = this.emergencyCareEpisodeInProgress?.id;
        }
        this.init();
      });

  }

  searchPatient(): void {
    const dialogRef = this.dialog.open(SearchPatientComponent);
    dialogRef.afterClosed().subscribe(
      patient => {
        if (patient) {
          const confirmRef = this.dialog.open(ConfirmDialogComponent, {
            data: {
              title: 'guardia.episode.search_patient.CONFIRM',
              content: `¿Está seguro que desea asociar el paciente con ID ${patient.basicData.id} al episodio de guardia?`,
              okButtonLabel: 'buttons.CONFIRM'
            }
          });

          confirmRef.afterClosed().subscribe(confirmed => {
            if (confirmed) {
              this.emergencyCareEpisodeService.setPatient(this.episodeId, patient.basicData.id).subscribe(
                saved => {
                  if (saved) {
                    this.snackBarService.showSuccess('guardia.episode.search_patient.update.SUCCESS');
                    this.responseEmergencyCare.patient = { // TODO La unica info necesaria es el id en realidad.
                      id: patient.basicData.id,
                      patientMedicalCoverageId: null,
                      person: null,
                      typeId: null
                    };
                  }
                }, error => this.snackBarService.showError(error.text));
            }
          });
        }
      });
  }

  newTriage(): void {
    this.triageDefinitionsService.getTriagePath(this.emergencyCareType)
      .subscribe(({ component }) => {
        const dialogRef = this.dialog.open(component, { data: this.episodeId });
        dialogRef.afterClosed().subscribe(idReturned => {
          if (idReturned) {
            this.loadTriages();
          }
        });
      });
  }

  cancelAttention(): void {
    const dialogRef = this.dialog.open(SelectConsultorioComponent, {
      width: '25%',
      data: { title: 'guardia.episode.CANCEL_ATTENTION' }
    });

    dialogRef.afterClosed().subscribe(consultorio => {
      if (consultorio) {
        this.episodeStateService.cancelar(this.episodeId, consultorio.id).subscribe(changed => {
          if (changed) {
            this.snackBarService.showSuccess(`guardia.episode.cancel_attention.SUCCESS`);
            this.episodeState = EstadosEpisodio.EN_ESPERA;
          } else {
            this.snackBarService.showError(`guardia.episode.cancel_attention.ERROR`);
          }
        }, _ => this.snackBarService.showError(`guardia.episode.cancel_attention.ERROR`)
        );
      }
    });
  }

  goToMedicalDischarge(): void {
    if (!this.responseEmergencyCare?.patient) {
      this.snackBarService.showError('guardia.episode.medical_discharge.PATIENT_REQUIRED');
    } else {
      this.router.navigate([`${this.router.url}/alta-medica`]);
    }
  }

  goToAdministrativeDischarge(): void {
    this.router.navigate([`${this.router.url}/alta-administrativa`]);
  }

  goToEditEpisode(): void {
    this.router.navigate([`/institucion/${this.contextService.institutionId}/guardia/episodio/${this.episodeId}/edit`]);
  }

  getFullName(triage: TriageReduced): string {
    return `${this.patientNameService.getPatientName(triage.createdBy.firstName, triage.createdBy.nameSelfDetermination)}, ${triage.createdBy.lastName}`;
  }

  private init(): void {
    this.emergencyCareEpisodeStateService.getState(this.episodeId).subscribe(
      state => {
        this.episodeState = state.id;

        if (this.isActive(this.episodeState)) {
          this.loadEpisode();
        } else {
          this.snackBarService.showError('guardia.episode.NOT_ACTIVE');
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

  private loadEpisode(): void {
    this.emergencyCareEpisodeService.getAdministrative(this.episodeId)
      .subscribe((responseEmergencyCare: ResponseEmergencyCareDto) => {
        this.responseEmergencyCare = responseEmergencyCare;
        this.emergencyCareType = responseEmergencyCare.emergencyCareType?.id;
        this.doctorsOfficeDescription = responseEmergencyCare.doctorsOffice?.description;
      });

    this.loadTriages();
  }

  private loadTriages(): void {
    this.triageService.getAll(this.episodeId).subscribe((triages: TriageListDto[]) => {
      this.lastTriage = this.guardiaMapperService.triageListDtoToTriage(triages[0]);
      if (hasHistory(triages)) {
        this.triagesHistory = triages.map(this.guardiaMapperService.triageListDtoToTriageReduced);
        this.triagesHistory.shift();
      }
    });

    function hasHistory(triages: TriageListDto[]) {
      return triages?.length > 1;
    }
  }

  private goToEmergencyCareHome(): void {
    this.router.navigateByUrl(this.routePrefix + '/guardia');
  }

}

export interface TriageReduced {
  creationDate: Date;
  category: TriageCategory;
  createdBy: {
    firstName: string,
    lastName: string,
    nameSelfDetermination: string
  };
  doctorsOfficeDescription: string;
}
