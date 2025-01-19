import { isAfter, isBefore, isSameDay, isWithinInterval, parseISO } from 'date-fns';
import { addDays, eachDayOfInterval, parse, set, startOfWeek } from 'date-fns';

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

export const newDate = (): Date => new Date(Date.now());

export const newDateLocal = (): Date => new Date();



export const dateISOParseDate = (dateISO: string): Date => parseISO(dateISO);


export const dateParseTime = (timeStr: string): Date => {
	const [h, m, s] = timeStr.split(':');
	const date = new Date();
	date.setHours(Number(h));
	date.setMinutes(Number(m));
	date.setSeconds(s ? Number(s) : 0);
	date.setMilliseconds(0)
	return date;
};


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



export const buildFullDateFromDate = (time: string, date: Date): Date => {
	const [hours, mins] = time.split(':');
	const seconds = date.getSeconds()
	return set(date, { hours: Number(hours), minutes: Number(mins), seconds });
};


export const currentDateWeek = (): Date[] => {
	const currentDate = new Date();
	const weekStart = addDays(startOfWeek(currentDate), 1);
	const days = eachDayOfInterval({ start: weekStart, end: addDays(weekStart, 6) });
	return days;
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

export const isSameOrBefore = (date: Date, dateToCompare: Date): boolean => isSameDay(date, dateToCompare) || isBefore(date, dateToCompare);