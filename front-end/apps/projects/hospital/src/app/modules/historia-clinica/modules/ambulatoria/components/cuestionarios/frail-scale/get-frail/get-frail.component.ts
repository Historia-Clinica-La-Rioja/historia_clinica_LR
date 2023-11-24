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
  patientId: any;
  consultation: number;

  constructor(
    private frailService: FrailService,
    @Inject(MAT_DIALOG_DATA) public data: any,
  ) {

    this.patientId = data.patientId

  }

  ngOnInit(): void {
  }

  generatePdf() {
    this.submitted = true;

    if (this.patientId) {
      this.frailService.getPdf(this.patientId).subscribe(
        (response) => {
          const blob = new Blob([response], { type: 'application/pdf' });

          this.patientId = this.data.patientId
          const fileName = `EscaladeFrail_${this.patientId}.pdf`;

          const url = window.URL.createObjectURL(blob);
          const a = document.createElement('a');
          a.href = url;
          a.download = fileName;

          document.body.appendChild(a);
          a.click();
          document.body.removeChild(a);

          window.URL.revokeObjectURL(url);

          // console.log("Descarga exitosa");
        },
        (error) => {
          //  console.error("Error en la descarga", error);
        }
      );
    }
  }
}  