import { Component, NgModule, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { ActivatedRoute } from '@angular/router';


@Component({
  selector: 'app-test-edmonton-popup',
  templateUrl: './test-edmonton-popup.component.html',
  styleUrls: ['./test-edmonton-popup.component.scss']
})

export class TestEdmontonPopupComponent {
  selectedOption: string | undefined;
  patientId: number;

  
  constructor(
    private dialogRef: MatDialogRef<TestEdmontonPopupComponent>,
    private readonly route: ActivatedRoute,
    @Inject(MAT_DIALOG_DATA) private data: any
    ) {
      this.patientId = data.patientId;
    } 

  trae(){
    const institutionId = 2
      this.route.paramMap.subscribe(
        (params) => {
          this.patientId = Number(params.get('idPaciente')); 
          console.log("paciente en trae", this.patientId)
      })
  }

  cerrarPopup() {
    if (this.dialogRef) {
      this.dialogRef.close();
    }
  }

}
