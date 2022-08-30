import { Injectable } from '@angular/core';

@Injectable({
	providedIn: 'root'
})
export class CalendarDateService {

	private calendarDate: Date = new Date();

	constructor() { }

	setCalendarDate(date: Date) {
		this.calendarDate = date;
	}

	getCalendarDate() {
		return this.calendarDate;
	}
}
