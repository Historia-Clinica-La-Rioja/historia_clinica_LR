import { Component, EventEmitter, Input, forwardRef } from '@angular/core';
import { FormGroup, Validators, NG_VALUE_ACCESSOR, NG_VALIDATORS, FormControl } from '@angular/forms';
import { AbstractCustomForm } from '@core/abstract-class/AbstractCustomForm';
import { VALIDATIONS, hasError } from '@core/utils/form.utils';

@Component({
	selector: 'app-phone-custom-form',
	templateUrl: './phone-custom-form.component.html',
	styleUrls: ['./phone-custom-form.component.scss'],
	providers: [
		{
			provide: NG_VALUE_ACCESSOR,
			multi: true,
			useExisting: forwardRef(() => PhoneCustomFormComponent),
		},
		{
			provide: NG_VALIDATORS,
			multi: true,
			useExisting: forwardRef(() => PhoneCustomFormComponent),
		},
	]
})
export class PhoneCustomFormComponent extends AbstractCustomForm {

	readonly hasError = hasError;
	readonly MAX_LENGTH_PREFIX = VALIDATIONS.MAX_LENGTH.phonePrefix;
	readonly MAX_LENGTH_NUMBER = VALIDATIONS.MAX_LENGTH.phone;
	form: FormGroup<PhoneCustomForm>;
	@Input()
	set submitParentFormEvent(event: EventEmitter<void>) {
		super.subscribeToSubmitParentForm(event);
	};

	constructor() {
		super();
		this.createForm();
	}

	createForm() {
		this.form = new FormGroup<PhoneCustomForm>({
			prefix: new FormControl(null, [Validators.required, Validators.maxLength(this.MAX_LENGTH_PREFIX)]),
			number: new FormControl(null, [Validators.required, Validators.maxLength(this.MAX_LENGTH_NUMBER)]),
		});
	}

}

interface PhoneCustomForm {
	prefix: FormControl<string>;
	number: FormControl<string>;
}