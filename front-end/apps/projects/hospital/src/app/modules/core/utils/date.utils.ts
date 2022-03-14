import { DatePipe } from '@angular/common';
import { DEFAULT_LANG } from '../../../app.component';
import {
	addMinutes,
	formatISO,
} from 'date-fns'

const MIN_YEAR = 1900;
const MIN_MONTH = 0;
const MIN_DAY = 1;

export const MIN_DATE = new Date(MIN_YEAR, MIN_MONTH, MIN_DAY)

export enum DateFormat {
	VIEW_DATE = 'dd/MM/yyyy',
}

export enum DatePipeFormat {
	SHORT = 'short',// es-AR format: 27/3/96 03:24
	MEDIUM = 'medium',// es-AR format: 27 mar. 1996 03:24:19
	LONG = 'long',// es-AR format: 27 de marzo de 1996 a las 03:24:19 GMT-3
	FULL = 'full',// es-AR format: miércoles, 27 de marzo de 1996 a las 03:24:19 GMT-03:00
	SHORT_DATE = 'shortDate',// es-AR format: 27/3/96
	MEDIUM_DATE = 'mediumDate',// es-AR format: 27 mar. 1996
	LONG_DATE = 'longDate',// es-AR format: 27 de marzo de 1996
	FULL_DATE = 'fullDate',//  es-AR format: miércoles, 27 de marzo de 1996
	SHORT_TIME = 'shortTime',// es-AR format: 03:24
	MEDIUM_TIME = 'mediumTime',// es-AR format: 03:24:19
	LONG_TIME = 'longTime',// es-AR format: 03:24:19 GMT-3
	FULL_TIME = 'fullTime',// es-AR format: 03:24:19 GMT-03:00
}

export function formatDateOnlyISO(date: Date) {
	return formatISO(date, { representation: 'date' });
}

export function formatTimeOnlyISO(date: Date) {
	return formatISO(date, { representation: 'time' });
}

export function formatDayOfWeek(date: Date): string{
	const datePipe = new DatePipe(DEFAULT_LANG);
	return datePipe.transform(date, 'EEEE');
}

export function formatHour(date: Date): string{
	const datePipe = new DatePipe(DEFAULT_LANG);
	return datePipe.transform(date, DatePipeFormat.SHORT_TIME);
}

export function obtainAppointmentRangeDates(date: Date, timeRange: number): Date[]{
	var dividedDate = [];
	var dateToDivide = new Date(date);
	dateToDivide.setHours(0,0,0,0);

	for (let currentRangeValue = 0; currentRangeValue <= (60/timeRange)*23; currentRangeValue++){
		dividedDate[currentRangeValue] = new Date(dateToDivide);
		dateToDivide = addMinutes(dateToDivide, timeRange);
	}

	return dividedDate;
}
