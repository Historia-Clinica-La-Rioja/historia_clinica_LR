import { Pipe, PipeTransform } from '@angular/core';
import { dateISOParseDate } from '@core/utils/moment.utils';

@Pipe({
	name: 'isoToDate'
})
export class IsoToDatePipe implements PipeTransform {

	transform(value: string): Date {
		return dateISOParseDate(value);
	}

}
