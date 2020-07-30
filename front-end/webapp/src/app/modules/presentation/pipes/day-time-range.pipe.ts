import { Pipe, PipeTransform } from '@angular/core';
import * as moment from 'moment';
import { momentFormatDate, DateFormat } from '@core/utils/moment.utils';
import { DatePipe } from '@angular/common';

@Pipe({
  name: 'dayTimeRange'
})
export class DayTimeRangePipe implements PipeTransform {

  transform(initDate: Date, endDate: Date): string {

	const dayOfWeekNumber: number = initDate.getDay();
	let dayOfWeek: string =  moment().localeData().weekdays()[dayOfWeekNumber];
	dayOfWeek = dayOfWeek[0].toUpperCase() +  dayOfWeek.slice(1);

	const initHourMin = new DatePipe('es-AR').transform(initDate, 'HH:mm');
	const endHourMin = new DatePipe('es-AR').transform(endDate, 'HH:mm');
	return dayOfWeek + ', ' + initHourMin + 'hs a ' +  endHourMin + 'hs';
  }

}
