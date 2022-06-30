import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from "@angular/forms";
import * as moment from 'moment';
import { Moment } from "moment";

@Component({
	selector: 'app-datepicker',
	templateUrl: './datepicker.component.html',
	styleUrls: ['./datepicker.component.scss']
})

export class DatepickerComponent implements OnInit {

	form: FormGroup;
	@Input() title: string;
	@Input() dateToSetInDatepicker: Date;
	@Input() maxDate: Date;
	@Input() minDate: Date;
	@Input() availableDays: number[];
	@Output() selectDate: EventEmitter<Date> = new EventEmitter();

	constructor(
		private readonly formBuilder: FormBuilder
	) { }

	ngOnInit(): void {
		this.form = this.formBuilder.group({
			selectedDate: [null]
		})
		this.form.controls.selectedDate.setValue(this.dateToSetInDatepicker);
	}

	changeDate() {
		const moment: Moment = this.form.value.selectedDate;
		this.selectDate.next(moment.toDate());
	}

	dateFilter = (d?: Moment): boolean => {
		const day = (d || moment()).day();
		// Prevent Saturday and Sunday from being selected.
		return this.availableDays.includes(day);
	  }
}
