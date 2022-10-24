import {Component, OnInit } from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {ApiErrorMessageDto} from "@api-rest/api-model";
import {AuthService} from "@api-rest/services/auth.service";
import {Router} from "@angular/router";
import {patternValidator} from "@core/utils/form.utils";

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
		  newPassword: [null, [Validators.required,
			  Validators.minLength(8),
			  patternValidator(new RegExp('(?=.*[a-z])'), {'min': true}),
			  patternValidator(new RegExp('(?=.*[A-Z])'), {'mayus': true}),
			  patternValidator(new RegExp('(?=.*[0-9])'), {'number': true}),
		  ]],
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

	updateValidation(){
		this.apiError = null;
	}

}
