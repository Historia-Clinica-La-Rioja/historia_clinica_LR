import { Component, EventEmitter, Input, forwardRef } from '@angular/core';
import { FormGroup, Validators, NG_VALIDATORS, NG_VALUE_ACCESSOR, FormControl } from '@angular/forms';
import { AbstractCustomForm } from '@core/abstract-class/AbstractCustomForm';
import { hasError } from '@core/utils/form.utils';

@Component({
	selector: 'app-email-custom-form',
	templateUrl: './email-custom-form.component.html',
	styleUrls: ['./email-custom-form.component.scss'],
	providers: [
		{
			provide: NG_VALUE_ACCESSOR,
			multi: true,
			useExisting: forwardRef(() => EmailCustomFormComponent),
		},
		{
			provide: NG_VALIDATORS,
			multi: true,
			useExisting: forwardRef(() => EmailCustomFormComponent),
		},
	],
})
export class EmailCustomFormComponent extends AbstractCustomForm {

	readonly hasError = hasError;
	form: FormGroup<EmailCustomForm>;
	@Input()
	set submitParentFormEvent(event: EventEmitter<void>) {
		super.subscribeToSubmitParentForm(event);
	};

	constructor() {
		super();
		this.createForm();
	}

	createForm() {
		this.form = new FormGroup<EmailCustomForm>({
			email: new FormControl(null, [Validators.required, Validators.email]),
		});
	}
}

interface EmailCustomForm {
	email: FormControl<string>;
}