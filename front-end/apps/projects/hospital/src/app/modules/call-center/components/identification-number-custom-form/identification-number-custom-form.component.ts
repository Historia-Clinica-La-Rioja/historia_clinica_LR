import { Component, EventEmitter, Input, OnDestroy, forwardRef } from '@angular/core';
import { FormGroup, Validators, NG_VALIDATORS, NG_VALUE_ACCESSOR, FormControl } from '@angular/forms';
import { AbstractCustomForm } from '@core/abstract-class/AbstractCustomForm';

@Component({
	selector: 'app-identification-number-custom-form',
	templateUrl: './identification-number-custom-form.component.html',
	styleUrls: ['./identification-number-custom-form.component.scss'],
	providers: [
		{
			provide: NG_VALUE_ACCESSOR,
			multi: true,
			useExisting: forwardRef(() => IdentificationNumberCustomFormComponent),
		},
		{
			provide: NG_VALIDATORS,
			multi: true,
			useExisting: forwardRef(() => IdentificationNumberCustomFormComponent),
		},
	],
})
export class IdentificationNumberCustomFormComponent extends AbstractCustomForm implements OnDestroy {

	form: FormGroup<IdentificationNumberCustomForm>;
	@Input()
	set submitParentFormEvent(event: EventEmitter<void>) {
		super.subscribeToSubmitParentForm(event);
	};

	constructor() {
		super();
		this.createForm();
	}

	ngOnDestroy() {
		super.unSubcribeFormChanges();
	}

	createForm() {
		this.form = new FormGroup<IdentificationNumberCustomForm>({
			identificationNumber: new FormControl(null, Validators.required)
		});
	}
}

interface IdentificationNumberCustomForm {
	identificationNumber: FormControl<string>;
}
