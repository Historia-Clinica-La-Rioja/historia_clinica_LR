import { Component, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { ERole } from '@api-rest/api-model';
import { ContextService } from '@core/services/context.service';
import { PATIENT_TYPE } from '@core/utils/patient.utils';

@Component({
  selector: 'app-patient-validator-popup',
  templateUrl: './patient-validator-popup.component.html',
  styleUrls: ['./patient-validator-popup.component.scss']
})
export class PatientValidatorPopupComponent {
  
  isTemporaryPatient: boolean = false;
  URL_PATIENT_EDIT: string = `institucion/${this.contextService.institutionId}/pacientes/edit`; 
  PRESCRIPTOR: ERole = ERole.PRESCRIPTOR;

  constructor(@Inject(MAT_DIALOG_DATA) public data,
              private readonly router: Router,
              private readonly dialogRef: MatDialogRef<PatientValidatorPopupComponent>,
              private readonly contextService: ContextService) {
    this.isTemporaryPatient = (data.patientType === PATIENT_TYPE.TEMPORARY);
  }

  goToEditPatient() {
    this.router.navigate([this.URL_PATIENT_EDIT], {queryParams: {id: this.data.patientId}});
    this.dialogRef.close();
  }

}
