import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ApiErrorMessageDto, PersonDataDto } from '@api-rest/api-model';
import { PublicUserService } from '@api-rest/services/public-user.service';
import { AccessDataService } from '@api-rest/services/access-data.service';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { processErrors } from '@core/utils/form.utils';

@Component({
	selector: 'app-access-data-reset',
	templateUrl: './access-data-reset.component.html',
	styleUrls: ['./access-data-reset.component.scss']
})
export class AccessDataResetComponent implements OnInit {
	public token: string;
	public form: FormGroup;
	public userPerson: PersonDataDto;
	public apiResponse: any = null;
	public location: string = window.location.href;

	constructor(private route: ActivatedRoute,
				private formBuilder: FormBuilder,
				private publicUserService: PublicUserService,
				private accessDataService: AccessDataService,
				private readonly snackBarService: SnackBarService) {
	}

	ngOnInit(): void {
		this.route.paramMap.subscribe(
			(params) => {
				this.token = params.get('token');
				this.publicUserService.getUserPersonData(this.token).subscribe
				(userPerson => {
					this.userPerson = userPerson;
				}, error => {
					this.apiResponse = {
						error
					};
				})
			});
		this.form = this.formBuilder.group({
			username: [null, Validators.required],
			password: [null, Validators.required],
			repassword: [null, Validators.required],
		}, {validator: this.checkIfMatchingPasswords('password', 'repassword')});
	}

	private checkIfMatchingPasswords(passwordKey: string, passwordConfirmationKey: string) {
		return (group: FormGroup) => {
			const passwordInput = group.controls[passwordKey];
			const passwordConfirmationInput = group.controls[passwordConfirmationKey];
			if (passwordInput.value !== passwordConfirmationInput.value) {
				return passwordConfirmationInput.setErrors({notEquivalent: true});
			} else {
				return passwordConfirmationInput.setErrors(null);
			}
		};
	}

	hasError(type: string, control: string): boolean {
		return this.form.get(control).hasError(type);
	}

	submit() {
		if (this.form.valid) {
			this.apiResponse = {
				loading: true
			}
			this.accessDataService.updateAccessData(
				{
					password: this.form.value.password,
					token: this.token,
					userId: this.userPerson.userId,
					username: this.form.value.username
				}).subscribe(
				() => {
					this.apiResponse = {
						ok: true
					};
				},
				(error: ApiErrorMessageDto) => {
					processErrors(error, (msg) => this.snackBarService.showError(msg));
					this.apiResponse = null;
				}
			);
		}
	}
}

