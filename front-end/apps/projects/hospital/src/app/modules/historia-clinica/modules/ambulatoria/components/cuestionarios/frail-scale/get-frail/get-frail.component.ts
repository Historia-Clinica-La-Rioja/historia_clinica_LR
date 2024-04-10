import { Component, Inject, OnInit } from '@angular/core';
import { FrailService } from '@api-rest/services/fragility-test.service';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';

@Component({
  selector: 'app-get-frail',
  templateUrl: './get-frail.component.html',
  styleUrls: ['./get-frail.component.scss'],

})

export class GetFrailComponent implements OnInit {
  submitted: boolean;
  patientId: any;
  consultation: number;
  questionnaireId: number;

  constructor(
    private frailService: FrailService,
    @Inject(MAT_DIALOG_DATA) public data: any,
  ) {

    this.questionnaireId = data.patientId

  }

  ngOnInit(): void {
  }

  generatePdf() {
    this.submitted = true;

    if (this.questionnaireId) {
      this.frailService.getPdf(this.questionnaireId).subscribe(
        (response) => {
          const blob = new Blob([response], { type: 'application/pdf' });
          console.log(this.questionnaireId, "cuestionario")

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
          
        }
      );
    }
  }
}  