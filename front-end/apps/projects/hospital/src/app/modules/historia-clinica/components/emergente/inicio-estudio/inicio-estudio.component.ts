import { Component, OnInit } from '@angular/core';
import { EstudiosPopupComponent } from '../pop-up/estudios-popup.component';
import { MatDialog } from '@angular/material/dialog';
import { BasicPatientDto, QuestionnairesResponses } from '@api-rest/api-model';
import { ContextService } from '@core/services/context.service';
import { ActivatedRoute } from '@angular/router';
import { LatestStudiesComponent } from '../pop-up/latest-studies/latest-studies.component';
import { EdmontonService } from '@api-rest/services/edmonton.service';
import { FrailService } from '@api-rest/services/fragility-test.service';
import { PhysicalPerformanceService } from '@api-rest/services/physical-performance.service';


@Component({
  selector: 'app-adulto-mayor',
  templateUrl: './inicio-estudio.component.html',
  styleUrls: ['./inicio-estudio.component.scss'],
})
export class AdultoMayorComponent implements OnInit {

  patientId: number;
  private readonly routePrefix;
  patientData: BasicPatientDto | undefined;
  lastEdmontonQuestionnaires: QuestionnairesResponses[] = [];
  lastFrailQuestionnaires: QuestionnairesResponses[] = [];
  lastPhysicalQuestionnaires: QuestionnairesResponses[] = [];

  constructor(
    private dialog: MatDialog,
    private readonly contextService: ContextService,
    private readonly route: ActivatedRoute,
    private edmontonService: EdmontonService,
    private frailService: FrailService,
    private PhysicalPerformanceService: PhysicalPerformanceService
  ) {
    this.routePrefix = `${this.contextService.institutionId}`;
    this.route.paramMap.subscribe(params => {
      this.patientId = Number(params.get('idPaciente'));

    });
  }

  ngOnInit(): void {
    this.loadLastEdmontonQuestionnaires();
    this.loadLastFrailQuestionnaires();
    this.loadLastPhysicalQuestionnaires();
  }

  loadLastEdmontonQuestionnaires(): void {
    this.edmontonService.getAllByPatientId(this.patientId).subscribe(
      (questionnaires: QuestionnairesResponses[]) => {
        const edmontonQuestionnaires = questionnaires.filter(
          (questionnaire) => questionnaire.questionnaireTypeId === 1
        );

        edmontonQuestionnaires.sort((a, b) => {
          return new Date(b.createdOn).getTime() - new Date(a.createdOn).getTime();
        });

        this.lastEdmontonQuestionnaires = edmontonQuestionnaires.slice(0, 3);
        this.lastEdmontonQuestionnaires.forEach(q => {
          if (q.questionnaireResult === "0 - 4 : Individuo sano") {
            q['questionnaireResult'] = "Individuo Sano"
          } else if (q.questionnaireResult === "5 - 6 : Vulnerable") {
            q['questionnaireResult'] = "Vulnerable"
          } else if (q.questionnaireResult === "7 - 8 : Fragilidad leve") {
            q['questionnaireResult'] = "Fragilidad leve"
          } else if (q.questionnaireResult === "9 - 10 : Fragilidad moderada") {
            q['questionnaireResult'] = "Fragilidad moderada"
          } else if (q.questionnaireResult === ">= 11 : Fragilidad severa") {
            q['questionnaireResult'] = "Fragilidad severa"
          }
        })
      },
      (error) => {
        console.error('Error fetching Edmonton questionnaires:', error);
      }
    );
  }

  loadLastFrailQuestionnaires(): void {
    this.frailService.getAllByPatientId(this.patientId).subscribe(
      (questionnaires: QuestionnairesResponses[]) => {
        const frailQuestionnaires = questionnaires.filter(
          (questionnaire) => questionnaire.questionnaireTypeId === 3
        );

        frailQuestionnaires.sort((a, b) => {
          return new Date(b.createdOn).getTime() - new Date(a.createdOn).getTime();
        });

        this.lastFrailQuestionnaires = frailQuestionnaires.slice(0, 3);
        this.lastFrailQuestionnaires.forEach(q => {
          if (q.questionnaireResult === "3-5: Persona frágil") {
            q['questionnaireResult'] = "Persona frágil"
          } else if (q.questionnaireResult === "0-2: Persona pre-frágil") {
            q['questionnaireResult'] = "Persona pre-frágil"
          }
        });

      },
      (error) => {
        console.error('Error fetching Frail questionnaires:', error);
      }
    );
  }

  loadLastPhysicalQuestionnaires(): void {
    this.PhysicalPerformanceService.getAllByPatientId(this.patientId).subscribe(
      (questionnaires: QuestionnairesResponses[]) => {
        const physicalQuestionnaires = questionnaires.filter(
          (questionnaire) => questionnaire.questionnaireTypeId === 4
        );

        physicalQuestionnaires.sort((a, b) => {
          return new Date(b.createdOn).getTime() - new Date(a.createdOn).getTime();
        });

        this.lastPhysicalQuestionnaires = physicalQuestionnaires.slice(0, 3);
        // this.lastPhysicalQuestionnaires.forEach(q => {
        //   if(q.questionnaireResult === "Desempeño fisico bajo") {
        //   } else (q.questionnaireResult === "Desempeño fisico alto")
        // })
      },
      (error) => {
        console.error('Error fetching Frail questionnaires:', error);
      }
    );
  }

  downloadEdmontonPdf(questionnaireId: number): void {
    this.edmontonService.edmontonGetPdf(questionnaireId).subscribe(
      (pdfBlob: Blob) => {
        const url = window.URL.createObjectURL(pdfBlob);
        window.open(url);
      },
      (error) => {
        console.error('Error downloading the questionnaire PDF:', error);
      }
    );
  }

  downloadFrailPdf(questionnaireId: number): void {
    this.frailService.getPdf(questionnaireId).subscribe(
      (pdfBlob: Blob) => {
        const url = window.URL.createObjectURL(pdfBlob);
        window.open(url);
      },
      (error) => {
        console.error('Error downloading the questionnaire PDF:', error);
      }
    );
  }

  downloadPhysicalPdf(questionnaireId: number): void {
    this.PhysicalPerformanceService.physicalPerformancePdf(questionnaireId).subscribe(
      (pdfBlob: Blob) => {
        const url = window.URL.createObjectURL(pdfBlob);
        window.open(url);
      },
      (error) => {
        console.error('Error downloading the questionnaire PDF:', error);
      }
    );
  }
  
  mostrarPopup(): void {
    const dialogRef = this.dialog.open(EstudiosPopupComponent, {
      width: '800px',
      data: {
        patientId: this.patientId,
      },
    });

    dialogRef.afterClosed().subscribe(result => {
    });
  }

  mostrarPopupVerEstudios(): void {
    const dialogRef = this.dialog.open(LatestStudiesComponent, {
      width: '800px',
      data: {
        patientId: this.patientId,
        institutionId: this.routePrefix,
      }
    });
    dialogRef.afterClosed().subscribe(result => {
    });
  }
}
