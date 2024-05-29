import { Component, OnInit } from '@angular/core';
import { EstudiosPopupComponent } from '../pop-up/estudios-popup.component';
import { MatDialog } from '@angular/material/dialog';
import { BasicPatientDto, QuestionnairesResponses } from '@api-rest/api-model';
import { ContextService } from '@core/services/context.service';
import { ActivatedRoute } from '@angular/router';
import { LatestStudiesComponent } from '../pop-up/latest-studies/latest-studies.component';
import { EdmontonService } from '@api-rest/services/edmonton.service';
import { FrailService } from '@api-rest/services/fragility-test.service';

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
  lastThreeFrailQuestionnaires: QuestionnairesResponses[] = [];  

  constructor(
    private dialog: MatDialog,
    private readonly contextService: ContextService,
    private readonly route: ActivatedRoute,
    private edmontonService: EdmontonService, 
    private frailService: FrailService  
  ) {
    this.routePrefix = `${this.contextService.institutionId}`;
    this.route.paramMap.subscribe(params => {
      this.patientId = Number(params.get('idPaciente'));
    });
  }

  ngOnInit(): void {
    this.loadLastEdmontonQuestionnaires();
    this.loadLastThreeFrailQuestionnaires();
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
      },
      (error) => {
        console.error('Error fetching Edmonton questionnaires:', error);
      }
    );
  }

  loadLastThreeFrailQuestionnaires(): void {
    this.frailService.getAllByPatientId(this.patientId).subscribe(
      (questionnaires: QuestionnairesResponses[]) => {
        const frailQuestionnaires = questionnaires.filter(
          (questionnaire) => questionnaire.questionnaireTypeId === 3
        );

        frailQuestionnaires.sort((a, b) => {
          return new Date(b.createdOn).getTime() - new Date(a.createdOn).getTime();
        });

        this.lastThreeFrailQuestionnaires = frailQuestionnaires.slice(0, 3);
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
