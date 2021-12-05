import * as moment from 'moment';
import { Moment } from 'moment';
import { DEFAULT_LANG } from '../../../app.component';

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

export const dateToMomentTimeZone = (date: Date): Moment => moment(date);

export const momentParseDate = (dateStr: string): Moment => momentParseDateTime(`${dateStr}T00:00:00.000-0300`);

export const momentParseDateTime = (dateStr: string): Moment => moment.parseZone(dateStr);

export const momentParseTime = (timeStr: string): Moment => moment(`${timeStr}-0300`, DateFormat.HOUR_MINUTE_SECONDS);

export const momentFormatDate = (date: Date, format?: DateFormat): string => moment.utc(date.getTime()).format(format);

export const momentFormat = (momentDate: Moment, format?: DateFormat): string => momentDate.local().format(format);

export const momentParse = (dateString: string, format: DateFormat): Moment => moment(dateString, format);

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

export const currentWeek = (): Moment[] => {
	const currentDate = moment();

	const weekStart = currentDate.clone().startOf('isoWeek');
	const days = [];

	for (let i = 0; i <= 6; i++) {
		days.push(moment(weekStart).add(i, 'days'));
	}
	return days;
};
