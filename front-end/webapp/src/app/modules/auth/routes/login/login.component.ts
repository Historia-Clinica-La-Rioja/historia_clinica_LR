import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { AuthenticationService } from '../../services/authentication.service';

@Component({
	selector: 'app-login',
	templateUrl: './login.component.html',
	styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {
	public form: FormGroup;
	constructor(
		private formBuilder: FormBuilder,
		private authenticationService: AuthenticationService,
	) { }

	ngOnInit(): void {
		this.form = this.formBuilder.group({
			username: ['admin@example.com', Validators.required],
			password: ['admin123', Validators.required],
		});
		// setTimeout(() => this.form.disable(), 2000);
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
			);
		}
	}
}
