import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { formatDateOnlyISO } from '@core/utils/date.utils';
import {FormGroup, FormControl} from '@angular/forms';

@Component({
	selector: 'app-date-only-iso-form-input',
	templateUrl: './date-only-iso-form-input.component.html',
	styleUrls: ['./date-only-iso-form-input.component.scss']
})
export class DateOnlyIsoFormInputComponent implements OnInit {
	@Input() label: string;
	@Input() hint: string;
	@Output() rangeChange = new EventEmitter<string[]>();

	dateRange = new FormGroup({
		start: new FormControl(),
		end: new FormControl(),
	});

	constructor() { }

	ngOnInit(): void {
		this.dateRange.valueChanges.subscribe(range => this.emitRangeChane(range));
	}

	private emitRangeChane({start, end}) {

		if (!start || !end) return;

		const startDate = formatDateOnlyISO(start.toDate());
		const endDate = formatDateOnlyISO(end.toDate());
		this.rangeChange.emit([startDate, endDate]);
	}

}
