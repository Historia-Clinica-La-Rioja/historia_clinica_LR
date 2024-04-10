import { DatePipe } from '@angular/common';
import { DateFormatPipe } from './date-format.pipe';
import { TestBed } from '@angular/core/testing';

describe('DateFormatPipe', () => {

	let dateFormatPipe: DateFormatPipe;
	const year = 2024;
	const monthIndex = 6; // July
	const date = 1;
	const hours = 2;
	const minutes = 10
	const seconds = 10;
	const ms = 20;
	const exampleDate = new Date(year, monthIndex, date, hours, minutes, seconds, ms);

	beforeEach(() => {
		TestBed.configureTestingModule({
			providers: [DatePipe]
		});
		dateFormatPipe = new DateFormatPipe();
	});

	// Tengo que chequear tambien la zona horaria?

	it('create an instance', () => {
		expect(dateFormatPipe).toBeTruthy();
	});

	it('should translate to "dd/mm/yyyy" when es-AR and date format are set', () => {
		const result = dateFormatPipe.transform(exampleDate, 'date');
		expect(result).toBe('01/07/2024')
	});

	it('should translate to "HH:mmhs." when es-AR and time format are set', () => {
		const result = dateFormatPipe.transform(exampleDate, 'time');
		expect(result).toBe('02:10hs.')
	})

	it('should translate to "dd/mm/yyyy - HH:mmhs." when es-AR and datetime format are set', () => {
		const result = dateFormatPipe.transform(exampleDate, 'datetime');
		expect(result).toBe('01/07/2024 - 02:10hs.')
	})

	// it('should translate to en-US format when en-US and date format are set', () => {
	// 	dateFormatPipe.currentLang = 'en-US';
	// 	const result = dateFormatPipe.transform(exampleDate, 'date');
	// 	expect(result).toBe('7/1/24')
	// });

	// it('should translate to en-US format when en-US and time format are set', () => {
	// 	dateFormatPipe.currentLang = 'en-US';
	// 	const result = dateFormatPipe.transform(exampleDate, 'time');
	// 	expect(result).toBe('2:10 AM')
	// })

	// it('should translate to en-US format when en-US and datetime format are set', () => {
	// 	dateFormatPipe.currentLang = 'en-US';
	// 	const result = dateFormatPipe.transform(exampleDate, 'datetime');
	// 	expect(result).toBe('7/1/24, 2:10 AM')
	// })

	it('should translate to undefined when falsy date is set', () => {
		const translatedNull = dateFormatPipe.transform(null, 'datetime');
		expect(translatedNull).toBeUndefined();

		const translatedUndefined = dateFormatPipe.transform(undefined, 'time');
		expect(translatedUndefined).toBeUndefined();
	})


});
