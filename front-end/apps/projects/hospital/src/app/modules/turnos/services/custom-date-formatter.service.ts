import { Injectable } from '@angular/core';
import { CalendarDateFormatter, DateFormatterParams } from 'angular-calendar';
import { endOfISOWeek, startOfISOWeek } from 'date-fns';
import { DatePipe } from '@angular/common';
import { DEFAULT_LANG } from '../../../app.component';
import { DatePipeFormat } from '@core/utils/date.utils';

@Injectable()
export class CustomDateFormatter extends CalendarDateFormatter {

	datePipe = new DatePipe(DEFAULT_LANG);
	public weekViewTitle({ date }: DateFormatterParams): string {
		const start = this.datePipe.transform(startOfISOWeek(date), DatePipeFormat.LONG_DATE)
		const end = this.datePipe.transform(endOfISOWeek(date), DatePipeFormat.LONG_DATE)
		return `${start} - ${end}`;
	}

	public dayViewTitle({ date }: DateFormatterParams): string {
		return this.datePipe.transform(date, DatePipeFormat.LONG_DATE);
	}

}
