import { DatePipe } from '@angular/common';
import { DatePipeFormat } from '@core/utils/date.utils';
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

	transform(paramDate: Date, type: 'date' | 'time' | 'datetime' | 'localtime'): string {
		if (!paramDate) {
			return undefined;
		}
		switch (type) {
			case 'date':
				return this.currentLang === 'es-AR' ? dateToViewDate(paramDate) : this.datePipe.transform(paramDate, DatePipeFormat.SHORT_DATE)
			case 'datetime':
				return this.currentLang === 'es-AR' ? dateTimeToViewDateHourMinute(paramDate) : this.datePipe.transform(paramDate, DatePipeFormat.SHORT);
			case 'time':
				return this.currentLang === 'es-AR' ? timeToHourMinute(paramDate) : this.datePipe.transform(paramDate, 'shortTime');
			default:
				return undefined;
		}
	}

}

const dateToViewDate = (date: Date): string => format(date, DateFormat.VIEW_DATE);

const timeToHourMinute = (time: Date): string => `${format(time, DateFormat.HOUR_MINUTE)}hs.`;

const dateTimeToViewDateHourMinute = (dateTime: Date): string => `${dateToViewDate(dateTime)} - ${timeToHourMinute(dateTime)}`;

enum DateFormat {
	VIEW_DATE = 'dd/MM/yyyy',
	API_DATE = 'yyyy-MM-dd',
	HOUR_MINUTE = 'HH:mm',
}
