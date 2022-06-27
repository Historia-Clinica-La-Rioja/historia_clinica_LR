import { Component, forwardRef, OnDestroy, OnInit } from '@angular/core';
import { ControlValueAccessor, FormBuilder, NG_VALUE_ACCESSOR, Validators } from '@angular/forms';
import { Subscription } from 'rxjs';

@Component({
	selector: 'app-date-range-time-form',
	templateUrl: './date-range-time-form.component.html',
	styleUrls: ['./date-range-time-form.component.scss'],
	providers: [
		{
			provide: NG_VALUE_ACCESSOR,
			useExisting: forwardRef(() => DateRangeTimeFormComponent),
			multi: true,
		}
	],
})
export class DateRangeTimeFormComponent implements ControlValueAccessor, OnDestroy, OnInit {

	form = this.formBuilder.group({
		date: [null, Validators.required],
		init: ["00:00", Validators.required],
		end: ["00:00", Validators.required],
	});

	onChangeSub: Subscription;

	constructor(private formBuilder: FormBuilder)
	{ }

	ngOnInit(): void {
		this.form.controls.init.valueChanges.subscribe(nv => {
			if (this.form.value.end < nv) {
				this.form.controls.end.setValue(nv);
			}
		});
	}

	onTouched = () => { };

	writeValue(obj: any): void {
		if (obj)
			this.form.setValue(obj);
	}

	registerOnChange(fn: any): void {
		this.onChangeSub = this.form.valueChanges
			.subscribe(value => {
				const toEmit = this.form.valid ? value : null
				fn(toEmit);
			});
	}

	registerOnTouched(fn: any): void {
		this.onTouched = fn;
	}

	ngOnDestroy(): void {
		this.onChangeSub.unsubscribe();
	}
}
