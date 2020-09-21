import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { AuthenticationService } from '../../services/authentication.service';
import { catchError } from 'rxjs/operators';
import { ApiErrorMessageDto } from '@api-rest/api-model';
import { RecaptchaService } from "@api-rest/services/recaptcha.service";

@Component({
	selector: 'app-login',
	templateUrl: './login.component.html',
	styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {
	public apiError: ApiErrorMessageDto = null;
	public form: FormGroup;
	public recaptchaRes: string;
	public recaptchaEnable: boolean = false;

	constructor(
		private formBuilder: FormBuilder,
		private authenticationService: AuthenticationService,
		private recaptchaService: RecaptchaService) { }

	ngOnInit(): void {

		this.form = this.formBuilder.group({
			username: [null, Validators.required],
			password: [null, Validators.required],
			recaptchaReactive: []
		});

		this.recaptchaService.isRecaptchaEnable().subscribe(data => {
			if(data) {
				this.recaptchaEnable = data;
				this.form.controls.recaptchaReactive.setValidators(Validators.required);
			}
		});

	}

	hasError(type: string, control: string): boolean {
		return this.form.get(control).hasError(type);
	}


	submit() {
		if (this.form.valid) {
			this.form.disable();

			this.authenticationService.login(
				this.form.value.username,
				this.form.value.password,
			).pipe(
				catchError((err: ApiErrorMessageDto) => {
					this.apiError = err;
					this.form.enable();
					throw err;
				}),
			).subscribe(
				() => this.authenticationService.goHome()
			);
		}
	}

	reCaptchaResolved(captchaResponse: string) {
		this.recaptchaRes = captchaResponse;
	}

}
