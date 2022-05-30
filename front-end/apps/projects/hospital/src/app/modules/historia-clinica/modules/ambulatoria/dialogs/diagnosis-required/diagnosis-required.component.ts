import { Component } from '@angular/core';
import { MatDialogRef } from "@angular/material/dialog";

@Component({
  selector: 'app-diagnosis-required',
  templateUrl: './diagnosis-required.component.html',
  styleUrls: ['./diagnosis-required.component.scss']
})
export class DiagnosisRequiredComponent {

  constructor(
	  public dialogRef: MatDialogRef<DiagnosisRequiredComponent>
  ) { }

  closeModal() {
  	this.dialogRef.close();
  }

}
