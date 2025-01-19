import { Component, Input, forwardRef } from '@angular/core';
import { NG_VALUE_ACCESSOR, UntypedFormBuilder, UntypedFormControl } from '@angular/forms';
import { COLOR } from '@turnos/constants/appointment';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-diary-label',
  templateUrl: './diary-label.component.html',
  styleUrls: ['./diary-label.component.scss'],
  providers: [
		{
			provide: NG_VALUE_ACCESSOR,
			useExisting: forwardRef(() => DiaryLabelComponent),
			multi: true,
		}
	],
})
export class DiaryLabelComponent {

	@Input() colorList: COLOR[] = [];
	@Input() currentColor: COLOR;
	onTouched = () => { };
	onChangeSub: Subscription;

	form = this.formBuilder.group({
		id: new UntypedFormControl(null),
		color: new UntypedFormControl(null),
		description: new UntypedFormControl(null),
	});

  	constructor(private formBuilder: UntypedFormBuilder) { }

	updateLabel(currentColor: COLOR, newColor: COLOR) {
		this.form.get('color').setValue(newColor);
		this.colorList.splice(this.colorList.findIndex((c: COLOR) => c.id === newColor.id), 1);
		this.colorList.push(currentColor);
	}

	writeValue(obj: any): void {
		if (obj)
			this.form.setValue(obj);
	}

	registerOnChange(fn: any): void {
		this.onChangeSub = this.form.valueChanges
			.subscribe(value => {
				const toEmit = this.form.valid ? value : null;
				fn(toEmit);
			})
	}

	registerOnTouched(fn: any): void {
		this.onTouched = fn;
	}

	setDisabledState?(isDisabled: boolean): void {
		isDisabled ? this.form.disable() : this.form.enable();
	}

	ngOnDestroy(): void {
		this.onChangeSub.unsubscribe();
	}

}
