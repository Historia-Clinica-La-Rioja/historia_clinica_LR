import { Component, Inject, OnInit } from '@angular/core';
import { FrailService } from '@api-rest/services/fragility-test.service';
import { MAT_DIALOG_DATA, MatDialog } from '@angular/material/dialog';
import { QuestionnairesResponses } from '@api-rest/api-model';
import { AlertDialogComponent } from '../../alert-dialog/alert-dialog.component';

@Component({
  selector: 'app-get-frail',
  templateUrl: './get-frail.component.html',
  styleUrls: ['./get-frail.component.scss'],

})

export class GetFrailComponent implements OnInit {
  submitted: boolean;
  patientId: number;
  frailQuestionnaires: QuestionnairesResponses[] = [];
  lastFrailQuestionnaireId: number | null = null;

  constructor(
    private frailService: FrailService,
    @Inject(MAT_DIALOG_DATA) public data: any,
    private dialog: MatDialog
  ) {

    this.patientId = data.patientId

  }

  ngOnInit(): void {
    this.frailService.getAllByPatientId(this.patientId).subscribe(
      (questionnaires: QuestionnairesResponses[]) => {
        this.frailQuestionnaires = questionnaires;
        this.getLastFrailQuestionnaireId();
      },
      (error) => {
        console.error('Error fetching frail questionnaires:', error);
      }
    );
  }

  getLastFrailQuestionnaireId(): void {
    // Filter "frail" questionnaires
    const frailQuestionnaires = this.frailQuestionnaires.filter(
      (questionnaire) => questionnaire.questionnaireTypeId === 3
    );

    // Sort by creation date
    frailQuestionnaires.sort((a, b) => {
      return new Date(b.createdOn).getTime() - new Date(a.createdOn).getTime();
    });

    // Get the latest ID
    if (frailQuestionnaires.length > 0) {
      this.lastFrailQuestionnaireId = frailQuestionnaires[0].id;
    } else {
      console.warn('No frail questionnaires found for this patient')
      this.showAlert('No hay cuestionarios disponibles para este paciente.')
    }
  }

  downloadLastFrailPdf(): void {
    this.submitted = true;

    if (this.lastFrailQuestionnaireId !== null) {
      this.frailService.getPdf(this.lastFrailQuestionnaireId).subscribe(
        (pdfBlob: Blob) => {
          const url = window.URL.createObjectURL(pdfBlob);
          window.open(url);
        },
        (error) => {
          console.error('Error downloading the questionnaire PDF:', error);
          this.showAlert('Error al descargar el PDF del cuestionario.');
        }
      );
    } else {
      console.warn('No Frail questionnaires available to download PDF.')
      this.showAlert('No hay cuestionarios disponibles para descargar.')
    }
  }

  showAlert(message: string): void {
    this.dialog.open(AlertDialogComponent, {
      data: { message: message },
    });
  }

}