import { Component, EventEmitter, Input, OnDestroy, forwardRef } from '@angular/core';
import { FormControl, FormGroup, NG_VALIDATORS, NG_VALUE_ACCESSOR, Validators } from '@angular/forms';
import { STRING_PATTERN, hasError } from '@core/utils/form.utils';
import { AbstractCustomForm } from '@core/abstract-class/AbstractCustomForm';

@Component({
	selector: 'app-full-name-custom-form',
	templateUrl: './full-name-custom-form.component.html',
	styleUrls: ['./full-name-custom-form.component.scss'],
	providers: [
		{
			provide: NG_VALUE_ACCESSOR,
			multi: true,
			useExisting: forwardRef(() => FullNameCustomFormComponent),
		},
		{
			provide: NG_VALIDATORS,
			multi: true,
			useExisting: forwardRef(() => FullNameCustomFormComponent),
		},
	],
})
export class FullNameCustomFormComponent extends AbstractCustomForm implements OnDestroy {

	readonly hasError = hasError;
	form: FormGroup<FullNameCustomForm>;
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
		this.form = new FormGroup<FullNameCustomForm>({
			name: new FormControl(null, [Validators.required, Validators.pattern(STRING_PATTERN)]),
			lastName: new FormControl(null, [Validators.required, Validators.pattern(STRING_PATTERN)])
		});
	}

}

interface FullNameCustomForm {
	name: FormControl<string>;
	lastName: FormControl<string>;
}
