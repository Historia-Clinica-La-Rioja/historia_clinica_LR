import { Injectable } from '@angular/core';
import { CalendarDateFormatter, DateFormatterParams } from 'angular-calendar';
import { endOfISOWeek, startOfISOWeek } from 'date-fns';
import { DateFormatPipe } from '@presentation/pipes/date-format.pipe';

@Injectable()
export class CustomDateFormatter extends CalendarDateFormatter {

	datePipe = new DateFormatPipe();
	public weekViewTitle({ date }: DateFormatterParams): string {
		const start = this.datePipe.transform(startOfISOWeek(date), 'fulldate')
		const end = this.datePipe.transform(endOfISOWeek(date), 'fulldate')
		return `${start} - ${end}`;
	}

	public dayViewTitle({ date }: DateFormatterParams): string {
		return this.datePipe.transform(date, 'fulldate');
	}

}
