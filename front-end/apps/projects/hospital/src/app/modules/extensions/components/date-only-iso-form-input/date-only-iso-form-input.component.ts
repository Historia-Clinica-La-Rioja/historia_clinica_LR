import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { formatDateOnlyISO } from '@core/utils/date.utils';
import { UntypedFormGroup, UntypedFormControl } from '@angular/forms';
import { newDate } from '@core/utils/moment.utils';
import { startOfYear } from 'date-fns';
import { fixDate } from '@core/utils/date/format';

@Component({
	selector: 'app-date-only-iso-form-input',
	templateUrl: './date-only-iso-form-input.component.html',
	styleUrls: ['./date-only-iso-form-input.component.scss']
})
export class DateOnlyIsoFormInputComponent implements OnInit {
	@Input() label: string;
	@Output() rangeChange = new EventEmitter<string[]>();

	dateRange = new UntypedFormGroup({
		start: new UntypedFormControl(startOfYear(newDate())),
		end: new UntypedFormControl(newDate()),
	});

	public today: Date = new Date();
	constructor() { }

	ngOnInit(): void {
		this.emitRangeChane(this.dateRange.value);
		this.dateRange.valueChanges.subscribe(dates => {
			this.emitRangeChane(this.fromFormToDate(dates));
		});
	}

	private emitRangeChane(dates: { start: Date, end: Date }) {
		if (!dates.start || !dates.end) return;
		const startDate = formatDateOnlyISO(dates.start);
		const endDate = formatDateOnlyISO(dates.end);
		this.rangeChange.emit([startDate, endDate]);
	}

	private fromFormToDate(dates: FormDates):Dates {
		return {
			start: fixDate(dates.start),
			end: fixDate(dates.end)
		}
	}
}

interface Dates {
	start: Date,
	end: Date
}

interface FormDates {
	start: any // moment,
	end: any //moment
}