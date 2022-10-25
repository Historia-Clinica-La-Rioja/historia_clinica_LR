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

export function getDayHoursIntervalsByMinuteValue(date: Date, timeRange: number): Date[] {
	var dividedDate = [];
	var dateToDivide = new Date(date);
	dateToDivide.setHours(0, 0, 0, 0);

	for (let currentRangeValue = 0; currentRangeValue <= (60 / timeRange) * 24; currentRangeValue++) {
		dividedDate[currentRangeValue] = new Date(dateToDivide);
		dateToDivide = addMinutes(dateToDivide, timeRange);
	}

	dividedDate[dividedDate.length - 1] = addMinutes(dividedDate[dividedDate.length - 1], -1);
	return dividedDate;
}

export function getDayHoursRangeIntervalsByMinuteValue(startDate: Date, endDate: Date, timeRange: number): Date[] {
	const hours = Math.abs(endDate.getTime() - startDate.getTime()) / (1000 * 60 * 60) % 24;
	var dividedDate = [];

	for (let currentRangeValue = 0; currentRangeValue < (60 / timeRange) * hours; currentRangeValue++) {
		dividedDate[currentRangeValue] = new Date(startDate);
		startDate = addMinutes(startDate, timeRange);
	}

	return dividedDate;
}

export function fromStringToDate(date: string): Date {
	const dateData = date.split("/");
	return new Date(+dateData[2], +dateData[1] - 1, +dateData[0]);
}

export function datePlusDays(date: Date, days: number): Date {
	let newDate = new Date();
	newDate.setMonth(date.getMonth());
	newDate.setFullYear(date.getFullYear())
	newDate.setDate(date.getDate() + days);
	return newDate;
}