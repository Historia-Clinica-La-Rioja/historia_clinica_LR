import { Injectable } from '@angular/core';
import { ProfessionalDto } from '@api-rest/api-model';

@Injectable({
	providedIn: 'root'
})
export class CalendarProfessionalInformation {

	private calendarDate: Date = new Date();
	private professionalSelected: ProfessionalDto;

	constructor() { }

	setProfessionalSelected(professional: ProfessionalDto) {
		this.professionalSelected = professional;
		this.calendarDate = new Date();
	}

	getProfessionalSelected(): ProfessionalDto {
		return this.professionalSelected;
	}

	setCalendarDate(date: Date) {
		this.calendarDate = date;
	}

	getCalendarDate() {
		return this.calendarDate;
	}
}
