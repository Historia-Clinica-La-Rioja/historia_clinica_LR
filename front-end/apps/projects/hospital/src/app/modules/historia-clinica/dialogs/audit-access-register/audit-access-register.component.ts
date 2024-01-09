import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { IdentificationTypeDto } from '@api-rest/api-model';
import { hasError } from '@core/utils/form.utils';

@Component({
  selector: 'app-audit-access-register',
  templateUrl: './audit-access-register.component.html',
  styleUrls: ['./audit-access-register.component.scss']
})
export class AuditAccessRegisterComponent implements OnInit {

  auditAccessForm: FormGroup<AuditAccessFormModel>;
  hasError = hasError;
  identificationMotiveList:IdentificationTypeDto[] = MOTIVES ;

  constructor(
    @Inject(MAT_DIALOG_DATA) public data: {scopeRequest: number},
    private readonly formBuilder: FormBuilder,
    private dialogRef: MatDialogRef<AuditAccessRegisterComponent>,
  ) { }

  ngOnInit(): void {
    this.auditAccessForm = this.formBuilder.group<AuditAccessFormModel>({
      motive: new FormControl (null, Validators.required),
      observations:  new FormControl (null, Validators.required)
  });
  }

  goToHC(): void {
    this.dialogRef.close(true)
  }

  cancel(): void {
    this.dialogRef.close(false)
  }

  isFormValid() {
    return this.auditAccessForm.valid
  }

}

export interface AuditAccessFormModel {
  motive:  FormControl<string>,
  observations:  FormControl<string>
}

export const MOTIVES: IdentificationTypeDto[] = [
  {
    id: 1,
    description: 'Urgencia médica'
  },
  {
    id: 2,
    description: 'Consulta profesional'
  },
  {
    id: 3,
    description: 'Consulta de paciente'
  },
  {
    id: 4,
    description: 'Auditoría'
  },
]
