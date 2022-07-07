import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormGroup, ValidatorFn, Validators} from "@angular/forms";
import {ApiErrorMessageDto, PasswordDto} from "@api-rest/api-model";
import {AuthService} from "@api-rest/services/auth.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-update-password',
  templateUrl: './update-password.component.html',
  styleUrls: ['./update-password.component.scss']
})
export class UpdatePasswordComponent implements OnInit {
	public form: FormGroup;
	public apiResponse: any = null;
	private pass: string;
	private newpass: string;
	public apiError: string;
	public matchingError: string;
	public hidePassword = true;
	public hideNewPassword = true;
	public hideReNewPassword = true;



  constructor(private formBuilder: FormBuilder,
			  private authService: AuthService,
			  private router: Router
			  ) { }

  ngOnInit(): void {
	  this.form = this.formBuilder.group({
		  password: [null, Validators.required],
		  newPassword: [null, [Validators.required, Validators.min(8), Validators.pattern('((?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])).{8,}')]],
	  });
  }

	submit() {
		this.pass = this.form.get("password").value;
		this.newpass = this.form.get("newPassword").value;
		if (this.form.valid) {
			this.form.disable();
			this.authService.updatePassword({newPassword: this.newpass, password: this.pass}).subscribe(
				(data: any) => {
					this.router.navigate(['home/update-password-success']);
				},
				(error: ApiErrorMessageDto) => {
					this.form.enable();
					this.apiError = error.text;
				}
			)};
		}

		cancel(){
			this.router.navigate(['home/profile']);
		}

	hasError(type: string, control: string): boolean {
		return this.form.get(control).hasError(type);
	}

}
