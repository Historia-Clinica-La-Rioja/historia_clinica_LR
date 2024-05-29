import { Component, Inject, OnInit } from '@angular/core';
import { EdmontonService } from '@api-rest/services/edmonton.service';
import { MAT_DIALOG_DATA, MatDialog } from '@angular/material/dialog';
import { QuestionnairesResponses } from '@api-rest/api-model';
import { AlertDialogComponent } from '../../alert-dialog/alert-dialog.component';

@Component({
  selector: 'app-get-edmonton',
  templateUrl: './get-edmonton.component.html',
  styleUrls: ['./get-edmonton.component.scss']
})
export class GetEdmontonComponent implements OnInit {

  submitted: boolean;
  patientId: number;
  edmontonQuestionnaires: QuestionnairesResponses[] = [];
  lastEdmontonQuestionnaireId: number | null = null;

  constructor(
    private EdmontonService: EdmontonService,
    @Inject(MAT_DIALOG_DATA) public data: any,
    private dialog: MatDialog
  ) {

    this.patientId = data.patientId

  }

  ngOnInit(): void {
    this.EdmontonService.getAllByPatientId(this.patientId).subscribe(
      (questionnaires: QuestionnairesResponses[]) => {
        this.edmontonQuestionnaires = questionnaires;
        this.getLastEdmontonQuestionnaireId();
      },
      (error) => {
        console.error('Error fetching frail questionnaires:', error);
      }
    );
  }

  getLastEdmontonQuestionnaireId(): void {
    // Filter "edmonton" questionnaires
    const edmontonQuestionnaires = this.edmontonQuestionnaires.filter(
      (questionnaire) => questionnaire.questionnaireTypeId === 1
    );

    // Sort by creation date
    edmontonQuestionnaires.sort((a, b) => {
      return new Date(b.createdOn).getTime() - new Date(a.createdOn).getTime();
    });

    // Get the latest ID
    if (edmontonQuestionnaires.length > 0) {
      this.lastEdmontonQuestionnaireId = edmontonQuestionnaires[0].id;
    } else {
      console.warn('No frail questionnaires found for this patient')
      this.showAlert('No hay cuestionarios disponibles para este paciente.')
    }
  }

  downloadLastEdmontonPdf(): void {
    this.submitted = true;

    if (this.lastEdmontonQuestionnaireId !== null) {
      this.EdmontonService.edmontonGetPdf(this.lastEdmontonQuestionnaireId).subscribe(
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
