import { Component, Input, forwardRef } from '@angular/core';
import { FormBuilder, NG_VALUE_ACCESSOR, Validators} from '@angular/forms';
import { hasError } from '@core/utils/form.utils';
import { Subscription } from 'rxjs';

@Component({
	selector: 'app-evolucion-form',
	templateUrl: './evolucion-form.component.html',
	styleUrls: ['./evolucion-form.component.scss'],
	providers: [
		{
			provide: NG_VALUE_ACCESSOR,
			useExisting: forwardRef(() => EvolucionFormComponent),
			multi: true,
		}
	]
})
export class EvolucionFormComponent {

	@Input()
	set markAsTouched(value: boolean) {
		if (value) {
			this.formEvolucion.get('evolucion')?.markAsTouched();
		}
	}

	hasError = hasError;
	onChangeSub: Subscription;
	formEvolucion = this.formBuilder.group({
		evolucion: [null, Validators.required],
	});

	constructor(
		private formBuilder: FormBuilder,
	) { }

	onTouched = () => { };

	writeValue(obj: any): void {
		if (obj)
			this.formEvolucion.setValue(obj);
	}

	registerOnChange(fn: any): void {
		this.onChangeSub = this.formEvolucion.valueChanges
			.subscribe(value => {
				const toEmit = this.formEvolucion.valid ? value : null;
				fn(toEmit);
			})
	}

	registerOnTouched(fn: any): void {
		this.onTouched = fn;
	}

	setDisabledState?(isDisabled: boolean): void {
		isDisabled ? this.formEvolucion.disable() : this.formEvolucion.enable();
	}

	ngOnDestroy(): void {
		this.onChangeSub.unsubscribe();
	}

}
