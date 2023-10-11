import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { VirtualConsultationDto } from '@api-rest/api-model';

@Component({
  selector: 'app-transfer-request',
  templateUrl: './transfer-request.component.html',
  styleUrls: ['./transfer-request.component.scss']
})
export class TransferRequestComponent implements OnInit {

  constructor(public dialogRef: MatDialogRef<TransferRequestComponent>, @Inject(MAT_DIALOG_DATA) public data: {
    virtualConsultation: VirtualConsultationDto
  }) { }

  ngOnInit(): void {
  }

}
