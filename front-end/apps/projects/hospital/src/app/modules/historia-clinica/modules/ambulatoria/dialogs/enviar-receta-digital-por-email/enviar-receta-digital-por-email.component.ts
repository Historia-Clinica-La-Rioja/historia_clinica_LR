import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MedicationRequestService } from '@api-rest/services/medication-request.service';
import { hasError } from '@core/utils/form.utils';
import { SnackBarService } from '@presentation/services/snack-bar.service';

@Component({
  selector: 'app-enviar-receta-digital-por-email',
  templateUrl: './enviar-receta-digital-por-email.component.html',
  styleUrls: ['./enviar-receta-digital-por-email.component.scss']
})
export class EnviarRecetaDigitalPorEmailComponent implements OnInit {

  emailForm: FormGroup;
  hasError = hasError;

  constructor(private readonly formBuilder: FormBuilder,
              private readonly snackBarService: SnackBarService,
              private readonly medicationRequestService: MedicationRequestService,
              private dialogRef: MatDialogRef<EnviarRecetaDigitalPorEmailComponent>,
              @Inject(MAT_DIALOG_DATA) public data?) { }

  ngOnInit(): void {
    this.emailForm = this.formBuilder.group({
			email: [this.data?.patientEmail, [Validators.required, Validators.email]],
		});
  }

  sendEmail() {
    this.medicationRequestService.sendEmail(this.data.patientId, this.emailForm.get('email').value, this.data.prescriptionRequest)
      .subscribe(_ => {
        this.snackBarService.showSuccess('Email enviado correctamente')
        this.dialogRef.close();
      });
  }

}
