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
}

export const newMoment = (): Moment => moment.utc(Date.now());

export const momentParseDate = (dateStr: string): Moment => moment.parseZone(dateStr);

export const momentFormatDate = (date: Date, format?: DateFormat): string => moment.utc(date.getTime()).format(format);
