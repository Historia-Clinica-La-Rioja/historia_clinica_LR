import { compare, toHourMinute, toHourMinuteSecond, } from "./date.utils";

const ISODate = `2000-01-01T01:30:00.000-05:00`;
//La zona horaria que le paso en el iso la tiene en cuenta perop tambien tiene en cuenta la hora local. Suma las zonas horarias
const date = new Date(ISODate);

const biggerISODate = `2010-01-01T01:30:00.000-05:00`;
const biggerDate = new Date(biggerISODate);

const SMALLER = -1
const EQUAL = 0
const BIGGER = 1


describe('compare', () => {

	it(`should be ${SMALLER} when first date is before seconds one`, () => {
		const result = compare(date, biggerDate)
		expect(result).toBe(SMALLER)
	});

	it(`should be ${EQUAL} when dates are exactly the same`, () => {
		const result = compare(date, date)
		expect(result).toBe(EQUAL)
	});

	it(`should be ${BIGGER} when first date is after seconds one`, () => {
		const result = compare(biggerDate, date)
		expect(result).toBe(BIGGER)
	});

	it('should be NaN when some date is falsy', () => {
		let result = compare(null, date)
		expect(result).toBeNaN()

		result = compare(date, undefined)
		expect(result).toBeNaN()
	});
})


const DIGITS_FOR_HOURS = 2
const DIGITS_FOR_MINUTES = 2
const DIGITS_FOR_SECONDS = 2

describe('toHourMinute', () => {

	it(`should be 2 digits for hours and for minutes spreaded by ':' `, () => {
		const result = toHourMinute(date)
		const [hours, minutes] = result.split(':')
		expect(hours).toHaveSize(DIGITS_FOR_HOURS);
		expect(minutes).toHaveSize(DIGITS_FOR_MINUTES);
	});

})
describe('toHourMinuteSeconds', () => {

	it(`should be 2 digits for hours, for minutes and also for seconds spreaded by ':' `, () => {
		const result = toHourMinuteSecond(date)
		const [hours, minutes, seconds] = result.split(':')
		expect(hours).toHaveSize(DIGITS_FOR_HOURS);
		expect(minutes).toHaveSize(DIGITS_FOR_MINUTES);
		expect(seconds).toHaveSize(DIGITS_FOR_SECONDS);
	});

})
