import { Component, EventEmitter, forwardRef, Input } from '@angular/core';
import { FormControl, FormGroup, NG_VALIDATORS, NG_VALUE_ACCESSOR, Validators } from '@angular/forms';
import { AbstractCustomForm } from '@core/abstract-class/AbstractCustomForm';
import { ToFormGroup } from '@core/utils/form.utils';

@Component({
	selector: 'app-observations-custom-form',
	templateUrl: './observations-custom-form.component.html',
	styleUrls: ['./observations-custom-form.component.scss'],
	providers: [
		{
			provide: NG_VALUE_ACCESSOR,
			multi: true,
			useExisting: forwardRef(() => ObservationsCustomFormComponent),
		},
		{
			provide: NG_VALIDATORS,
			multi: true,
			useExisting: forwardRef(() => ObservationsCustomFormComponent),
		},
	],
})
export class ObservationsCustomFormComponent extends AbstractCustomForm {

	form: FormGroup<ToFormGroup<ObservationCustomForm>>;

	@Input()
	set isRequired(isRequired: boolean) {
		isRequired ? this.addRequiredValidator() : this.removeRequiredValidator();
	};
	@Input()
	set submitParentFormEvent(event: EventEmitter<void>) {
		if (event)
			super.subscribeToSubmitParentForm(event);
	};

	constructor() {
		super();
		this.createForm();
	}

	createForm() {
		this.form = new FormGroup<ToFormGroup<ObservationCustomForm>>({
			observations: new FormControl<string>(null)
		});
	}

	private addRequiredValidator() {
		this.form.controls.observations.addValidators(Validators.required);
		this.form.controls.observations.updateValueAndValidity();
	}

	private removeRequiredValidator() {
		this.form.controls.observations.removeValidators(Validators.required);
		this.form.controls.observations.updateValueAndValidity();
	}

}

export interface ObservationCustomForm {
	observations: string;
}