import { isValid } from "date-fns";
import { dateISOParseDate } from "./moment.utils";

const year = `1996`;
const month = `03`;
const date = `27`;

const ISOFormatedDate = `${year}-${month}-${date}`;
const arFormatedDate = `${date}/${month}/${year}`;


const initialHours = 0
const initialMinutes = 0
const initialSecs = 0



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
