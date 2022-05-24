import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from "@angular/material/dialog";

@Component({
  selector: 'app-operation-denied',
  templateUrl: './operation-denied.component.html',
  styleUrls: ['./operation-denied.component.scss']
})
export class OperationDeniedComponent {

  constructor(
	  public dialogRef: MatDialogRef<OperationDeniedComponent>,
	  @Inject(MAT_DIALOG_DATA) public data: { message: string }
  ) { }

  closeModal() {
  	this.dialogRef.close();
  }

}
