import { Injectable } from '@angular/core';
import { CalendarDateFormatter, DateFormatterParams } from 'angular-calendar';
import { DateFormat, dateToMoment, momentFormat } from '@core/utils/moment.utils';

@Injectable()
export class CustomDateFormatter extends CalendarDateFormatter {

	public weekViewTitle({ date, locale }: DateFormatterParams): string {
		let start: string;
		if (locale === 'es-AR') {
			const startDay = dateToMoment(date).startOf('isoWeek').format('D');
			const month = dateToMoment(date).startOf('isoWeek').format('MMMM');
			start = `${startDay} de ${month}`;
		} else {
			start = dateToMoment(date).startOf('isoWeek').format('LL');
		}
		const end = dateToMoment(date).endOf('isoWeek').format('LL');

		return `${start} - ${end}`;
	}

	public dayViewTitle({date, locale}: DateFormatterParams): string {
		return momentFormat(dateToMoment(date), DateFormat.HUMAN)
	}

}
