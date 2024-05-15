import { Component, Inject, OnInit } from '@angular/core';
import { EdmontonService } from '@api-rest/services/edmonton.service';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import { QuestionnairesResponses } from '@api-rest/api-model';

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
    console.log("TRAEEE: ", this.edmontonQuestionnaires);
    // Filter "edmonton" questionnaires
    const edmontonQuestionnaires = this.edmontonQuestionnaires.filter(
      (questionnaire) => questionnaire.questionnaireTypeId === 1
    );

    console.log("Trae? Okk?", edmontonQuestionnaires);

    // Sort by creation date
    edmontonQuestionnaires.sort((a, b) => {
      return new Date(b.createdOn).getTime() - new Date(a.createdOn).getTime();
    });

    console.log("Trae ordenados? Okk?", edmontonQuestionnaires);

    // Get the latest ID
    if (edmontonQuestionnaires.length > 0) {
      this.lastEdmontonQuestionnaireId = edmontonQuestionnaires[0].id;
      console.log("Trae ID? Okk? ", this.lastEdmontonQuestionnaireId);
    } else {
      console.warn('No frail questionnaires found for this patient')
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
        }
      );
    } else {
      console.warn('No Frail questionnaires available to download PDF.')
    }
  }
}
