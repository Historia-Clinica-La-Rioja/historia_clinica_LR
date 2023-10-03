import { Pipe, PipeTransform } from '@angular/core';
import { dateTimeToViewDateHourMinute, dateToViewDate } from '@api-rest/mapper/date-dto.mapper';

@Pipe({
	name: 'dateFormat'
})
export class DateFormatPipe implements PipeTransform {

	transform(date: Date, type: 'date' | 'time' | 'datetime'): string {
		switch (type) {
			case 'date':
				return  dateToViewDate(date);
			case 'datetime':
				return dateTimeToViewDateHourMinute(date);
			default:
				return undefined;
		}
	}

}
