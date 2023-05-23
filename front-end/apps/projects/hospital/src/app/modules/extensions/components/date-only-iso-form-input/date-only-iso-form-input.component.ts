import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { formatDateOnlyISO } from '@core/utils/date.utils';
import {UntypedFormGroup, UntypedFormControl} from '@angular/forms';
import * as moment from "moment";

@Component({
	selector: 'app-date-only-iso-form-input',
	templateUrl: './date-only-iso-form-input.component.html',
	styleUrls: ['./date-only-iso-form-input.component.scss']
})
export class DateOnlyIsoFormInputComponent implements OnInit {
	@Input() label: string;
	@Output() rangeChange = new EventEmitter<string[]>();

	dateRange = new UntypedFormGroup({
		start: new UntypedFormControl(moment().startOf('year')),
		end: new UntypedFormControl(moment()),
	});

	public today: Date = new Date();
	constructor() { }

	ngOnInit(): void {
		this.emitRangeChane(this.dateRange.value);
		this.dateRange.valueChanges.subscribe(range => this.emitRangeChane(range));
	}

	private emitRangeChane({start, end}) {

		if (!start || !end) return;

		const startDate = formatDateOnlyISO(start.toDate());
		const endDate = formatDateOnlyISO(end.toDate());
		this.rangeChange.emit([startDate, endDate]);
	}

}
