import { Component, EventEmitter, Input, OnChanges, OnInit, Output } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { map, startWith } from 'rxjs/operators';
import { MatOptionSelectionChange } from '@angular/material/core';
import { MatAutocompleteTrigger } from '@angular/material/autocomplete';

@Component({
	selector: 'app-typeahead',
	templateUrl: './typeahead.component.html',
	styleUrls: ['./typeahead.component.scss']
})
export class TypeaheadComponent implements OnInit, OnChanges {

	@Input() options: TypeaheadOption<any>[] = [];
	@Input() placeholder: string;
	@Input() externalSetValue: TypeaheadOption<any>;
	@Output() selectionChange = new EventEmitter();

	form: FormGroup;
	optionsFiltered: TypeaheadOption<any>[];
	optionSelected: TypeaheadOption<any>;

	constructor(private readonly formBuilder: FormBuilder) {
		this.form = this.formBuilder.group({
			searchValue: [null]
		});
	}

	ngOnInit(): void {
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

		if (this.externalSetValue && this.options) {
			this.form.controls.searchValue.setValue(this.externalSetValue.compareValue);
			this.optionSelected = this.externalSetValue;
		}

		if (this.optionSelected && this.optionsNotIncludesSelected()) {
			this.reset();
		}
	}

	select(event: MatOptionSelectionChange, option: TypeaheadOption<any>): void {
		if (event.isUserInput) {
			this.optionSelected = option;
			this.selectionChange.emit(option.value);
		}
	}

	clear(event, autocompleteTrigger: MatAutocompleteTrigger ): void {
		this.reset();
		event.stopPropagation();
		autocompleteTrigger.openPanel();
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

}

export interface TypeaheadOption<T> {
	value: T;
	compareValue: string;
	viewValue?: string;
}
