import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';

@Component({
	selector: 'app-date-range-picker',
	templateUrl: './date-range-picker.component.html',
	styleUrls: ['./date-range-picker.component.scss']
})
export class DateRangePickerComponent implements OnInit {

	dateRangeForm: FormGroup;

	@Input() label: string;
	@Input() min?: Date = null;
	@Input() max = new Date();
	@Input() dateRange: DateRange;
	@Input()
	set disabled(value: boolean) {
		if (value)
			this.dateRangeForm?.disable();
		else
			this.dateRangeForm?.enable();
	};
	@Output() dateRangeChange = new EventEmitter<DateRange>();

	constructor(
		private readonly formBuilder: FormBuilder,
	) { }

	ngOnInit() {
		this.dateRangeForm = this.formBuilder.group({
			start: [null],
			end: [null]
		});

		if (this.dateRange) {
			this.dateRangeForm.controls.start.setValue(this.dateRange.start);
			this.dateRangeForm.controls.end.setValue(this.dateRange.end);
			this.dateRangeChange.emit({
				start: this.dateRangeForm.value.start,
				end: this.dateRangeForm.value.end
			});
		}

		if (this.disabled)
			this.dateRangeForm.disable();
	}

	emitChange() {
		if (!this.dateRangeForm.value.end)
			return;
		const start = new Date(this.dateRangeForm.controls.start.value);
		const end = new Date(this.dateRangeForm.controls.end.value);
		this.dateRangeChange.emit({ start, end });
	}
}

export interface DateRange {
	start: Date;
	end: Date;
}