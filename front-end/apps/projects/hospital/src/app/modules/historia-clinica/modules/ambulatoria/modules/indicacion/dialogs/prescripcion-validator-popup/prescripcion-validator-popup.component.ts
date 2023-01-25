import { Component, OnInit, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { Router } from '@angular/router';

@Component({
  selector: 'app-prescripcion-validator-popup',
  templateUrl: './prescripcion-validator-popup.component.html',
  styleUrls: ['./prescripcion-validator-popup.component.scss']
})
export class PrescripcionValidatorPopupComponent implements OnInit {

  constructor(@Inject(MAT_DIALOG_DATA) public data: any,
              public dialogRef: MatDialogRef<PrescripcionValidatorPopupComponent>,
              private router: Router) {}

  ngOnInit(): void {
  }

  toAccount() {
    this.router.navigate(['home/profile']);
    this.dialogRef.close();
  }

}
