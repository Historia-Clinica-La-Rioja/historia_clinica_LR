import { Component, Inject, OnInit } from '@angular/core';
import { FrailService } from '@api-rest/services/frail.service';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';

@Component({
  selector: 'app-get-frail',
  templateUrl: './get-frail.component.html',
  styleUrls: ['./get-frail.component.scss'],

})

export class GetFrailComponent implements OnInit {
  submitted: boolean;
  patientId: number;
  patientName: string;
  consultation: number;
  questionnaireResponseId: number;

  constructor(
    private frailService: FrailService,
    @Inject(MAT_DIALOG_DATA) public data: any,
  ) {
    // console.log('Data recibida:', data);
    this.patientId = data.patientId
  }

  ngOnInit(): void {
  }

  downloadPdf(): void {
    this.frailService.getAllByPatientId(this.patientId).subscribe(
      (allQuestionnaires) => {
        if (allQuestionnaires && allQuestionnaires.length > 0) {
          const latestQuestionnaire = allQuestionnaires.reduce((prev, current) => {
            return (new Date(current.createdOn) > new Date(prev.createdOn)) ? current : prev;
          });

          // const latestQuestionnaireDate = latestQuestionnaire.createdOn;

          // console.log("Fecha más reciente:", latestQuestionnaireDate);

          this.generatePdf(latestQuestionnaire.id);
        }
      },
      (error) => {
        console.error("Error: ", error)
      }
    );
  }

  generatePdf(frailQuestionnaireId: number): void {

    this.frailService.getPdf(frailQuestionnaireId).subscribe(
      (pdfBlob) => {
        const blob = new Blob([pdfBlob], { type: 'application/pdf' });

        // console.log("Trae okk: ", frailQuestionnaireId);
        const fileName = `EscaladeFrail_${this.patientId}.pdf`;
        const url = window.URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = fileName;

        document.body.appendChild(a);
        a.click();
        document.body.removeChild(a);

        window.URL.revokeObjectURL(url);
      },
      (error) => {
        console.error('Error al descargar PDF:', error);
      }
    );
  }

}  