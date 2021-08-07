import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';

@Component({
  selector: 'app-succes-message-dialog',
  templateUrl: './succes-message-dialog.component.html',
  styleUrls: ['./succes-message-dialog.component.scss']
})
export class SuccesMessageDialogComponent implements OnInit {

  successMessage: string;
  constructor(
    public dialogRef: MatDialogRef<SuccesMessageDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: {
      message: string
    }
  ) { }

  ngOnInit(): void {
    this.successMessage = this.data.message ? this.data.message : '';
  }

}
