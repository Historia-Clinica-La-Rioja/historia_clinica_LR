import { isValid } from "date-fns";
import {	
	currentDateWeek, 
	dateParseTime,
	dateISOParseDate,
	isSameOrAfter,
	isSameOrBefore,
	isBetweenDates
} from "./moment.utils";

import { isAfter } from "date-fns";



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

const WEEK_LONG = 7;
const MONDAY = 1;
const TUESDAY = 2;
const WEDNESDAY = 3;
const THURSDAY = 4;
const FRIDAY = 5;
const SATURDAY = 6;
const SUNDAY = 0;

const today = new Date();
const yesterday = new Date();
yesterday.setDate(today.getDate() - 1);
const tomorrow = new Date();
tomorrow.setDate(today.getDate() + 1);
const includeExtremes = '[]';
const includeOnlyLeftExtreme = '[)';
const includeOnlyRightExtreme = '(]';
const notIncludeExtremes = '()';

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

})

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
