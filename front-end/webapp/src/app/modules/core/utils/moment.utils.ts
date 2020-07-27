import { Moment } from 'moment'
import * as moment from 'moment';

moment.locale('es-AR');

export enum DateFormat {
	API_DATE = 'YYYY-MM-DD',
	VIEW_DATE = 'DD/MM/YYYY',
	MONTH_AND_YEAR = 'MMMM YYYY',
	WEEK_DAY = 'ddd D',
	HUMAN = 'dddd LL',
	YEAR_FULL = 'YYYY',
	MONTH_FULL = 'MM',
	HOUR_MINUTE = 'HH:mm',
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

export const dateToMoment = (date: Date): Moment => moment.utc(date);

export const momentParseDate = (dateStr: string): Moment => momentParseDateTime(`${dateStr}T00:00:00.000-0300`);

export const momentParseDateTime = (dateStr: string): Moment => moment.parseZone(dateStr);

export const momentFormatDate = (date: Date, format?: DateFormat): string => moment.utc(date.getTime()).format(format);

export const momentFormat = (momentDate: Moment, format?: DateFormat): string => momentDate.local().format(format);
