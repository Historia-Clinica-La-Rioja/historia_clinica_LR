import { CapitalizeFirstLetterPipe } from './capitalize-first-letter.pipe';

describe('CapitalizeFirstLetterPipe', () => {
	let pipe: CapitalizeFirstLetterPipe;

	beforeEach(() => {
	  pipe = new CapitalizeFirstLetterPipe();
	});

	it('should create an instance', () => {
	  expect(pipe).toBeTruthy();
	});

	it('should capitalize only the first letter of a lowercase string', () => {
	  expect(pipe.transform('hello world')).toBe('Hello world');
	});

	it('should not change the rest of the string after the first letter', () => {
	  expect(pipe.transform('hELLO wORLD')).toBe('HELLO wORLD');
	});

	it('should handle an already capitalized string correctly', () => {
	  expect(pipe.transform('Hello world')).toBe('Hello world');
	});

	it('should handle a single character string', () => {
	  expect(pipe.transform('a')).toBe('A');
	});

	it('should return an empty string when the input is empty', () => {
	  expect(pipe.transform('')).toBe('');
	});

	it('should return the string unchanged if the first character is not a letter', () => {
	  expect(pipe.transform('123 hello')).toBe('123 hello');
	});

	it('should handle strings with leading spaces correctly', () => {
	  expect(pipe.transform(' hello')).toBe(' hello');
	});

	it('should return an empty string if the input is null', () => {
	  expect(pipe.transform(null)).toBe('');
	});

	it('should return an empty string if the input is undefined', () => {
	  expect(pipe.transform(undefined)).toBe('');
	});

	it('should capitalize the first letter even if it is a special character', () => {
	  expect(pipe.transform('!hello world')).toBe('!hello world');
	});
});
