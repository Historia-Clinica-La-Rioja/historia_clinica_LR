import { Component, OnInit } from '@angular/core';
import { MatDialogRef } from '@angular/material/dialog';

@Component({
  selector: 'app-derive-report',
  templateUrl: './derive-report.component.html',
  styleUrls: ['./derive-report.component.scss']
})
export class DeriveReportComponent implements OnInit {

  constructor(public dialogRef: MatDialogRef<DeriveReportComponent>) { }

  ngOnInit(): void {
  }

  closeDialog() {
    this.dialogRef.close()
  }

}
