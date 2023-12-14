import { Pipe, PipeTransform } from '@angular/core';
import { dateTimeToViewDateHourMinute, dateToViewDate, timeToHourMinute } from '@core/utils/date.utils';

@Pipe({
	name: 'dateFormat'
})
export class DateFormatPipe implements PipeTransform {

	transform(date: Date, type: 'date' | 'time' | 'datetime' | 'localtime'): string {
		switch (type) {
			case 'date':
				return dateToViewDate(date);
			case 'datetime':
				return dateTimeToViewDateHourMinute(date);
			case 'time':
				return timeToHourMinute(date);
			default:
				return undefined;
		}
	}

}
