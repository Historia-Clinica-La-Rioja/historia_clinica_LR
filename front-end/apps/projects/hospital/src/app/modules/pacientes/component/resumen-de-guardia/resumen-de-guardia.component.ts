import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { ResponseEmergencyCareDto, TriageListDto } from '@api-rest/api-model';
import { EmergencyCareEpisodeService } from '@api-rest/services/emergency-care-episode.service';
import { TriageService } from '@api-rest/services/triage.service';
import { PatientNameService } from '@core/services/patient-name.service';
import { TriageCategory } from '@historia-clinica/modules/guardia/components/triage-chip/triage-chip.component';
import { GuardiaMapperService } from '@historia-clinica/modules/guardia/services/guardia-mapper.service';
import { TriageDefinitionsService } from '@historia-clinica/modules/guardia/services/triage-definitions.service';
import { GUARDIA } from '@historia-clinica/constants/summaries';
import { SummaryHeader } from '@presentation/components/summary-card/summary-card.component';
import { RiskFactorFull, Triage } from '@historia-clinica/modules/guardia/components/triage-details/triage-details.component';
import { EmergencyCareTypes } from '@historia-clinica/modules/guardia/constants/masterdata';
import { MatDialog } from '@angular/material/dialog';

@Component({
  selector: 'app-resumen-de-guardia',
  templateUrl: './resumen-de-guardia.component.html',
  styleUrls: ['./resumen-de-guardia.component.scss']
})
export class ResumenDeGuardiaComponent implements OnInit {

  @Input() episodeId: number;
  @Input() withoutMedicalDischarge: boolean;

  @Output() triageRiskFactors = new EventEmitter<RiskFactorFull[]>();
  guardiaSummary: SummaryHeader = GUARDIA;

  responseEmergencyCare: ResponseEmergencyCareDto;

  triagesHistory: TriageReduced[];
  fullNamesHistoryTriage: string[];
  lastTriage: Triage;

  emergencyCareType: EmergencyCareTypes;
  doctorsOfficeDescription: string;

  constructor(
    private readonly emergencyCareEpisodeService: EmergencyCareEpisodeService,
    private readonly triageService: TriageService,
    private readonly guardiaMapperService: GuardiaMapperService,
    private readonly patientNameService: PatientNameService,
    private readonly triageDefinitionsService: TriageDefinitionsService,
    private readonly dialog: MatDialog,
  ) {

  }

  ngOnInit(): void {
    this.loadEpisode();
  }

  newTriage() {
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

  private loadFullNames() {
    this.fullNamesHistoryTriage = [];
    this.triagesHistory.forEach(
      (triage: TriageReduced) => {
        this.fullNamesHistoryTriage.push(this.getFullName(triage));
      }
    );
  }

  private getFullName(triage: TriageReduced): string {
    return `${this.patientNameService.getPatientName(triage.createdBy.firstName, triage.createdBy.nameSelfDetermination)}, ${triage.createdBy.lastName}`;
  }

  private loadEpisode() {
    this.emergencyCareEpisodeService.getAdministrative(this.episodeId)
      .subscribe((responseEmergencyCare: ResponseEmergencyCareDto) => {
        this.responseEmergencyCare = responseEmergencyCare;
        this.emergencyCareType = responseEmergencyCare.emergencyCareType?.id;
        this.doctorsOfficeDescription = responseEmergencyCare.doctorsOffice?.description;
      });

    this.loadTriages();
  }

  private loadTriages() {
    this.triageService.getAll(this.episodeId).subscribe((triages: TriageListDto[]) => {
      this.lastTriage = this.guardiaMapperService.triageListDtoToTriage(triages[0]);
      if (hasHistory(triages)) {
        this.triagesHistory = triages.map(this.guardiaMapperService.triageListDtoToTriageReduced);
        this.triagesHistory.shift();
        this.loadFullNames();
      }
    });

    function hasHistory(triages: TriageListDto[]) {
      return triages?.length > 1;
    }
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
