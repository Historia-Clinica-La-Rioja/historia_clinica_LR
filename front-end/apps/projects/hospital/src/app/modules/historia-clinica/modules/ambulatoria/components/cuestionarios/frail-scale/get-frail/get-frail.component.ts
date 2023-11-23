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
      )

      console.log(this.generatePdf, "descarga exitosa")
        }
      }
}
