import { Inject, Component, OnInit } from '@angular/core';
import { PhysicalPerformanceService } from '@api-rest/services/physical-performance.service';
import { MAT_DIALOG_DATA, MatDialog } from '@angular/material/dialog';
import { QuestionnairesResponses } from '@api-rest/api-model';
import { AlertDialogComponent } from '../../alert-dialog/alert-dialog.component';
 
@Component({
  selector: 'app-get-physical-performance',
  templateUrl: './get-physical-performance.component.html',
  styleUrls: ['./get-physical-performance.component.scss']
})

export class GetPhysicalPerformanceComponent implements OnInit {  
  submitted: boolean;
  patientId: number;
  physicalQuestionnaires: QuestionnairesResponses[] = [];
  lastPhysicalQuestionnaireId: number | null = null;

  constructor(
    private PhysicalPerformanceService: PhysicalPerformanceService,
    @Inject(MAT_DIALOG_DATA) public data: any,
    private dialog: MatDialog
  ) {

    this.patientId = data.patientId

  }

  ngOnInit(): void {
    this.PhysicalPerformanceService.getAllByPatientId(this.patientId).subscribe(
      (questionnaires: QuestionnairesResponses[]) => {
        this.physicalQuestionnaires = questionnaires;
        this.getLastPhysicalQuestionnaireId();
      },
      (error) => {
        console.error('Error fetching frail questionnaires:', error);
      }
    );
  }

  getLastPhysicalQuestionnaireId(): void {
    // Filter "edmonton" questionnaires
    const physicalQuestionnaires = this.physicalQuestionnaires.filter(
      (questionnaire) => questionnaire.questionnaireTypeId === 4
    );

    // Sort by creation date
    physicalQuestionnaires.sort((a, b) => {
      return new Date(b.createdOn).getTime() - new Date(a.createdOn).getTime();
    });


    // Get the latest ID
    if (physicalQuestionnaires.length > 0) {
      this.lastPhysicalQuestionnaireId = physicalQuestionnaires[0].id;
    } else {
      console.warn('No frail questionnaires found for this patient')
      this.showAlert('No hay cuestionarios disponibles para este paciente.')
    }
  }

  downloadLastPhysicalPdf(): void {
    this.submitted = true;

    if (this.lastPhysicalQuestionnaireId !== null) {
      this.PhysicalPerformanceService.physicalPerformancePdf(this.lastPhysicalQuestionnaireId).subscribe(
        (pdfBlob: Blob) => {
          const url = window.URL.createObjectURL(pdfBlob);
          window.open(url);
        },
        (error) => {
          console.error('Error downloading the questionnaire PDF:', error);
          this.showAlert('No hay cuestionarios disponibles para este paciente.')
        }
      );
    } else {
      console.warn('No Frail questionnaires available to download PDF.')
      this.showAlert('No hay cuestionarios disponibles para este paciente.')
    }
  }

  showAlert(message: string): void {
    this.dialog.open(AlertDialogComponent, {
      data: { message: message }
    });
  }

}
