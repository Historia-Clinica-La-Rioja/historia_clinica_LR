import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';

@Component({
	selector: 'app-date-range-picker',
	templateUrl: './date-range-picker.component.html',
	styleUrls: ['./date-range-picker.component.scss']
})
export class DateRangePickerComponent implements OnInit {

	dateRangeForm: FormGroup<DateRangeForm> = new FormGroup<DateRangeForm>({
		start: new FormControl(null),
		end: new FormControl(null)
	});

	@Input() label: string;
	@Input() min?: Date = null;
	@Input() max?: Date = null;
	@Input() dateRange: DateRange;
	@Input() fixedRangeDays?: number;

	@Input()
	set disabled(disableForm: boolean) {
		disableForm ? this.dateRangeForm?.disable() : this.dateRangeForm?.enable();
	};

	@Output() dateRangeChange = new EventEmitter<DateRange>();

	constructor() { }

	ngOnInit() {

		if (this.dateRange) {
			this.dateRangeForm.controls.start.setValue(this.dateRange.start);
			this.dateRangeForm.controls.end.setValue(this.dateRange.end);
			this.dateRangeChange.emit({
				start: this.dateRangeForm.value.start,
				end: this.dateRangeForm.value.end
			});
		}
	}

	setAndEmitRangeForFixedDates(){
		let endDate = new Date(this.dateRangeForm.controls.start.value);
		endDate.setDate(endDate.getDate() + this.fixedRangeDays);

		this.dateRangeForm.controls.end.setValue(endDate);
		this.emitChange();
	}

	emitChange() {
		if (!this.dateRangeForm.value.end)
			return;
		const start = new Date(this.dateRangeForm.controls.start.value);
		const end = new Date(this.dateRangeForm.controls.end.value);
		this.dateRangeChange.emit({ start, end });
	}
}

interface DateRangeForm {
	start: FormControl<Date>;
	end: FormControl<Date>;
}

export interface DateRange {
	start: Date;
	end: Date;
}

//Se podria incorporar a futuro la previsualizacion del fixedRange como el ejemplo de "Date range picker with custom a selection strategy":
//https://material.angular.io/components/datepicker/examples
