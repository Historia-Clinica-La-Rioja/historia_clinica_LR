import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from "@angular/forms";
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
	@Input() minDate: Date;
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
}
