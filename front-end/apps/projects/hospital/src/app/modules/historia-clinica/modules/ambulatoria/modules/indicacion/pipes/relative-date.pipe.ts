import { Pipe, PipeTransform } from '@angular/core';
import { DateDto } from '@api-rest/api-model';
import { dateDtoToDate } from '@api-rest/mapper/date-dto.mapper';

@Pipe({
	name: 'relativeDate'
})
export class RelativeDatePipe implements PipeTransform {

	transform(valueDate: DateDto): string {
		const value = dateDtoToDate(valueDate);
		const today = new Date();
		const yesterday = new Date(today);
		yesterday.setDate(yesterday.getDate() - 1);
		const dayBeforeYesterday = new Date(yesterday);
		dayBeforeYesterday.setDate(dayBeforeYesterday.getDate() - 1);

		if (value.toDateString() === today.toDateString()) {
			return 'indicacion.relative-date-pipe.PRESCRIBED_TODAY_TO_THIS_PATIENT';
		} else if (value.toDateString() === yesterday.toDateString()) {
			return 'indicacion.relative-date-pipe.PRESCRIBED_YESTERDAY_TO_THIS_PATIENT';
		} else if (value.toDateString() === dayBeforeYesterday.toDateString()) {
			return 'indicacion.relative-date-pipe.PRESCRIBED_THE_DAY_BEFORE_YESTERDAY_TO_THIS_PATIENT';
		} else {
			return value.toDateString();
		}
	}

}
