import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthService } from '@api-rest/services/auth.service';

@Component({
	selector: 'app-password-recover',
	templateUrl: './password-recover.component.html',
	styleUrls: ['./password-recover.component.scss']
})
export class PasswordRecoverComponent implements OnInit {
	form: FormGroup;
	spinner = false;
	showTextError = false;
	email = '';
	emailSent = false;
	userFound = false;

	constructor(
		private formBuilder: FormBuilder,
		private router: Router,
		private route: ActivatedRoute,
		private authService: AuthService,

	) { }

	ngOnInit(): void {
		this.form = this.formBuilder.group({
			username: [null, Validators.required],
		})
		this.route.paramMap.subscribe(params => {

			if (params.get('user'))
				this.form.controls.username.setValue(params.get('user'));
		})
		this.form.valueChanges.subscribe(p => this.showTextError = false);
	}

	hasError(type: string, control: string): boolean {
		return this.form.get(control).hasError(type);
	}

	submit() {

		this.spinner = true;

		if (this.form.valid) {
			this.form.disable();
			this.authService.restorePassword(
				this.form.value.username
			).subscribe(
				(response) => {
					this.userFound = true;
					this.emailSent = true;
					this.email = response;
				},
				error => {
					let err = JSON.parse(error);
					switch (err.code) {
						case ('UNEXISTED_USER'):
							{
								this.form.enable();
								this.spinner = false;
								this.showTextError = true;
							}
							break;
						case ('INVALID_EMAIL'):
							this.userFound = true;
							break;
						default:
					}
				}
			);
		}
	}

	goBack() {
		this.router.navigate(['auth/login']);
	}
}
