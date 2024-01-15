import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { ClinicHistoryAccessDto, EClinicHistoryAccessReason } from '@api-rest/api-model';
import { ClinicHistoryAccessService } from '@api-rest/services/clinic-history-access.service';
import { hasError } from '@core/utils/form.utils';

@Component({
  selector: 'app-audit-access-register',
  templateUrl: './audit-access-register.component.html',
  styleUrls: ['./audit-access-register.component.scss']
})
export class AuditAccessRegisterComponent implements OnInit {

  auditAccessForm: FormGroup<AuditAccessFormModel>;
  hasError = hasError;
  identificationMotiveList:SelectTypeAuditDto[] = MOTIVES ;

  constructor(
    @Inject(MAT_DIALOG_DATA) public data: {scopeRequest: number, patientId: number , institutionId: number},
    private readonly formBuilder: FormBuilder,
    private readonly dialogRef: MatDialogRef<AuditAccessRegisterComponent>,
    private readonly clinicHistoryAccessService: ClinicHistoryAccessService,
  ) { }

  ngOnInit(): void {
    this.auditAccessForm = this.formBuilder.group<AuditAccessFormModel>({
      motive: new FormControl (null, Validators.required),
      observations:  new FormControl (null, Validators.required)
  });
  }

  saveAuditAccess(): void {
    const clinicHistoryAccessDto: ClinicHistoryAccessDto   = {
      observations: this.auditAccessForm.get('observations').value,
      reason: this.auditAccessForm.get('motive').value ,
      scope: this.data.scopeRequest,
    }
    this.clinicHistoryAccessService
		.saveAudit(this.data.patientId, clinicHistoryAccessDto, this.data.institutionId)
		.subscribe( _ => {
			this.dialogRef.close(true);
		});
  }

  cancel(): void {
    this.dialogRef.close(false)
  }

  isFormValid() {
    return this.auditAccessForm.valid
  }

}

export interface AuditAccessFormModel {
  motive:  FormControl<EClinicHistoryAccessReason>,
  observations:  FormControl<string>
}

export interface SelectTypeAuditDto {
  id: EClinicHistoryAccessReason,
  description: string
}


export const MOTIVES: SelectTypeAuditDto[] = [
  {
    id:  EClinicHistoryAccessReason.MEDICAL_EMERGENCY,
    description: 'Urgencia médica'
  },
  {
    id: EClinicHistoryAccessReason.PROFESSIONAL_CONSULTATION,
    description: 'Consulta profesional'
  },
  {
    id: EClinicHistoryAccessReason.PROFESSIONAL_CONSULTATION,
    description: 'Consulta de paciente'
  },
  {
    id: EClinicHistoryAccessReason.AUDIT,
    description: 'Auditoría'
  },
]
