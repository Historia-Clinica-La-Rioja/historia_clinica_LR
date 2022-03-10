import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { AuthenticationService } from '../../services/authentication.service';
import { catchError } from 'rxjs/operators';
import { ApiErrorMessageDto, RecaptchaPublicConfigDto } from '@api-rest/api-model';
import { PublicService } from '@api-rest/services/public.service';
import { ActivatedRoute } from '@angular/router';

@Component({
	selector: 'app-hospital-login',
	templateUrl: './hospital-login.component.html',
	styleUrls: ['./hospital-login.component.scss']
})
export class HospitalLoginComponent implements OnInit {
	apiError: ApiErrorMessageDto = null;
	form: FormGroup;
	recaptchaRes: string;
	recaptchaEnable = false;
	recaptchaSiteKey = '';
	private returnUrl: string;

	constructor(
		private formBuilder: FormBuilder,
		private authenticationService: AuthenticationService,
		private publicService: PublicService,
		private route: ActivatedRoute) {
	}

	ngOnInit(): void {

		this.form = this.formBuilder.group({
			username: [null, Validators.required],
			password: [null, Validators.required],
			recaptchaReactive: []
		});

		this.publicService.getRecaptchaPublicConfig().subscribe((recaptchaPublicConfigDto: RecaptchaPublicConfigDto) => {
			if (recaptchaPublicConfigDto) {
				this.recaptchaEnable = recaptchaPublicConfigDto.enabled;
				this.recaptchaSiteKey = recaptchaPublicConfigDto.siteKey;
				if (this.recaptchaEnable) {
					this.form.controls.recaptchaReactive.setValidators(Validators.required);
				}
			}
		});

		this.route.queryParams.subscribe(params => {
			if (params.returnUrl) {
				this.returnUrl = params.returnUrl;
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
				this.recaptchaRes,
			).pipe(
				catchError((err: ApiErrorMessageDto) => {
					this.apiError = err;
					this.form.controls.recaptchaReactive.reset();
					this.form.enable();
					throw err;
				}),
			).subscribe(
				() => this.returnUrl ? this.authenticationService.go(this.returnUrl) : this.authenticationService.go()
			);
		}
	}

	reCaptchaResolved(captchaResponse: string) {
		this.recaptchaRes = captchaResponse;
	}

}
