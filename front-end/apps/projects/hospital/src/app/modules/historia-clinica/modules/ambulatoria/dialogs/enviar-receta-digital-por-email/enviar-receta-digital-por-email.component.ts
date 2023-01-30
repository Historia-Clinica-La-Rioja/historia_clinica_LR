import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { hasError } from '@core/utils/form.utils';

@Component({
  selector: 'app-enviar-receta-digital-por-email',
  templateUrl: './enviar-receta-digital-por-email.component.html',
  styleUrls: ['./enviar-receta-digital-por-email.component.scss']
})
export class EnviarRecetaDigitalPorEmailComponent implements OnInit {

  emailForm: FormGroup;
  hasError = hasError;

  constructor(private readonly formBuilder: FormBuilder) { }

  ngOnInit(): void {
    this.emailForm = this.formBuilder.group({
			email: [null, [Validators.required, Validators.email]],
		});
  }

}
