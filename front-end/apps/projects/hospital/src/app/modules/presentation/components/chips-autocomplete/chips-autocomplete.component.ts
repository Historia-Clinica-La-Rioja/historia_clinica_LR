import { Component, ElementRef, EventEmitter, Input, OnChanges, Output, ViewChild } from '@angular/core';
import { UntypedFormBuilder, UntypedFormControl, UntypedFormGroup } from '@angular/forms';
import { MatAutocompleteSelectedEvent } from '@angular/material/autocomplete';
import { pushIfNotExists } from '@core/utils/array.utils';
import { Observable, map, startWith } from 'rxjs';

@Component({
	selector: 'app-chips-autocomplete',
	templateUrl: './chips-autocomplete.component.html',
	styleUrls: ['./chips-autocomplete.component.scss']
})
export class ChipsAutocompleteComponent implements OnChanges {

	form: UntypedFormGroup;
	searchValue = new UntypedFormControl('');
	optionsFiltered: Observable<ChipsOption<any>[]>;
	optionsSelected: ChipsOption<any>[] = [];

	@Input() options: ChipsOption<any>[];
	@Input() placeholder: string;
	@Input() externalSetValues: ChipsOption<any>[];
	@Input() set clearOptionsSelected(clear: boolean) {
		if (clear) this.optionsSelected = [];
	} 
	@Output() selectionChange = new EventEmitter();
	@ViewChild('input') input: ElementRef<HTMLInputElement>;

	constructor(
		private readonly formBuilder: UntypedFormBuilder,
	) {
		this.form = this.formBuilder.group({
			searchValue: [null]
		});
	}

	ngOnChanges() {
		this.optionsFiltered = this.searchValue.valueChanges.pipe(
			startWith(null),
			map(value => (value ? this._filter(value) : this.options?.slice()))
		);

		if (this.externalSetValues?.length > 0 && !this.optionsSelected.length) {
			this.optionsSelected = this.externalSetValues;
			this.selectionChange.emit(this.optionsSelected);
		}
	}

	remove(value: ChipsOption<any>) {
		const index = this.optionsSelected.indexOf(value);

		if (index >= 0)
			this.optionsSelected.splice(index, 1);

		this.selectionChange.emit(this.optionsSelected);
	}

	selected(event: MatAutocompleteSelectedEvent) {
		this.optionsSelected = pushIfNotExists<any>(this.optionsSelected, event.option.value, this.compare);
		this.selectionChange.emit(this.optionsSelected);
		this.resetInput();
	}

	compare(concept1: ChipsOption<any>, concept2: ChipsOption<any>): boolean {
		return concept1.identifier === concept2.identifier
	}

	private _filter(value: any): ChipsOption<any>[] {
		const filterValue: string = value.compareValue ? value.compareValue : value;
		return this.options?.filter(opt => opt.compareValue.toLowerCase().includes(filterValue.toLowerCase()));
	}

	private resetInput() {
		this.input.nativeElement.value = '';
		this.searchValue.setValue(null);
	}
}

export interface ChipsOption<T> {
	value: T;
	compareValue: string;
	identifier: number;
}
