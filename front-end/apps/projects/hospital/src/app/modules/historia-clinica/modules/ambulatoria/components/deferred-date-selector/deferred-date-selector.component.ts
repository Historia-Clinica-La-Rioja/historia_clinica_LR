import { Component, EventEmitter, Input, Output } from '@angular/core';

@Component({
	selector: 'app-deferred-date-selector',
	templateUrl: './deferred-date-selector.component.html',
	styleUrls: ['./deferred-date-selector.component.scss']
})
export class DeferredDateSelectorComponent {
	@Input() minDate: Date;
	@Input() maxDate: Date;
	@Input() isSelectedDateInThePast: boolean;
	@Input() isDeferredSectionSelected: boolean = false;

	@Output() dateChange = new EventEmitter<Date>();
	@Output() timeChange = new EventEmitter<{ hours: number; minutes: number }>();
	@Output() deferredSectionToggle = new EventEmitter<boolean>();

	selectedDateTime: Date | null = null;

	toggleDeferredSection(event: Event): void {
		const isChecked = (event.target as HTMLInputElement).checked;
		this.deferredSectionToggle.emit(isChecked);
	}

	setDate(date: Date) {
		if (date) {
			this.selectedDateTime = this.mergeDateAndTime(date, this.selectedDateTime);
			this.dateChange.emit(this.selectedDateTime);
		}
	}

	setHours(time: { hours: number; minutes: number }) {
		if (!this.selectedDateTime) {
			this.selectedDateTime = new Date();
		}

		const dateWithTime = new Date(this.selectedDateTime);
		dateWithTime.setHours(time.hours);
		dateWithTime.setMinutes(time.minutes);
		this.selectedDateTime = dateWithTime;

		this.timeChange.emit({ hours: dateWithTime.getHours(), minutes: dateWithTime.getMinutes() });
	}


	private mergeDateAndTime(date: Date, currentDateTime: Date | null): Date {
		const newDate = new Date(date);
		if (currentDateTime) {
			newDate.setHours(currentDateTime.getHours());
			newDate.setMinutes(currentDateTime.getMinutes());
		}
		return newDate;
	}
}
