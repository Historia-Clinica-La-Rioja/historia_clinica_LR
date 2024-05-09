import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormControl, FormGroup, Validators } from "@angular/forms";
import { fixDate } from '@core/utils/date/format';

@Component({
	selector: 'app-datepicker',
	templateUrl: './datepicker.component.html',
	styleUrls: ['./datepicker.component.scss']
})

export class DatepickerComponent implements OnInit {

	form = new FormGroup({
		selectedDate: new FormControl(null)
	});

	@Input() enableDelete = false;
	@Input() title?: string;
	@Input() maxDate: Date;
	@Input() minDate: Date;
	@Input() availableDays: number[] = [];
	@Input() disableDays: Date[] = [];
	@Input() set dateToSetInDatepicker(dateToSet: Date) {
			this.form.controls.selectedDate.setValue(dateToSet);
	};
	@Output() selectDate: EventEmitter<Date> = new EventEmitter();
	@Input() set requiredText(text: string) {
		this._requiredText = text;
		text ? this.form.controls.selectedDate.addValidators(Validators.required) : this.form.controls.selectedDate.removeValidators(Validators.required)
	}
	@Input()
	set markAsTouched(value: boolean) {
		if (value) {
			this.form.controls.selectedDate.markAsTouched();
		}
	}

	_requiredText: string;

	constructor() { }

	ngOnInit(): void {
		this.subscribeToFormChanges();
	}

	dateFilter = (date?: Date): boolean => {
		if (!this.availableDays.length && !this.disableDays.length)
			return true;
		if (date != null) {
			if (this.disableDays.find(x => x.getTime() == date.getTime())) {
				return false;
			}
		}
		const day = (date || new Date()).getDay();
		return this.availableDays.includes(day);
	}

	delete() {
		this.form.controls.selectedDate.setValue(null);
		this.selectDate.next(null);
	}

	private subscribeToFormChanges() {
		this.form.controls.selectedDate.valueChanges.subscribe(selectedDate => {
			const date = fixDate(selectedDate);
			this.selectDate.next(date);
		});
	}
}
