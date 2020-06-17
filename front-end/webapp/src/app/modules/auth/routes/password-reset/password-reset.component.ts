import { Component, OnInit } from '@angular/core';
import { PasswordResetService } from '../../../api-rest/services/password-reset.service';
import { ActivatedRoute, ParamMap } from '@angular/router';
import { switchMap, map } from 'rxjs/operators';
import { Observable } from 'rxjs';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { ApiErrorMessage } from '@api-rest/api-model';

@Component({
	selector: 'app-password-reset',
	templateUrl: './password-reset.component.html',
	styleUrls: ['./password-reset.component.scss']
})
export class PasswordResetComponent implements OnInit {
	public passwordResetToken$: Observable<string>;
	public form: FormGroup;
	public apiResponse: any = null;

	constructor(
		private route: ActivatedRoute,
		private formBuilder: FormBuilder,
		private service: PasswordResetService
	) { }

	ngOnInit(): void {
		this.passwordResetToken$ = this.route.paramMap.pipe(
			map((params: ParamMap) => params.get('token'))
		);
		this.form = this.formBuilder.group({
			password: [null, Validators.required],
			repassword: [null, Validators.required],
		}, { validator: this.checkIfMatchingPasswords('password', 'repassword') });
	}

	private checkIfMatchingPasswords(passwordKey: string, passwordConfirmationKey: string) {
		return (group: FormGroup) => {
			const passwordInput = group.controls[passwordKey];
			const passwordConfirmationInput = group.controls[passwordConfirmationKey];
			if (passwordInput.value !== passwordConfirmationInput.value) {
				return passwordConfirmationInput.setErrors({ notEquivalent: true })
			}
			else {
				return passwordConfirmationInput.setErrors(null);
			}
		}
	}

	hasError(type: string, control: string): boolean {
		return this.form.get(control).hasError(type);
	}


	submit() {
		if (this.form.valid) {
			this.apiResponse = {
				loading: true,
			};
			this.passwordResetToken$.pipe(
				switchMap(token => this.service.setPassword({token, password: this.form.value.password}))
			).subscribe(
				(data: any) => {
					this.apiResponse = {
						data
					};
				},
				(error: ApiErrorMessage) => {
					this.apiResponse = {
						error
					};
				}
			);
		}
	}
}
