import { DatePipe, formatDate } from '@angular/common';
import { Pipe, PipeTransform } from '@angular/core';
import { DEFAULT_LANG } from '../../../app.component';
import { format } from 'date-fns';
@Pipe({
	name: 'dateFormat'
})
export class DateFormatPipe implements PipeTransform {

	datePipe: DatePipe;
	currentLang = DEFAULT_LANG;

	constructor() {
		this.datePipe = new DatePipe(this.currentLang);
	}

	transform(paramDate: Date, type: 'date' | 'time' | 'datetime' | 'fulldate' | 'localtime'): string {
		if (!paramDate) {
			return undefined;
		}
		switch (type) {
			case 'date':
				return this.currentLang === 'es-AR' ? dateToViewDate(paramDate) : this.datePipe.transform(paramDate, 'shortDate');
			case 'datetime':
				return this.currentLang === 'es-AR' ? dateTimeToViewDateHourMinute(paramDate) : this.datePipe.transform(paramDate, 'short');
			case 'time':
				return this.currentLang === 'es-AR' ? timeToHourMinute(paramDate) : this.datePipe.transform(paramDate, 'shortTime');
			case 'fulldate':
				return this.currentLang === 'es-AR' ? dateToFullDate(paramDate) : this.datePipe.transform(paramDate, 'fullDate');
			default:
				return undefined;
		}
	}

}

const dateToViewDate = (date: Date): string => format(date, DateFormat.VIEW_DATE);

const timeToHourMinute = (time: Date): string => `${format(time, DateFormat.HOUR_MINUTE)}hs.`;

const dateTimeToViewDateHourMinute = (dateTime: Date): string => `${dateToViewDate(dateTime)} - ${timeToHourMinute(dateTime)}`;

const dateToFullDate = (date: Date): string => formatDate(date, DateFormat.FULL_DATE, DEFAULT_LANG).split('/').join(' de ');

enum DateFormat {
	VIEW_DATE = 'dd/MM/yyyy',
	HOUR_MINUTE = 'HH:mm',
	FULL_DATE = 'EEEE, d/MMMM/yyyy',
}