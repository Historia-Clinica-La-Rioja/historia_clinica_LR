import { Component, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { ProfessionalLicenseNumberValidationResponseDto } from '@api-rest/api-model';

@Component({
  selector: 'app-prescripcion-validator-popup',
  templateUrl: './prescripcion-validator-popup.component.html',
  styleUrls: ['./prescripcion-validator-popup.component.scss']
})
export class PrescripcionValidatorPopupComponent {

  constructor(
    @Inject(MAT_DIALOG_DATA) public data: ProfessionalLicenseNumberValidationResponseDto,
    public dialogRef: MatDialogRef<PrescripcionValidatorPopupComponent>,
    private router: Router
  ) { }

  toAccount() {
    this.router.navigate(['home/profile']);
    this.dialogRef.close();
  }

}
