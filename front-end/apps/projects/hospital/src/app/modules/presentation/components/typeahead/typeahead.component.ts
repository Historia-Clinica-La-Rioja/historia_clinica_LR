import { Component, EventEmitter, Input, OnChanges, OnInit, Output } from '@angular/core';
import { UntypedFormBuilder, UntypedFormGroup, Validators } from '@angular/forms';
import { map, startWith } from 'rxjs/operators';
import { MatOptionSelectionChange } from '@angular/material/core';

@Component({
	selector: 'app-typeahead',
	templateUrl: './typeahead.component.html',
	styleUrls: ['./typeahead.component.scss']
})
export class TypeaheadComponent implements OnInit, OnChanges {

	@Input() options: TypeaheadOption<any>[] = [];
	@Input() placeholder: string;
	@Input() titleInput: string = ' ';
	@Input() externalSetValue: TypeaheadOption<any>;
	@Output() selectionChange = new EventEmitter();
	@Input() required :boolean;
	@Input() disabled? :boolean;

	form: UntypedFormGroup;
	optionsFiltered: TypeaheadOption<any>[];
	optionSelected: TypeaheadOption<any>;

	constructor(private readonly formBuilder: UntypedFormBuilder,
	) {
		this.setFormAndSubscribe();
	}

	ngOnInit(): void {
		this.isRequired();
	}

	ngOnChanges(): void {
		this.optionsFiltered = this.options;

		if (this.options) {
			this.form.controls.searchValue.setValue(this.externalSetValue?.compareValue);
			this.optionSelected = this.externalSetValue;
			if (this.externalSetValue) {
				this.selectionChange.emit(this.optionSelected?.value);
			}
		}

		if (this.optionSelected && this.optionsNotIncludesSelected()) {
			this.reset();
		}

		if (this.disabled) {
			this.form.controls.searchValue.disable();
		} else {
			this.form.controls.searchValue.enable();
		}
	}

	isRequired() :void {
		if(this.required){
		this.form.controls.searchValue.setValidators(Validators.required);
		this.form.controls.searchValue.setValue(null);
		this.form.controls.searchValue.updateValueAndValidity();
		}
	}

	select(event: MatOptionSelectionChange, option: TypeaheadOption<any>): void {
		if (!this.disabled && event.isUserInput) {
			this.optionSelected = option;
			this.selectionChange.emit(option.value);
		}
		if (this.disabled) {
			this.reset();
		}
	}

	clear(event): void {
		this.reset();
		event.stopPropagation();
	}

	private reset(): void {
		this.optionSelected = null;
		this.form.controls.searchValue.reset();
		this.selectionChange.emit(null);
	}

	private filter(value: string): TypeaheadOption<any>[] {
		if (!value) {
			return this.options;
		}
		const searchValue = value?.toLowerCase();
		return this.options?.filter(opt => {
			return opt.compareValue.toLowerCase().includes(searchValue);
		});
	}

	private optionsNotIncludesSelected(): boolean {
		return this.optionsFiltered && !this.optionsFiltered
			.find(o => o.compareValue === this.optionSelected.compareValue);
	}

	private setFormAndSubscribe() {
		this.form = this.formBuilder.group({
			searchValue: [null]
		});

		this.form.controls.searchValue.valueChanges
			.pipe(
				startWith(''),
				map(value => this.filter(value)))
			.subscribe(filtered => this.optionsFiltered = filtered
			);
	}

}

export interface TypeaheadOption<T> {
	value: T;
	compareValue: string;
	viewValue?: string;
	disabled?: boolean;
}
