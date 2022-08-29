import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { DAYS_OF_WEEK, MONTHS_OF_YEAR } from '@historia-clinica/modules/ambulatoria/modules/indicacion/constants/internment-indications';
import { isToday, isTomorrow, isYesterday, differenceInCalendarDays, getDay, getMonth, isSameDay } from 'date-fns';

@Component({
	selector: 'app-day-display',
	templateUrl: './day-display.component.html',
	styleUrls: ['./day-display.component.scss']
})
export class DayDisplayComponent implements OnInit {

	viewDay: ViewDate;
	today = true;
	yesterday = false;
	tomorrow = false;
	actualDate: Date = new Date();
	@Input() minDate: Date;
	@Output() actualDateEmmiter: EventEmitter<Date> = new EventEmitter<Date>();

	constructor() { }

	ngOnInit(): void {
		this.actualDateEmmiter.emit(this.actualDate);
		this.viewDay = {
			nameDay: DAYS_OF_WEEK[getDay(this.actualDate)],
			numberDay: this.actualDate.getDate(),
			month: MONTHS_OF_YEAR[getMonth(this.actualDate)]
		};
	}

	viewAnotherDay(daysToMove: number) {
		this.actualDate.setDate(this.actualDate.getDate() + daysToMove);
		this.today = isToday(this.actualDate);
		this.tomorrow = isTomorrow(this.actualDate);
		this.yesterday = isYesterday(this.actualDate);
		this.loadDay();
	}

	sameDay(date1: Date, date2: Date) {
		return isSameDay(date1, date2);
	}

	private loadDay() {
		const differenceInDays = differenceInCalendarDays(this.actualDate, this.minDate);
		if (differenceInDays >= 0) {
			this.viewDay = {
				nameDay: DAYS_OF_WEEK[getDay(this.actualDate)],
				numberDay: this.actualDate.getDate(),
				month: MONTHS_OF_YEAR[getMonth(this.actualDate)]
			};
			this.actualDateEmmiter.emit(this.actualDate);
		}
	}
}

interface ViewDate {
	nameDay: string,
	numberDay: number,
	month: string,
}