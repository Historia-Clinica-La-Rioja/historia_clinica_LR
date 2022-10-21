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
	@Input() titleInput: string = ' ';
	@Input() externalSetValue: TypeaheadOption<any>;
	@Output() selectionChange = new EventEmitter();

	form: FormGroup;
	optionsFiltered: TypeaheadOption<any>[];
	optionSelected: TypeaheadOption<any>;

	constructor(private readonly formBuilder: FormBuilder,
	) {
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

		if (this.externalSetValue && this.optionsIncludes(this.externalSetValue)) {
			this.optionSelected = this.externalSetValue;

			this.form.controls.searchValue.setValue(this.optionSelected?.compareValue);
			this.selectionChange.emit(this.optionSelected?.value);
		}
		else {
			this.reset();
		}
	}

	select(event: MatOptionSelectionChange, option: TypeaheadOption<any>): void {
		if (event.isUserInput) {
			this.optionSelected = option;
			this.selectionChange.emit(option.value);
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

	private optionsIncludes(option: TypeaheadOption<any>): boolean {
		return this.optionsFiltered && !!this.optionsFiltered
			.find(o => o.compareValue === option.compareValue);
	}

}

export interface TypeaheadOption<T> {
	value: T;
	compareValue: string;
	viewValue?: string;
}
