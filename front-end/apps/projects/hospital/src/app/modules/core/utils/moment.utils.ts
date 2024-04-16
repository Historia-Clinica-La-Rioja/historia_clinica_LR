import * as moment from 'moment';
import { Moment } from 'moment';
import { DEFAULT_LANG } from '../../../app.component';
import { DateDto } from '@api-rest/api-model';
import { isAfter, isSameDay, isWithinInterval, parseISO } from 'date-fns';
import { addDays, eachDayOfInterval, parse, set, startOfWeek } from 'date-fns';

moment.locale(DEFAULT_LANG);

export enum DateFormat {
	API_DATE = 'YYYY-MM-DD',
	VIEW_DATE = 'DD/MM/YYYY',
	MONTH_AND_YEAR = 'MMMM YYYY',
	WEEK_DAY = 'ddd D',
	HUMAN = 'dddd LL',
	YEAR_FULL = 'YYYY',
	MONTH_FULL = 'MM',
	HOUR_MINUTE = 'HH:mm',
	HOUR_MINUTE_12 = 'hh:mm',
	HOUR_MINUTE_SECONDS = 'HH:mm:ss',
	FILE_DATE = 'DD-MM-YYYY',
	YEAR_MONTH = 'YYYYMM',
}

export enum MONTHS_OF_YEAR {
	Enero,
	Febrero,
	Marzo,
	Abril,
	Mayo,
	Junio,
	Julio,
	Agosto,
	Septiembre,
	Octubre,
	Noviembre,
	Diciembre
}

export const MAT_APP_DATE_FORMATS = {
	parse: {
		dateInput: 'L',
	},
	display: {
		dateInput: 'L',
		monthYearLabel: 'MMM YYYY',
		dateA11yLabel: 'LL',
		monthYearA11yLabel: 'MMMM YYYY',
	},
};

export const newMoment = (): Moment => moment.utc(Date.now());
export const newDate = (): Date => new Date(Date.now());

export const newMomentLocal = (): Moment => moment(Date.now());
export const newDateLocal = (): Date => new Date();

export const dateToMoment = (date: Date): Moment => moment.utc(date);

export const dateToMomentTimeZone = (date: Date): Moment => moment(date);

export const momentParseDate = (dateStr: string): Moment => momentParseDateTime(`${dateStr}T00:00:00.000-0300`);
export const dateISOParseDate = (dateISO: string): Date => parseISO(dateISO);

export const momentParseDateTime = (dateStr: string): Moment => moment.parseZone(dateStr);

export const momentParseTime = (timeStr: string): Moment => moment(`${timeStr}-0300`, DateFormat.HOUR_MINUTE_SECONDS);
export const dateParseTime = (timeStr: string): Date => {
	const [h, m, s] = timeStr.split(':');
	const date = new Date();
	date.setHours(Number(h));
	date.setMinutes(Number(m));
	date.setSeconds(s ? Number(s) : 0);
	date.setMilliseconds(0)
	return date;
};

//Estos dos no hay que usarlos, hay que usar datepipe
export const momentFormat = (momentDate: Moment, format?: DateFormat): string => momentDate.local().format(format);
export const momentFormatDate = (date: Date, format?: DateFormat): string => moment(date.getTime()).format(format);

export const momentParse = (dateString: string, format: DateFormat): Moment => moment(dateString, format);
export const dateParse = (dateString: string, format: DateFormat): Date => parse(dateString, mappedFormats[format], new Date());
enum FormatosFecha {
	AnioMesDia = 'yyyy-MM-dd',
	AnioMesDiaBarra = 'yyyy/MM/dd',
	DiaMesAnio = 'dd/MM/yyyy',
	FechaHora = 'yyyy-MM-dd HH:mm',
	HoraMinutosSegundos = 'HH:mm:ss',
	HoraMinutos = 'HH:mm',
	AnioMes = 'yyyyMM'
}
const mappedFormats = {
	[DateFormat.API_DATE]: FormatosFecha.AnioMesDia,
	[DateFormat.FILE_DATE]: FormatosFecha.AnioMesDiaBarra,
	[DateFormat.VIEW_DATE]: FormatosFecha.DiaMesAnio,
	[DateFormat.HOUR_MINUTE_SECONDS]: FormatosFecha.HoraMinutosSegundos,
	[DateFormat.HOUR_MINUTE]: FormatosFecha.HoraMinutos,
	[DateFormat.YEAR_MONTH]: FormatosFecha.AnioMes,

}

export const isMoment = (date: any): boolean => moment.isMoment(date);

export const buildFullDate = (time: string, date: Moment): Moment => {
	const timeMoment: Moment = momentParseTime(time);
	const output = date.clone();
	output.set({
		hour: timeMoment.hour(),
		minute: timeMoment.minute()
	});
	return output;
};
export const buildFullDateV2 = (time: string, date: Moment): Date => {
	const timeMoment: Moment = momentParseTime(time);
	const output = date.clone();
	output.set({
		hour: timeMoment.hour(),
		minute: timeMoment.minute()
	});

	return output.toDate();
};
export const buildFullDateFromDate = (time: string, date: Date): Date => {
	const [hours, mins] = time.split(':');
	const seconds = date.getSeconds()
	return set(date, { hours: Number(hours), minutes: Number(mins), seconds });
};


export const currentWeek = (): Moment[] => {
	const currentDate = moment();

	const weekStart = currentDate.clone().startOf('isoWeek');
	const days = [];

	for (let i = 0; i <= 6; i++) {
		days.push(moment(weekStart).add(i, 'days'));
	}
	return days;
};
export const currentDateWeek = (): Date[] => {
	const currentDate = new Date();
	const weekStart = addDays(startOfWeek(currentDate), 1);
	const days = eachDayOfInterval({ start: weekStart, end: addDays(weekStart, 6) });
	return days;
}

export const momentToDateDto = (momentDate: Moment): DateDto => {
	return {
		day: momentDate.date(),
		month: momentDate.month() + 1,
		year: momentDate.year(),
	};
}

export const isSameOrAfter = (date: Date, dateToCompare): boolean => {
	return isSameDay(date, dateToCompare) || isAfter(date, dateToCompare);
}

type Inclusivity = "()" | "[)" | "(]" | "[]";

export const isBetweenDates = (date: Date, start: Date, end: Date, inclusivity?: Inclusivity): boolean => {
	if (!inclusivity)
		return isWithinInterval(date, { start, end });

	let adjustedStartDate = start;
	let adjustedEndDate = end;

	if (inclusivity[0] === '(')
		adjustedStartDate = new Date(start.getTime() + 1);

	if (inclusivity[1] === ')')
		adjustedEndDate = new Date(end.getTime() - 1);

	return isWithinInterval(date, { start: adjustedStartDate, end: adjustedEndDate });
}
