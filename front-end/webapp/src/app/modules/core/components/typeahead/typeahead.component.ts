import { Component, EventEmitter, Input, OnChanges, OnInit, Output } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
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
	@Input() initValue: TypeaheadOption<any>;
	@Output() onSelectionChange = new EventEmitter();

	form: FormGroup;
	optionsFiltered: TypeaheadOption<any>[];
	optionSelected: TypeaheadOption<any>;

	constructor(private readonly formBuilder: FormBuilder) {
	}

	ngOnInit(): void {
		this.form = this.formBuilder.group({
			searchValue: [null]
		});

		this.form.controls.searchValue.valueChanges
			.pipe(
				startWith(''),
				map(value => this.filter(value)))
			.subscribe(filtered => {
				this.optionsFiltered = filtered;
			});
	}

	ngOnChanges(): void {
		this.optionsFiltered = this.options;

		if (this.initValue && this.options && !this.optionSelected) {
			this.form.controls.searchValue.setValue(this.initValue.compareValue);
			this.optionSelected = this.initValue;
		}

		if (this.optionSelected && this.optionsNotIncludesSelected()) {
			this.reset();
		}
	}

	select(event: MatOptionSelectionChange, option: TypeaheadOption<any>): void {
		if (event.isUserInput) {
			this.optionSelected = option;
			this.onSelectionChange.emit(option.value);
		}
	}

	clear(): void {
		this.reset();
	}

	private reset(): void {
		this.optionSelected = null;
		this.form.controls.searchValue.reset();
		this.onSelectionChange.emit(null);
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

}

export interface TypeaheadOption<T> {
	value: T;
	compareValue: string;
	viewValue?: string;
}
