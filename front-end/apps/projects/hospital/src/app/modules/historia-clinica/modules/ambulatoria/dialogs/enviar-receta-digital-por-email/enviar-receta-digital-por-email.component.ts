import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import { hasError } from '@core/utils/form.utils';

@Component({
  selector: 'app-enviar-receta-digital-por-email',
  templateUrl: './enviar-receta-digital-por-email.component.html',
  styleUrls: ['./enviar-receta-digital-por-email.component.scss']
})
export class EnviarRecetaDigitalPorEmailComponent implements OnInit {

  emailForm: FormGroup;
  hasError = hasError;

  constructor(private readonly formBuilder: FormBuilder,
              @Inject(MAT_DIALOG_DATA) public data?) { }

  ngOnInit(): void {
    this.emailForm = this.formBuilder.group({
			email: [this.data?.patientEmail, [Validators.required, Validators.email]],
		});
  }

}
