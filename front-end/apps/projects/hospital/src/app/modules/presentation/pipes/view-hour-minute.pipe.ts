import { Pipe, PipeTransform } from '@angular/core';
import { momentFormat, momentParseTime, DateFormat } from '@core/utils/moment.utils';

@Pipe({
  name: 'viewHourMinute'
})
export class ViewHourMinutePipe implements PipeTransform {

  	transform(date: string): string {
		return momentFormat(momentParseTime(date), DateFormat.HOUR_MINUTE);
	}

}
