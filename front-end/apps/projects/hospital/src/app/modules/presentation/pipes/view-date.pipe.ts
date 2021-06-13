import { Pipe, PipeTransform } from '@angular/core';
import { DateFormat, momentFormat, momentParseDate } from '@core/utils/moment.utils';

@Pipe({
	name: 'viewDate'
})
export class ViewDatePipe implements PipeTransform {

	transform(date: string): string {
		return momentFormat(momentParseDate(date), DateFormat.VIEW_DATE);
	}

}
