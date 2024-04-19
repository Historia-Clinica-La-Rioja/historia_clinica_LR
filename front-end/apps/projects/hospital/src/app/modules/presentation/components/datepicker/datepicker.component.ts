import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormControl, FormGroup } from "@angular/forms";
import { fixDate } from '@core/utils/date/format';
import * as moment from 'moment';
import { Moment } from "moment";

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

	constructor() { }

	ngOnInit(): void {
		this.subscribeToFormChanges();
	}

	dateFilter = (date?: Moment): boolean => {
		if (!this.availableDays.length && !this.disableDays.length)
			return true;
		if (date != null) {
			if (this.disableDays.find(x => x.getTime() == date.toDate().getTime())) {
				return false;
			}
		}
		const day = (date || moment()).day();
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
