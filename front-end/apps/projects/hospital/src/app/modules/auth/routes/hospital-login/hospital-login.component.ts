import { Component, OnInit } from '@angular/core';
import { UntypedFormGroup, UntypedFormBuilder, Validators } from '@angular/forms';
import { AuthenticationService } from '../../services/authentication.service';
import { catchError } from 'rxjs/operators';
import { ApiErrorMessageDto, RecaptchaPublicConfigDto } from '@api-rest/api-model';
import { AppFeature } from '@api-rest/api-model';
import { ERole } from '@api-rest/api-model';
import { PublicService } from '@api-rest/services/public.service';
import { Router } from '@angular/router';
import { MatDialog } from "@angular/material/dialog";
import { LoginPinCodeComponent } from "../../dialogs/login-pin-code/login-pin-code.component";
import { FeatureFlagService } from "@core/services/feature-flag.service";

@Component({
	selector: 'app-hospital-login',
	templateUrl: './hospital-login.component.html',
	styleUrls: ['./hospital-login.component.scss']
})
export class HospitalLoginComponent implements OnInit {
	apiError: ApiErrorMessageDto = null;
	form: UntypedFormGroup;
	recaptchaRes: string;
	recaptchaEnable = false;
	recaptchaSiteKey = '';
	hidePassword = true;
	ffRecoverPasswordIsOn: boolean;

	constructor(
		private formBuilder: UntypedFormBuilder,
		private authenticationService: AuthenticationService,
		private publicService: PublicService,
		private router: Router,
		private readonly dialog: MatDialog,
		private readonly featureFlagService: FeatureFlagService
	) {
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
		this.featureFlagService.isActive(AppFeature.HABILITAR_RECUPERAR_PASSWORD).subscribe(isOn => this.ffRecoverPasswordIsOn = isOn);
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
				(response) => {
					let partiallyAuthenticated = response.roleAssignments
						.filter((ra) => ra.role === ERole.PARTIALLY_AUTHENTICATED).length > 0;
					if (!partiallyAuthenticated) {
						this.doRedirect();
						return;
					}
					this.openLoginPinCodeDialog();
				}
			);
		}
	}

	private openLoginPinCodeDialog() {
		const dialogRef = this.dialog.open(LoginPinCodeComponent, {
			width: '30%'
		})
		dialogRef.afterClosed().subscribe(result => {
			if (result?.loginSuccessful) {
				this.doRedirect();
				return;
			}
			this.form.reset();
			this.form.enable();
		})
	}

	private doRedirect() {
		this.authenticationService.go();
	}

	reCaptchaResolved(captchaResponse: string) {
		this.recaptchaRes = captchaResponse;
	}

	goToRecoverPassword() {
		const user = this.form.value?.username || '';
		this.router.navigate(['auth/password-recover', user]);
	}
}
