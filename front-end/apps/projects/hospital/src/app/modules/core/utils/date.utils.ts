import {
	addMinutes,
	compareAsc,
	differenceInHours,
	differenceInMinutes,
	formatISO,
	format,
	isSameHour,
	isSameMinute
} from 'date-fns'

const WAITING_TIME = 'En espera desde hace';

const MIN_YEAR = 1900;
const MIN_MONTH = 0;
const MIN_DAY = 1;

export const MIN_DATE = new Date(MIN_YEAR, MIN_MONTH, MIN_DAY)

export enum DateFormat {
	VIEW_DATE = 'dd/MM/yyyy',
	API_DATE = 'yyyy-MM-dd',
	HOUR_MINUTE = 'HH:mm',
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
	MEDIUM_TIME = 'mediumTime',// es-AR format: 03:24:19
	LONG_TIME = 'longTime',// es-AR format: 03:24:19 GMT-3
	FULL_TIME = 'fullTime',// es-AR format: 03:24:19 GMT-03:00
}

export const dateToViewDate = (date: Date): string => format(date, DateFormat.VIEW_DATE);

export const timeToHourMinute = (time: Date): string => format(time, DateFormat.HOUR_MINUTE);

export const dateTimeToViewDateHourMinute = (dateTime: Date): string => `${dateToViewDate(dateTime)} - ${timeToHourMinute(dateTime)}`;

export const dateTimeToViewDateHourMinuteSecond = (dateTime: Date): string => `${dateToViewDate(dateTime)}, ${toHourMinuteSecond(dateTime)}`;

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

export function dateMinusDays(date: Date, days: number): Date {
	let newDate = new Date();
	newDate.setMonth(date.getMonth());
	newDate.setFullYear(date.getFullYear())
	newDate.setDate(date.getDate() - days);
	return newDate;
}

export function timeDifference(createdOn: Date) {
	const mins = differenceInMinutes(new Date(), createdOn);
	if (mins === 0) return `${WAITING_TIME} unos segundos`;
	if (mins < 60) return `${WAITING_TIME} ${mins} minuto/s`
	return `${WAITING_TIME} ${differenceInHours(new Date(), createdOn)} hora/s`
}

export function fromStringToDateByDelimeter(date: string, delimeter: string): Date {
	const dateData = date.split(delimeter);
	const year = +dateData[0];
	const month = +dateData[1] - 1;
	const dayDate = +dateData[2];
	return new Date(year, month, dayDate);
}

export const compare = (d1: Date, d2: Date): number => compareAsc(d1, d2); // -1 , 0 , 1

export const isEqualDate = (d1: Date, d2: Date): boolean => {
    return (compare(d1,d2) == 0)
}

const HOUR_MINUTE = 'HH:mm';
export const toHourMinute = (date: Date): string => format(date, HOUR_MINUTE);

const HOUR_MINUTE_SECOND = 'HH:mm:ss';
export const toHourMinuteSecond = (date: Date): string => format(date, HOUR_MINUTE_SECOND);

export function sameDayMonthAndYear(date1: Date, date2: Date): boolean {
	return (
		date1.getFullYear() === date2.getFullYear() &&
		date1.getMonth() === date2.getMonth() &&
		date1.getDate() === date2.getDate()
	);
}

export const sameHourAndMinute = (date: Date, dateToCompare: Date): boolean => isSameHour(date, dateToCompare) && isSameMinute(date, dateToCompare);