import { EventEmitter } from "@angular/core";
import { AbstractControl, ControlValueAccessor, FormGroup, ValidationErrors, Validator } from "@angular/forms";
import { Subscription } from "rxjs";

export abstract class AbstractCustomForm implements ControlValueAccessor, Validator {

	form: FormGroup<any>;
	onTouched: Function = () => { };
	onChangeSubs: Subscription[] = [];
	submitFormEvent: EventEmitter<void>;

	constructor() { }

	abstract createForm();

	unSubcribeFormChanges() {
		this.onChangeSubs.forEach(subscribe => subscribe.unsubscribe());
	}

	registerOnChange(onChange: any) {
		const sub = this.form.valueChanges.subscribe(onChange);
		this.onChangeSubs.push(sub);
	}

	registerOnTouched(onTouched: Function) {
		this.onTouched = onTouched;
	}

	setDisabledState(disabled: boolean) {
		disabled ? this.form.disable() : this.form.enable();
	}

	writeValue(value: any) {
		if (value)
			this.form.setValue(value, { emitEvent: false });
	}

	validate(control: AbstractControl): ValidationErrors | null {
		if (this.form.valid)
			return null;

		let errors: any = {};

		const formControls = this.form.controls;

		Object.keys(formControls).forEach(formControl =>
			errors = this.addControlErrors(errors, formControl)
		);

		return errors;
	}

	addControlErrors(allErrors: any, controlName: string): any {
		const errors = { ...allErrors };
		const controlErrors = this.form.controls[controlName].errors;

		if (controlErrors)
			errors[controlName] = controlErrors;

		return errors;
	}

	subscribeToSubmitParentForm(submitFormEvent: EventEmitter<void>) {
		submitFormEvent.subscribe(() => {
			this.form.markAllAsTouched();
			this.form.updateValueAndValidity();
		});
	}
}