import { isValid } from "date-fns";
import { Moment } from "moment";
import {
	DateFormat, buildFullDate, buildFullDateFromDate, buildFullDateV2,
	currentDateWeek, currentWeek, dateParse,
	dateParseTime,
	momentFormat, dateISOParseDate, momentFormatDate, momentParse, momentParseDate, momentParseDateTime, momentParseTime, newDate, newDateLocal, newMoment, newMomentLocal,
	isSameOrAfter,
	isSameOrBefore,
	isBetweenDates
} from "./moment.utils";
import * as moment from "moment";
import { isAfter } from "date-fns";

const UTC_OFFSET = 0;

const hours = `01`;
const minutes = `30`;
const seconds = `45`;
const time = `${hours}:${minutes}:${seconds}`;

const year = `1996`;
const month = `03`;
const date = `27`;

const ISOFormatedDate = `${year}-${month}-${date}`;
const arFormatedDate = `${date}/${month}/${year}`;

const initialHours = 0
const initialMinutes = 0
const initialSecs = 0


const invalidDateFormat = `thisIsAnInvalidStringedDate`;
const stringedFullDate = `${ISOFormatedDate}T${time}`;

//Should we use this methods of moment?
const momentInstance: Moment = moment(stringedFullDate);
const invalidMoment: Moment = moment(invalidDateFormat);
const dateInstance: Date = new Date(stringedFullDate);
const invalidDate: Date = new Date(invalidDateFormat);

//milisecs from AR TZ. Should test use this values?
const miliseconds = 827895600000; // milisegundos desde 1996-03-27T00:00:00
const utcMiliseconds = 827884800000 // milisegundos desde 1996-03-26T21:00:00
//const otherDateMiliseconds = 827899245000 // milisegundos desde 1996-03-27T01:00:45


const WEEK_LONG = 7;
const MONDAY = 1;
const TUESDAY = 2;
const WEDNESDAY = 3;
const THURSDAY = 4;
const FRIDAY = 5;
const SATURDAY = 6;
const SUNDAY = 0;

const UMBRAL = 9;

const today = new Date();
const yesterday = new Date();
yesterday.setDate(today.getDate() - 1);
const tomorrow = new Date();
tomorrow.setDate(today.getDate() + 1);
const includeExtremes = '[]';
const includeOnlyLeftExtreme = '[)';
const includeOnlyRightExtreme = '(]';
const notIncludeExtremes = '()';

function differenceBetween(num1: number, num2: number) {
	return Math.abs(num1 - num2);
}

describe('newMoment', () => {

	it('should be utc offset', () => {

		const result = newMoment();
		expect(result.isUtcOffset()).toBe(true);
		expect(result.utcOffset()).toBe(UTC_OFFSET);
	})
})

describe('newDate', () => {
	//Can't know offset because it depends on the execution
	it('should return same result as newMoment ', () => {

		const momentMilisecs = newMoment().valueOf();
		const dateMilisecs = newDate().getTime();

		expect(differenceBetween(momentMilisecs, dateMilisecs)).toBeLessThan(UMBRAL);
	});
})

describe('newMomentLocal and newDateLocal', () => {

	/**
	 * Can't know offset because it depends on the execution
	 * Can't know date nor time beacause it depends on the execution
	 */

	it('should return same time instance in localTime', () => {

		const momentResult = newMomentLocal().valueOf();
		const dateResult = newDateLocal().getTime();

		expect(differenceBetween(momentResult, dateResult)).toBeLessThan(UMBRAL);
	})

})

describe('momentParseDate)', () => {

	it('should return a zoneTimed instance that has given year, month, day and time 00:00:00', () => {
		const moment: Moment = momentParseDate(ISOFormatedDate);
		expect(moment.year()).toBe(Number(year));
		expect(moment.month() + 1).toBe(Number(month));
		expect(moment.date()).toBe(Number(date));
		expect(moment.hour()).toBe(initialHours);
		expect(moment.minutes()).toBe(initialMinutes);
		expect(moment.seconds()).toBe(initialSecs);
	});

	it('should parse string to zoneTimed moment', () => {
		const moment = momentParseDate(ISOFormatedDate);
		expect(moment.valueOf()).toBe(miliseconds)
	});

});

describe('dateISOParseDate', () => {

	it('should return a valid date instance when iso string is given ', () => {
		const dateResult: Date = dateISOParseDate(ISOFormatedDate);
		expect(dateResult.valueOf()).toBeTruthy();
		expect(isValid(dateResult)).toBeTrue();
	});

	it('should return a date instance that has given year, month, day and time 00:00:00', () => {
		const dateResult: Date = dateISOParseDate(ISOFormatedDate);
		expect(dateResult.getFullYear()).toBe(Number(year));
		expect(dateResult.getMonth() + 1).toBe(Number(month));
		expect(dateResult.getDate()).toBe(Number(date));
		expect(dateResult.getHours()).toBe(initialHours);
		expect(dateResult.getMinutes()).toBe(initialMinutes);
		expect(dateResult.getSeconds()).toBe(initialSecs);
	});

	it('should return an invalid date instance when given string is not ISO format ', () => {
		const dateResult: Date = dateISOParseDate(arFormatedDate);
		expect(dateResult.valueOf()).toBeNaN();
		expect(isValid(dateResult)).toBeFalse();
	});

	it('should return an invalid date instance when given string is null ', () => {
		const dateResult: Date = dateISOParseDate(null);
		expect(dateResult.valueOf()).toBeNaN();
		expect(isValid(dateResult)).toBeFalse();
	});

	it('should return same time instance as momentParseDate', () => {

		const momentResult = momentParseDate(ISOFormatedDate).valueOf();
		const dateResult = dateISOParseDate(ISOFormatedDate).getTime();

		expect(differenceBetween(momentResult, dateResult)).toBeLessThan(UMBRAL);
	})
})

describe('momentParseDateTime)', () => {

	it('should have given year', () => {
		const moment: Moment = momentParseDateTime(ISOFormatedDate);
		expect(moment.year()).toBe(Number(year));
	});
	it('should have given month', () => {
		const moment: Moment = momentParseDateTime(ISOFormatedDate);
		expect(moment.month() + 1).toBe(Number(month));
	});
	it('should have given day', () => {
		const moment: Moment = momentParseDateTime(ISOFormatedDate);
		expect(moment.date()).toBe(Number(date));
	});
	it('should have 0 hs', () => {
		const moment: Moment = momentParseDateTime(ISOFormatedDate);
		expect(moment.hours()).toBe(initialHours);
	});
	it('should have 0 minutes', () => {
		const moment: Moment = momentParseDateTime(ISOFormatedDate);
		expect(moment.minutes()).toBe(initialMinutes);
	});
	it('should have 0 seconds', () => {
		const moment: Moment = momentParseDateTime(ISOFormatedDate);
		expect(moment.seconds()).toBe(initialSecs);
	});


	it('create a moment instance based on current timezone and the given stringed date', () => {
		const moment = momentParseDateTime(ISOFormatedDate);
		expect(moment.valueOf()).toBe(utcMiliseconds)
	})
})


describe('momentParseTime)', () => {

	// crea un moment con la hora, mins y segs que le doy y con el offset de argentina.

	it('should have given hours', () => {
		const moment = momentParseTime(time);
		expect(moment.hour()).toBe(Number(hours));
	})

	it('should have given minutes', () => {
		const moment = momentParseTime(time);
		expect(moment.minutes()).toBe(Number(minutes));
	})

	it('should have given seconds', () => {
		const moment = momentParseTime(time);
		expect(moment.seconds()).toBe(Number(seconds));
	})

	it('should return currentDate when invalid time is given', () => {
		//No es un gran test este
		const result = momentParseTime(invalidDateFormat);
		expect(result).toBeTruthy();
	});
})

describe('dateParseTime', () => {

	it('should parse string to date', () => {
		const result = dateParseTime(time);
		expect(result.getHours()).toBe(Number(hours));
	});

	it('should return NaN when invalid time is given', () => {
		const result = dateParseTime(invalidDateFormat);
		expect(result.getHours()).toBeNaN();
	});

	it('should return same time instance as dateParseTime', () => {

		const momentResult = momentParseTime(time).valueOf();
		const dateInstance = dateParseTime(time).getTime();
		expect(differenceBetween(momentResult, dateInstance)).toBeLessThan(UMBRAL);
	})

})



describe('momentFormat', () => {

	it('should format moment instance to the given format', () => {

		let format = DateFormat.API_DATE;
		let result: string = momentFormat(momentInstance, format)
		expect(result).toBe(`${year}-${month}-${date}`);

		format = DateFormat.VIEW_DATE;
		result = momentFormat(momentInstance, format)
		expect(result).toBe(`${date}/${month}/${year}`);

		format = DateFormat.HOUR_MINUTE;
		result = momentFormat(momentInstance, format)
		expect(result).toBe(`${hours}:${minutes}`);

		format = DateFormat.HOUR_MINUTE_SECONDS;
		result = momentFormat(momentInstance, format)
		expect(result).toBe(`${time}`);

		format = DateFormat.FILE_DATE;
		result = momentFormat(momentInstance, format)
		expect(result).toBe(`${date}-${month}-${year}`);

		format = DateFormat.YEAR_MONTH;
		result = momentFormat(momentInstance, format)
		expect(result).toBe(`${year}${month}`);

	});

	it('should return "Fecha inválida" when the moment instance is invalid', () => {

		let format = DateFormat.API_DATE;
		let result: string = momentFormat(invalidMoment, format)
		expect(result).toBe('Fecha inválida');

	});


});

describe('momentFormatDate', () => {

	it('should format date instance to the given format', () => {

		let format = DateFormat.API_DATE;
		let result: string = momentFormatDate(dateInstance, format)
		expect(result).toBe(`${year}-${month}-${date}`);

		format = DateFormat.VIEW_DATE;
		result = momentFormatDate(dateInstance, format)
		expect(result).toBe(`${date}/${month}/${year}`);

	});

	it('should return "Fecha inválida" when the date instance is invalid', () => {
		let format = DateFormat.API_DATE;
		let result: string = momentFormatDate(invalidDate, format)
		expect(result).toBe('Fecha inválida');

	});

});



describe('momentParse', () => {

	//It builds a date from current timezone so we can't know the result beforehand

	it('sholud return NaN if string does not match with the given format', () => {
		const result = momentParse(invalidDateFormat, DateFormat.API_DATE);
		expect(result.valueOf()).toBeNaN()
	});

	it('sholud return NaN if string is empty', () => {
		const result = momentParse('', DateFormat.API_DATE);
		expect(result.valueOf()).toBeNaN()
	});

});

describe('dateParse', () => {

	it('should return same time instance as momentParse', () => {

		const momentResult = momentParse(ISOFormatedDate, DateFormat.API_DATE).valueOf();
		const dateResult = dateParse(ISOFormatedDate, DateFormat.API_DATE).getTime();

		expect(differenceBetween(momentResult, dateResult)).toBeLessThan(UMBRAL);
	})

});


describe('buildFullDate', () => {

	it('should build same time instance if hours and minutes are the same that ones in given momentInstance', () => {
		const result = buildFullDate(time, momentInstance);
		expect(result).toEqual(momentInstance);
	});
});
describe('buildFullDateV2', () => {
	it('should return same time instance as buildFullDate', () => {

		const v1 = buildFullDate(time, momentInstance).valueOf();
		const v2 = buildFullDateV2(time, momentInstance).getTime();

		expect(differenceBetween(v1, v2)).toBeLessThan(UMBRAL);
	})

});
describe('buildFullDateFromDate', () => {

	it('should return same time instance as buildFullDate and buildFullDateV2', () => {

		const v1 = buildFullDate(time, momentInstance).valueOf();
		const v2 = buildFullDateV2(time, momentInstance).getTime();
		const v3 = buildFullDateFromDate(time, dateInstance).getTime();

		expect(differenceBetween(v1, v3)).toBeLessThan(UMBRAL);
		expect(differenceBetween(v2, v3)).toBeLessThan(UMBRAL);
	})

});

describe('currentWeek', () => {

	it(`should return ${WEEK_LONG} moment instances`, () => {
		const result = currentWeek();
		expect(result).toBeTruthy();
		expect(result).toHaveSize(WEEK_LONG);
		result.forEach(day => expect(day instanceof moment).toBe(true));

	});

	it('should return the days of the week go in ascending order', () => {
		const result = currentWeek();
		for (let i = 1; i < result.length; i++) {
			expect(result[i].isAfter(result[i - 1])).toBe(true);
		}

	});

	it('should start on monday and finish at sunday', () => {
		const result = currentWeek();
		expect(result[0].day()).toBe(MONDAY);
		expect(result[1].day()).toBe(TUESDAY);
		expect(result[2].day()).toBe(WEDNESDAY);
		expect(result[3].day()).toBe(THURSDAY);
		expect(result[4].day()).toBe(FRIDAY);
		expect(result[5].day()).toBe(SATURDAY);
		expect(result[6].day()).toBe(SUNDAY);

	});

});

describe('currentDateWeek', () => {

	it(`should return ${WEEK_LONG} date instances`, () => {
		const result = currentDateWeek();
		expect(result).toBeTruthy();
		expect(result).toHaveSize(WEEK_LONG);
		result.forEach(day => expect(day instanceof Date).toBe(true));

	});

	it('should return the days of the week go in ascending order', () => {
		const result = currentDateWeek();
		for (let i = 1; i < result.length; i++) {
			expect(isAfter(result[i], result[i - 1])).toBe(true);
		}

	});

	it('should start on monday and finish at sunday', () => {
		const result = currentDateWeek();
		expect(result[0].getDay()).toBe(MONDAY);
		expect(result[1].getDay()).toBe(TUESDAY);
		expect(result[2].getDay()).toBe(WEDNESDAY);
		expect(result[3].getDay()).toBe(THURSDAY);
		expect(result[4].getDay()).toBe(FRIDAY);
		expect(result[5].getDay()).toBe(SATURDAY);
		expect(result[6].getDay()).toBe(SUNDAY);

	});

});

describe('isSameOrAfter', () => {

	it(`should return true if the date is same to the date to compare`, () => {
		const result = isSameOrAfter(today, today);
		expect(result).toEqual(true);
	});

	it(`should return true if the date is after to the date to compare`, () => {
		const result = isSameOrAfter(tomorrow, today);
		expect(result).toEqual(true);
	});

	it(`should return false if the date is before to the date to compare`, () => {
		const result = isSameOrAfter(yesterday, today);
		expect(result).toEqual(false);
	});
});

describe('isSameOrBefore', () => {

	it(`should return true if the date is same to the date to compare`, () => {
		const result = isSameOrBefore(today, today);
		expect(result).toEqual(true);
	});

	it(`should return true if the date is before to the date to compare`, () => {
		const result = isSameOrBefore(yesterday, today);
		expect(result).toEqual(true);
	});

	it(`should return false if the date is after to the date to compare`, () => {
		const result = isSameOrBefore(tomorrow, today);
		expect(result).toEqual(false);
	});
});

describe('isBetweenDates', () => {

	it(`should return true if the date is in the interval with inclusivity []`, () => {
		const inclusivity = includeExtremes;
		const result = isBetweenDates(today, yesterday, tomorrow, inclusivity);
		expect(result).toEqual(true);
	});

	it(`should return true if the date is in the interval with inclusivity []`, () => {
		const inclusivity = includeExtremes;
		const result = isBetweenDates(yesterday, yesterday, tomorrow, inclusivity);
		expect(result).toEqual(true);
	});

	it(`should return true if the date is in the interval with inclusivity []`, () => {
		const inclusivity = includeExtremes;
		const result = isBetweenDates(tomorrow, yesterday, tomorrow, inclusivity);
		expect(result).toEqual(true);
	});

	it(`should return false if the date isn't in the interval with inclusivity []`, () => {
		const inclusivity = includeExtremes;
		const result = isBetweenDates(tomorrow, yesterday, today, inclusivity);
		expect(result).toEqual(false);
	});

	it(`should return true if the date is in the interval with inclusivity (]`, () => {
		const inclusivity = includeOnlyRightExtreme;
		const result = isBetweenDates(today, yesterday, tomorrow, inclusivity);
		expect(result).toEqual(true);
	});

	it(`should return false if the date isn't in the interval with inclusivity (]`, () => {
		const inclusivity = includeOnlyRightExtreme;
		const result = isBetweenDates(yesterday, yesterday, tomorrow, inclusivity);
		expect(result).toEqual(false);
	});

	it(`should return true if the date is in the interval with inclusivity (]`, () => {
		const inclusivity = includeOnlyRightExtreme;
		const result = isBetweenDates(tomorrow, yesterday, tomorrow, inclusivity);
		expect(result).toEqual(true);
	});

	it(`should return true if the date is in the interval with inclusivity [)`, () => {
		const inclusivity = includeOnlyLeftExtreme;
		const result = isBetweenDates(today, yesterday, tomorrow, inclusivity);
		expect(result).toEqual(true);
	});

	it(`should return true if the date is in the interval with inclusivity [)`, () => {
		const inclusivity = includeOnlyLeftExtreme;
		const result = isBetweenDates(yesterday, yesterday, tomorrow, inclusivity);
		expect(result).toEqual(true);
	});

	it(`should return false if the date isn't in the interval with inclusivity [)`, () => {
		const inclusivity = includeOnlyLeftExtreme;
		const result = isBetweenDates(tomorrow, yesterday, tomorrow, inclusivity);
		expect(result).toEqual(false);
	});

	it(`should return true if the date is in the interval with inclusivity ()`, () => {
		const inclusivity = notIncludeExtremes;
		const result = isBetweenDates(today, yesterday, tomorrow, inclusivity);
		expect(result).toEqual(true);
	});

	it(`should return false if the date isn't in the interval with inclusivity ()`, () => {
		const inclusivity = notIncludeExtremes;
		const result = isBetweenDates(yesterday, yesterday, tomorrow, inclusivity);
		expect(result).toEqual(false);
	});

	it(`should return false if the date isn't in the interval with inclusivity ()`, () => {
		const inclusivity = notIncludeExtremes;
		const result = isBetweenDates(tomorrow, yesterday, tomorrow, inclusivity);
		expect(result).toEqual(false);
	});

});
