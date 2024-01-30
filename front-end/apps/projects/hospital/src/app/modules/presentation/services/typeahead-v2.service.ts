import { Injectable } from "@angular/core";
import { FormControl, FormGroup, UntypedFormBuilder } from "@angular/forms";
import { TypeaheadOption } from "@presentation/components/typeahead/typeahead.component";
import { BehaviorSubject } from "rxjs";

@Injectable({
	providedIn: 'root'
})

export class TypeaheadV2Service {
	disabled = false;
	form: FormGroup;
	externalValue: TypeaheadOption<any> = null;
	private selectValue = new BehaviorSubject<TypeaheadOption<any>>(null);
	readonly selectValue$ = this.selectValue.asObservable();

	private optionfilter = new BehaviorSubject<TypeaheadOption<any>[]>([]);
	readonly optionfilter$ = this.optionfilter.asObservable();

	private disabledObs = new BehaviorSubject<boolean>(false);
	readonly disabled$ = this.disabledObs.asObservable();

	private options: TypeaheadOption<any>[];

	constructor(
		private readonly formBuilder: UntypedFormBuilder,

	) {
		this.initForm();
		this.suscribeForm();
	}

	filterOptions(input: string, options: TypeaheadOption<any>[]) {
		if (input) {
			const inputValue = input?.toLowerCase().trim();
			if (inputValue)
				this.setOptionfilter(options.filter(option =>
					option.compareValue.toLowerCase().includes(inputValue)
				));
		}
	}

	setOptions(options: TypeaheadOption<any>[]) {
		this.options = options;
		this.setOptionfilter(options);
	}

	select(optionToSelect: TypeaheadOption<any>) {
		if (!this.disabled)
			if (!!!this.options)
				this.emit(optionToSelect);
			else
				this.emit(null);
	}

	clear() {
		this.form.controls.searchValue.setValue(null);
		this.setOptionfilter(this.options);
		this.emit(null);
	}

	setExternalSetValue(externalValue: TypeaheadOption<any>) {
		if (externalValue) {
			this.externalValue = externalValue;
			this.emit(externalValue);
		}
	}

	setDisabled(disabled: boolean) {
		this.disabled = disabled;
		this.disabledObs.next(disabled);
	}

	submitForm(value: TypeaheadOption<any>) {
		this.emit(value);
	}

	private emit(value: TypeaheadOption<any>) {
		this.selectValue.next(value);
	}

	private setOptionfilter(options: TypeaheadOption<any>[]) {
		this.optionfilter.next(options);
	}

	private initForm() {
		this.form = this.formBuilder.group({
			searchValue: [this.externalValue || null]
		}) as FormGroup & FormTypeahead;
	}

	private suscribeForm() {
		this.form.controls.searchValue.valueChanges
			.subscribe(filtered => {
				if (filtered)
					this.filterOptions(filtered, this.options)
			});
	}
}

interface FormTypeahead {
	observation: FormControl<string>;
}
