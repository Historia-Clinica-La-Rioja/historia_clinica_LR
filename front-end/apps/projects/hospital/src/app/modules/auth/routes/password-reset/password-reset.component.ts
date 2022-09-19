import { Component, OnInit } from '@angular/core';
import { PasswordResetService } from '../../../api-rest/services/password-reset.service';
import { ActivatedRoute, ParamMap } from '@angular/router';
import { switchMap, map } from 'rxjs/operators';
import { Observable } from 'rxjs';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { ApiErrorMessageDto } from '@api-rest/api-model';
import {patternValidator} from "@core/utils/form.utils";

@Component({
	selector: 'app-password-reset',
	templateUrl: './password-reset.component.html',
	styleUrls: ['./password-reset.component.scss']
})
export class PasswordResetComponent implements OnInit {
	public passwordResetToken$: Observable<string>;
	public form: FormGroup;
	public apiResponse: any = null;
	public hidePassword = true;

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
			password:[null, [Validators.required,
				Validators.minLength(8),
				patternValidator(new RegExp('(?=.*[a-z])'), {'min': true}),
				patternValidator(new RegExp('(?=.*[A-Z])'), {'mayus': true}),
				patternValidator(new RegExp('(?=.*[0-9])'), {'number': true}),
			]],
		});
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
				(error: ApiErrorMessageDto) => {
					this.apiResponse = {
						error
					};
				}
			);
		}
	}
}
