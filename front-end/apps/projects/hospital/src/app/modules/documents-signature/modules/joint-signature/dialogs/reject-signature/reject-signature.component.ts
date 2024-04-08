import { Component } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { MatDialogRef } from '@angular/material/dialog';
import { ERejectDocumentElectronicJointSignatureReason, RejectDocumentElectronicJointSignatureDto } from '@api-rest/api-model';

@Component({
  selector: 'app-reject-signature',
  templateUrl: './reject-signature.component.html',
  styleUrls: ['./reject-signature.component.scss']
})
export class RejectSignatureComponent {
  motives = OptionsReject;
  rejectForm = this.formBuilder.group({
    motive: [null, Validators.required],
    observations: [null, Validators.required]
  })
  constructor(private dialogRef: MatDialogRef<RejectSignatureComponent>,
    private formBuilder: FormBuilder) { }

  emitReasonRejection() {
    if (this.rejectForm.valid) {
      const reject: RejectDocumentElectronicJointSignatureDto = {
        documentIds: [],
        description: this.rejectForm.value.observations,
        rejectReason: this.rejectForm.value.motive,
      }
      this.dialogRef.close(reject);
    } else {
      this.rejectForm.markAllAsTouched();
    }
  }
}

const OptionsReject = [
  {
    id: ERejectDocumentElectronicJointSignatureReason.WRONG_PROFESSIONAL,
    text: 'firma-conjunta.REJECT_SIGNATURE.form.motive-options.WRONG_PROFESSIONAL'
  },
  {
    id: ERejectDocumentElectronicJointSignatureReason.OTHER,
    text: 'firma-conjunta.REJECT_SIGNATURE.form.motive-options.OTHER'
  }
]